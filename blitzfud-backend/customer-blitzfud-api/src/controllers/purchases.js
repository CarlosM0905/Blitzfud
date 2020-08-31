const urljoin = require('url-join');

const PurchaseOrder = require('../models/purchaseOrder');

const { STATUS } = require('../constants/order');
const ORDERS_PATH = urljoin(process.env.HOST, 'orders'); 
const ENDPOINT_PATH = urljoin(process.env.HOST, 'purchases');

const { responseToMongooseError } = require('../helpers/responses');

function getAllPurchases (req, res) {
    const customerId = req.user._id;
    let conditions = { customer: customerId }
    if (req.query.from) {
        let from = new Date(req.query.from);
        let to;
        if (req.query.to){
            to = new Date(req.query.to);
        } else {
            to = new Date(from.getTime());
            to.setDate(from.getDate() + 1);
        }

        conditions.createdAt = {
            '$gte': from, 
            '$lt': to
        }      
    } else {
        let today = new Date();
        today.setHours(0,0,0,0);
        let tomorrow = new Date(today.getTime());
        tomorrow.setDate(today.getDate() + 1);

        conditions.createdAt = {
            '$gte': today, 
            '$lt': tomorrow
        }
    }
    PurchaseOrder.find(conditions)
        .select('orders totalAmount createdAt _id')
        .populate({
            path: 'orders',
            populate: {
                path: 'market',
                select: 'name'
            },
            match: {
                status: {
                    $in: [STATUS.SUCCESS, STATUS.CANCELLED, STATUS.DENIED]
                }
            }
        })
        .exec()
        .then(docs => {
            if (docs.length != 0){
                docs = docs.filter(doc => doc.orders.length > 0);
                const response = {
                    count: docs.length,
                    purchases: docs.map(doc => {
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
                        type: 'GET',
                        url: ENDPOINT_PATH
                    }
                }
                res.status(200).json(response);
            } else {
                res.status(200).json({
                    count: 0,
                    purchases: [],
                    request: {
                        type: 'GET',
                        url: ENDPOINT_PATH,
                    }     
                });
            }

        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}


module.exports = {
    getAllPurchases
}