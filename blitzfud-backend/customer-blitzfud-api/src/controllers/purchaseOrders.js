const urljoin = require('url-join');

const Order = require('../models/order');
const PurchaseOrder = require('../models/purchaseOrder');

const { STATUS } = require('../constants/order');
const ORDERS_PATH = urljoin(process.env.HOST, 'orders'); 
const ENDPOINT_PATH = urljoin(process.env.HOST, 'purchaseOrders');

const { responseToMongooseError } = require('../helpers/responses');

function getAllPurchaseOrders (req, res) {
    const customerId = req.user._id;
    PurchaseOrder.find({ customer: customerId })
        .select('orders totalAmount createdAt _id')
        .populate({
            path: 'orders',
            populate: {
                path: 'market',
                select: 'name location'
            },
            match: {
                status: {
                    $in: [STATUS.PREPROCESSING, STATUS.IN_PROGRESS]
                }
            }
        })
        .exec()
        .then(docs => {
            if (docs.length != 0){
                docs = docs.filter(doc => doc.orders.length > 0);
                const response = {
                    count: docs.length,
                    purchaseOrders: docs.map(doc => {
                        return {
                            totalAmount: doc.totalAmount,
                            createdAt: doc.createdAt,
                            orders: doc.orders.map(order => {
                                    return {
                                        totalAmount: order.totalAmount,
                                        status: order.status,
                                        deliveryMethod: order.deliveryMethod,
                                        market: {
                                            name: order.market.name,
                                            location: order.market.location,
                                            _id: order.market._id
                                        },
                                        _id: order._id
                                    }
                                }
                            ),
                            _id: doc._id,
                            request: {
                                type: 'GET',
                                url: urljoin(ORDERS_PATH, doc._id.toString())
                            }
                        }
                    }),
                    request: {
                        type: 'POST',
                        url: ENDPOINT_PATH,
                        body: {
                            type: 'Array',
                            items: {
                                product: 'ObjectId',
                                quantity: 'Number'
                            },
                            deliveryMethod: 'String',
                            location: {
                                coordinates: 'Array'
                            }
                        }  
                    }
                }
                res.status(200).json(response);
            } else {
                res.status(200).json({
                    count: 0,
                    purchaseOrders: [],
                    request: {
                        type: 'POST',
                        url: ENDPOINT_PATH,
                        body: {
                            type: 'Array',
                            items: {
                                product: 'ObjectId',
                                quantity: 'Number'
                            },
                            deliveryMethod: 'String',
                            location: {
                                coordinates: 'Array'
                            }
                        }  
                    }     
                });
            }

        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

function createPurchaseOrder (req, res) {
    const customerId = req.user._id;
    const subcarts = req.body;
    Order.create(subcarts)
        .then(result => {
            const ordersId = result.map(order => order._id);
            const purchaseOrder = new PurchaseOrder({
                customer: customerId,
                orders: ordersId
            });
            purchaseOrder.save()
                .then(result => {
                    res.status(201).json({
                        message: 'Orden creada con éxito',
                        request: {
                            type: 'GET',
                            url: urljoin(ENDPOINT_PATH, result._id.toString())
                        }
                    });
                })
                .catch(err => {
                    responseToMongooseError(res, err);
                });              
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });    
}

module.exports = {
    getAllPurchaseOrders,
    createPurchaseOrder
}