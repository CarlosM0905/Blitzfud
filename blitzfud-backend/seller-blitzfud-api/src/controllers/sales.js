const urljoin = require('url-join');

const Order = require('../models/order');

const A_MONTH_AGO = (30 * 24 * 60 * 60 * 1000);
const { STATUS } = require('../constants/order');
const STATUS_CONDITIONS = { $in: [STATUS.SUCCESS, STATUS.CANCELLED] };

const ENDPOINT_PATH = urljoin(process.env.HOST, 'sales');

const { responseToMongooseError } = require('../helpers/responses');

function getAllSales (req, res) {
    const marketId = req.user.market;
    Order.find({ 
            market: marketId, 
            status: STATUS_CONDITIONS,
            updatedAt: {
                $gte: new Date(new Date().getTime() - A_MONTH_AGO)
            } 
        })
        .populate('customer', 'firstName lastName')
        .populate('deliveryProvider', 'firstName lastName')
        .select('status deliveryMethod createdAt updatedAt totalAmount \
                 totalQuantityOfProducts customer deliveryProvider')
        .sort({ updatedAt: -1 })
        .exec()
        .then(docs => {
            const response = {
                sales: docs.map(order => {
                    return {
                        status: order.status,
                        deliveryMethod: order.deliveryMethod,
                        createdAt: order.createdAt,
                        updatedAt: order.updatedAt,
                        totalAmount: order.totalAmount,
                        totalQuantityOfProducts: order.totalQuantityOfProducts,
                        customer: {
                            firstName: order.customer.firstName,
                            lastName: order.customer.lastName,
                        },
                        deliveryProvider: order.deliveryProvider,
                        _id: order._id,
                    }
                }),
                request: {
                    type: 'GET',
                    url: ENDPOINT_PATH
                } 
            }
            res.status(200).json(response);
        })
        .catch(err => {
            responseToMongooseError(res, err)
        });
}

function getSale (req, res) {
    const marketId = req.user.market;
    const saleId = req.params.saleId;
    Order.findOne({ 
            _id: saleId,
            market: marketId, 
            status: STATUS_CONDITIONS 
        })
        .populate('customer', 'firstName lastName')
        .populate('deliveryProvider', 'firstName lastName')
        .select('status deliveryMethod createdAt updatedAt \
                 totalAmount deliveryPrice items customer deliveryProvider')
        .exec()
        .then(doc => {
            if (!doc) {
                return res.status(404).json({
                    message: 'No se ha encontrado la venta'
                })
            }
            const response = {
                status: doc.status,
                deliveryMethod: doc.deliveryMethod,
                createdAt: doc.createdAt,
                updatedAt: doc.updatedAt,
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
                },
                deliveryProvider: doc.deliveryProvider,
                _id: doc._id,
                request: {
                    type: 'GET',
                    url: ENDPOINT_PATH
                } 
            }
            res.status(200).json(response);
        })
        .catch(err => {
            responseToMongooseError(res, err)
        });    
}

module.exports = {
    getAllSales,
    getSale
}