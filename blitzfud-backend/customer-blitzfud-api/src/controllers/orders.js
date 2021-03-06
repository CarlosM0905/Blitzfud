const urljoin = require('url-join');

const Order = require('../models/order');

const PURCHASE_ORDERS_PATH = urljoin(process.env.HOST, 'purchaseOrders');
const MARKETS_PATH = urljoin(process.env.HOST, 'markets');

const { responseToMongooseError } = require('../helpers/responses');

function getOrder (req, res) {
    const customerId = req.user._id;
    const orderId = req.params.orderId;
    Order.findOne({
        _id: orderId,
        customer: customerId
    })
    .populate('market', 'name')
    .populate('deliveryProvider', 'firstName lastName profilePhotoURL')
    .exec()
    .then(doc => {
        if (doc) {
            const response = {
                status: doc.status,
                deliveryMethod: doc.deliveryMethod,
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
                market: {
                    name: doc.market.name,
                    _id: doc.market._id,
                    request: {
                        type: 'GET',
                        url: urljoin(MARKETS_PATH, doc.market._id.toString())
                    }
                },
                deliveryPoint: doc.deliveryPoint,
                deliveryProvider: doc.deliveryProvider,
                _id: doc._id,
                request: {
                    type: 'GET',
                    url: PURCHASE_ORDERS_PATH
                }
            }
            res.status(200).json(response);
        } else {
            res.status(404).json({
                message: 'No se ha encontrado una Orden con dicho id',
                request: {
                    type: 'GET',
                    url: PURCHASE_ORDERS_PATH
                }
            })
        }
    })
    .catch(err => {
        responseToMongooseError(res, err)
    })
}

module.exports = {
    getOrder
}