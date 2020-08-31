const urljoin = require('url-join');

const Order = require('../models/order');

const { STATUS } = require('../constants/order');
const { DELIVERY_METHODS } = require('../constants/market');
const STATUS_CONDITIONS = { $in: [STATUS.PREPROCESSING, STATUS.IN_PROGRESS] };

const ORDERS_PATH = urljoin(process.env.HOST, 'orders');

const { groupBy } = require('../helpers/functions');
const { responseToMongooseError } = require('../helpers/responses');

function getAllOrders (req, res) {
    const marketId = req.user.market;
    Order.find({ market: marketId, status: STATUS_CONDITIONS })
        .populate('customer', 'firstName lastName phoneNumber')
        .populate('deliveryProvider', 'firstName lastName phoneNumber')
        .exec()
        .then(orders => {
            const groupedOrders = groupBy(orders, order => order.deliveryMethod);
            const deliveryOrders = groupedOrders.get(DELIVERY_METHODS.DELIVERY) || [];
            const pickupOrders = groupedOrders.get(DELIVERY_METHODS.PICKUP) || [];
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
            responseToMongooseError(res, err)
        });
}

function updateOrder (req, res) {
    const marketId = req.user.market;
    const orderId = req.params.orderId;
    const statusToChange = req.body.status;
    const deliveryProvider = req.body.deliveryProvider;
    Order.findOne({ _id: orderId, market: marketId })
        .exec()
        .then(order => {
            if (!order) {
                return res.status(404).json({
                    message: 'No existe orden con dicho id'
                })
            }
            switch (order.status) {
                case STATUS.PREPROCESSING: 
                    if (statusToChange !== STATUS.IN_PROGRESS &&
                        statusToChange !== STATUS.DENIED) {
                        return res.status(400).json({
                            message: 'Cambio de estado no permitido'
                        })
                    } else {
                        if (statusToChange === STATUS.IN_PROGRESS &&
                            order.deliveryMethod === DELIVERY_METHODS.DELIVERY) {
                            if (deliveryProvider) {
                                order.deliveryProvider = deliveryProvider;
                            } else {
                                return res.status(400).json({
                                    message: 'No se especificó repartidor'
                                });
                            }
                        }
                    }
                    break;
        
                case STATUS.IN_PROGRESS:
                    if (statusToChange !== STATUS.SUCCESS && 
                        statusToChange !== STATUS.CANCELLED) {
                        return res.status(400).json({
                            message: 'Cambio de estado no permitido'
                        })
                    }
                    break;
        
                case STATUS.SUCCESS: 
                case STATUS.CANCELLED:
                    return res.status(400).json({
                        message: 'Orden ya ha sido cerrada'
                    });
            }
            order.status = statusToChange;
            order.save()
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

function formatOrders (orders) {
    return orders.map(order => {
        let deliveryPoint;
        if (order.deliveryPoint) {
            deliveryPoint = order.deliveryPoint.coordinates;
        }
        return {
            status: order.status,
            createdAt: order.createdAt,
            totalAmount: order.totalAmount,
            totalQuantityOfProducts: order.totalQuantityOfProducts,
            deliveryPrice: order.deliveryPrice,
            items: order.items.map(item => {
                return {
                    name: item.name,
                    price: item.price,
                    quantity: item.quantity,
                    itemPrice: item.itemPrice,
                    productId: item.product
                }
            }),
            customer: {
                firstName: order.customer.firstName,
                lastName: order.customer.lastName,
                phoneNumber: order.customer.phoneNumber,
                deliveryPoint: deliveryPoint,
            },
            deliveryProvider: order.deliveryProvider,
            _id: order._id,
        }
    })
}

module.exports = {
    getAllOrders,
    updateOrder
}