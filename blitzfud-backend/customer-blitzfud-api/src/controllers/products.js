const urljoin = require('url-join');

const Market = require('../models/market');
const Product = require('../models/product');

const { STATUS } = require('../constants/product');
const AVAILABLE_STATUS = [STATUS.AVAILABLE, STATUS.OUT_OF_STOCK];

const ENDPOINT_PATH = urljoin(process.env.HOST, 'products');

const { responseToMongooseError } = require('../helpers/responses');

function getAllProducts (req, res) {
    const marketId = req.params.marketId;
    const categoryId = req.query.category;
    const offset = req.query.offset || 0;
    const limit = req.query.limit || 20;
    let productConditions = { 
        status: { $in: AVAILABLE_STATUS }
    };
    if (categoryId){
        productConditions.category = categoryId;
    }
    Market.findById(marketId)
        .select('products')
        .populate({
            path: 'products',
            select: 'name unitOfMeasurement content maxQuantityPerOrder price highlight status category _id',
            options: {
                skip: offset,
                limit: limit,
                sort: { created: -1 }
            },
            match: productConditions
        })
        .exec()
        .then(docs => {
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
                        status: doc.status,
                        category: doc.category,
                        _id: doc._id,
                        request: {
                            type: 'GET',
                            url: urljoin(ENDPOINT_PATH, doc._id.toString())
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

function getProduct (req, res) {
    const productId = req.params.productId;
    Product.findOne({
            _id: productId,
            status: { $in: AVAILABLE_STATUS }    
        })
        .select('name unitOfMeasurement content maxQuantityPerOrder price highlight status category _id')
        .populate()
        .exec()
        .then(doc => {
            if (doc) {
                const response = {
                    name: doc.name,
                    unitOfMeasurement: doc.unitOfMeasurement,
                    content: doc.content,
                    maxQuantityPerOrder: doc.maxQuantityPerOrder,
                    price: doc.price,
                    highlight: doc.highlight,
                    status: doc.status,
                    category: doc.category,
                    _id: doc._id,
                    request: {
                        type: 'GET',
                        url: ENDPOINT_PATH
                    }
                }
                res.status(200).json(response)
            } else {
                res.status(404).json({
                    message: 'No se ha encontrado un producto con dicho id'
                })
            } 
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

module.exports = {
    getAllProducts,
    getProduct
}