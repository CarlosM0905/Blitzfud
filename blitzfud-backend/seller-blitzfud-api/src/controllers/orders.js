const urljoin = require('url-join');

const Order = require('../models/order');

const ORDERS_PATH = urljoin(process.env.HOST, 'orders');
const STATUS_CONDITIONS = { $in: ['preprocessing', 'in-progress'] };

const { groupBy } = require('../helpers/functions');
const { responseToMongooseError } = require('../helpers/responses');

function formatOrders (orders) {
    return orders.map(doc => {
        let deliveryPoint;
        if (doc.deliveryPoint) {
            deliveryPoint = doc.deliveryPoint.coordinates;
        }
        return {
            status: doc.status,
            createdAt: doc.createdAt,
            totalAmount: doc.totalAmount,
            deliveryPrice: doc.deliveryPrice,
            items: doc.items.map(item => {
                return {
                    name: item.name,
                    price: item.price,
                    quantity: item.quantity,
                    itemPrice: item.itemPrice,
                    productId: item.product
                }
            }),
            customer: {
                firstName: doc.customer.firstName,
                lastName: doc.customer.lastName,
                phoneNumber: doc.customer.phoneNumber,
                deliveryPoint: deliveryPoint,
            },
            deliveryProvider: doc.deliveryProvider,
            _id: doc._id,
        }
    })
}

function getAllOrders (req, res) {
    const marketId = req.user.market;
    Order.find({ market: marketId, status: STATUS_CONDITIONS })
        .populate('customer', 'firstName lastName phoneNumber')
        .populate('deliveryProvider', 'firstName lastName phoneNumber')
        .exec()
        .then(docs => {
            const ordersGrouped = groupBy(docs, doc => doc.deliveryMethod);
            const deliveryOrders = ordersGrouped.get('delivery') || [];
            const pickupOrders = ordersGrouped.get('pickup') || [];
            const response = {
                deliveryOrders: formatOrders(deliveryOrders),
                pickupOrders: formatOrders(pickupOrders),
                request: {
                    type: 'GET',
                    url: ORDERS_PATH
                } 
            }
            res.status(200).json(response);
        })
        .catch(err => {
            console.log(err)
            responseToMongooseError(res, err)
        });
}

function updateOrder (req, res) {
    const marketId = req.user.market;
    const orderId = req.params.orderId;
    const statusToChange = req.body.status;
    const deliveryProvider = req.body.deliveryProvider;
    Order.findOne({
        _id: orderId,
        market: marketId
    })
    .exec()
    .then(doc => {
        if (!doc) {
            return res.status(500).json({
                message: 'No existe orden con dicho id'
            })
        }
        switch (doc.status) {
            case 'preprocessing': 
                if (statusToChange !== 'in-progress' &&
                    statusToChange !== 'denied')  {
                    return res.status(400).json({
                        message: 'Cambio de estado no permitido'
                    })
                } else if (statusToChange === 'in-progress' &&
                           doc.deliveryMethod === 'delivery'){
                    if (deliveryProvider) {
                        doc.deliveryProvider = deliveryProvider
                    } else {
                        return res.status(400).json({
                            message: 'No se especificó repartidor'
                        });
                    }
                }
                break;
            case 'in-progress':
                if (statusToChange !== 'success' && 
                    statusToChange !== 'cancelled') {
                    return res.status(400).json({
                        message: 'Cambio de estado no permitido'
                    })
                }
                break;
            case 'success': case 'cancelled':
                return res.status(400).json({
                    message: 'Orden ya ha sido cerrada'
                })
        }
        doc.status = statusToChange;
        doc.save()
            .then(result => {
                res.status(200).json({
                    message: 'Se cambió de estado satisfactoriamente',
                })
            })
            .catch(err => {
                responseToMongooseError(res, err)
            })
    })    
    .catch(err => {
        responseToMongooseError(res, err)
    });
}

module.exports = {
    getAllOrders,
    updateOrder
}