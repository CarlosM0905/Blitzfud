const urljoin = require('url-join');

const Market = require('../models/market');
const Seller = require('../models/seller');

const ENDPOINT_PATH = urljoin(process.env.HOST, 'market');

const { responseToMongooseError } = require('../helpers/responses');

function getMyMarket (req, res) {
    const marketId = req.user.market;
    Market.findById(marketId)
        .select('name description marketStatus deliveryMethods deliveryPrice location _id')
        .exec()
        .then(doc => {
            if (doc) {
                const response = {
                    name: doc.name, 
                    description: doc.description,
                    marketStatus: doc.marketStatus,
                    deliveryMethods: doc.deliveryMethods,
                    deliveryPrice: doc.deliveryPrice,
                    location: doc.location,
                    _id: doc._id,
                    request: {
                        type: 'GET',
                        url: ENDPOINT_PATH
                    }
                }
                res.status(200).json(response);
            } else {
                res.status(404).json({
                    message: 'No dispones de una tienda'
                });
            } 
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

function createMarket (req, res) {
    let body = req.body;
    const sellerId = req.user._id;
    const market = new Market ({
        name: body.name,
        description: body.description,
        deliveryMethods: body.deliveryMethods,
        deliveryPrice: body.deliveryPrice,
        location: body.location,
    });
    market.save()
        .then(result => {
            const marketId = result._id;
            Seller.updateOne({ _id: sellerId }, { market: marketId })
                .exec()
                .then(result => {
                    res.status(201).json({
                        message: 'Tienda creada con éxito'
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

function updateMarket (req, res) {
    let body = req.body;
    const marketId = req.user.market;
    const updateOps = {};
    for (const ops of body) {
        updateOps[ops.propName] = ops.value;
    }
    Market.updateOne({ _id: marketId }, { $set: updateOps })
        .exec()
        .then(result => {
            if (result.n === 0) {
                res.status(404).json({
                    message: 'No se encontró la tienda'
                });
            } else {
                res.status(200).json({
                    message: 'Tienda actualizada'
                });
            }
        })        
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

module.exports = {
    getMyMarket,
    createMarket,
    updateMarket   
}
