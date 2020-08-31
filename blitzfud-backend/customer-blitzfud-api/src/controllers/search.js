const urljoin = require('url-join');

const Market = require('../models/market');

const MARKET_CONSTANTS = require('../constants/market');
const PRODUCT_CONSTANTS = require('../constants/product');

const PRODUCTS_PATH = urljoin(process.env.HOST, 'products');

const { responseToMongooseError } = require('../helpers/responses');

function getProducts (req, res) {
    const coords = [req.query.lon, req.query.lat];
    Market.find({
            location: {
                $near: {
                    $geometry: {
                        type: 'Point',
                        coordinates: coords
                    },
                    $maxDistance: 5 * 1000  // 5km
                }
            },
            marketStatus: MARKET_CONSTANTS.STATUS.OPEN
        })
        .select('products name _id')
        .populate({
            path: 'products',
            select: 'name unitOfMeasurement content maxQuantityPerOrder price _id',
            match: {
                nameLowercase: { $regex: '.*' + req.query.keyword.toLowerCase() + '.*' }, 
                status: PRODUCT_CONSTANTS.STATUS.AVAILABLE,
                highlight: true 
            },
            options: { limit: 30 }
        })
        .exec()
        .then(docs => {
            docs = docs.filter((doc) => {
                return doc.products.length > 0
            });
            const response = {
                count: docs.length,
                markets: docs.map(market => {
                    return {
                        products: market.products.map(doc => {
                            return {
                                name: doc.name,
                                unitOfMeasurement: doc.unitOfMeasurement,
                                content: doc.content,
                                maxQuantityPerOrder: doc.maxQuantityPerOrder,
                                price: doc.price,
                                _id: doc._id,
                                request: {
                                    type: 'GET',
                                    url: urljoin(PRODUCTS_PATH, doc._id.toString())
                                }
                            }
                        }),
                        name: market.name,
                        _id: market._id
                    }
                })
            }
            res.status(200).json(response);
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

function getProductsOfMarket (req, res) {
    const marketId = req.params.marketId;
    Market.findById(marketId)
        .select('products')
        .populate({
            path: 'products',
            select: 'name unitOfMeasurement content maxQuantityPerOrder price _id',
            match: {
                nameLowercase: { $regex: '.*' + req.query.keyword.toLowerCase() + '.*' }, 
                status: PRODUCT_CONSTANTS.STATUS.AVAILABLE,
                highlight: true 
            },
            options: { limit: 30 }
        })
        .exec()
        .then(docs => {
            if (!docs) {
                return res.status(404).json({
                    message: 'Tienda no ha sido encontrada'
                });
            }
            docs = docs.products;
            const response = {
                count: docs.length,
                products: docs.map(doc => {
                    return {
                        name: doc.name,
                        unitOfMeasurement: doc.unitOfMeasurement,
                        content: doc.content,
                        maxQuantityPerOrder: doc.maxQuantityPerOrder,
                        price: doc.price,
                        highlight: doc.highlight,
                        category: doc.category,
                        _id: doc._id,
                        request: {
                            type: 'GET',
                            url: urljoin(PRODUCTS_PATH, doc._id.toString())
                        }
                    }
                })
            }
            res.status(200).json(response);
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });    
}

module.exports = {
    getProducts,
    getProductsOfMarket
}