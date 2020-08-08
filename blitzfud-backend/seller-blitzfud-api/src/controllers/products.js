const urljoin = require('url-join');

const Product = require('../models/product');

const ENDPOINT_PATH = urljoin(process.env.HOST, 'products');
const STATUS_CONDITIONS = { $in: ['available', 'out-of-stock'] };

const { responseToMongooseError } = require('../helpers/responses');

function getAllProducts (req, res) {
    const marketId = req.user.market;
    const offset = req.query.offset || 0;
    const limit = req.query.limit || 20;
    Product.find({ market: marketId , status: STATUS_CONDITIONS })
        .select('name content unitOfMeasurement price status maxQuantityPerOrder highlight category _id')
        .populate('category', 'name')
        .skip(offset)
        .limit(limit)
        .exec()
        .then(docs => {
            const response = {
                count: docs.length,
                products: docs.map(doc => {
                    return {
                        name: doc.name,
                        content: doc.content,
                        unitOfMeasurement: doc.unitOfMeasurement,
                        price: doc.price,
                        status: doc.status,
                        maxQuantityPerOrder: doc.maxQuantityPerOrder,
                        highlight: doc.highlight,
                        category: {
                            name: doc.category.name,
                            _id: doc.category._id
                        },
                        _id: doc._id,
                        request: {
                            type: 'GET',
                            url: urljoin(ENDPOINT_PATH, doc._id.toString())
                        }
                    }
                }),
                request: {
                    type: 'POST',
                    url: ENDPOINT_PATH,
                    body: {
                        name: 'String',
                        unitOfMeasurement: 'String',
                        content: 'Number',
                        maxQuantityPerOrder: 'Number',
                        price: 'Number',
                        highlight: 'Boolean',
                        category: 'ObjectId'
                    }
                }
            }
            res.status(200).json(response);
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

function createProduct (req, res) {
    let body = req.body;
    const marketId = req.user.market;
    const product = new Product ({
        name: body.name,
        unitOfMeasurement: body.unitOfMeasurement,
        content: body.content,
        maxQuantityPerOrder: body.maxQuantityPerOrder,
        price: body.price,
        highlight: body.highlight,
        market: marketId,
        category: body.category
    });
    product.save()
        .then(result => {
            res.status(201).json({
                message: 'Producto creado con éxito'
            });
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

function getProduct (req, res) {
    const marketId = req.user.market;
    const productId = req.params.productId;
    Product.findOne({ _id: productId, market: marketId, status: STATUS_CONDITIONS })
        .select('name unitOfMeasurement content maxQuantityPerOrder price highlight status category _id')
        .populate({
            path : 'category',
            select: 'name _id',
        })
        .exec()
        .then(doc => {
            if (doc) {
                const response = {
                    name: doc.name,
                    content: doc.content,
                    unitOfMeasurement: doc.unitOfMeasurement,
                    price: doc.price,
                    status: doc.status,
                    maxQuantityPerOrder: doc.maxQuantityPerOrder,
                    highlight: doc.highlight,
                    category: doc.category,
                    _id: doc._id
                }
                res.status(200).json(response);
            } else {
                res.status(404).json({
                    message: 'No existe dicho producto'
                });
            } 
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

function updateProduct (req, res) {
    let body = req.body;
    const marketId = req.user.market;
    const productId = req.params.productId;
    const updateOps = {};
    for (const ops of body) {
        updateOps[ops.propName] = ops.value;
    }
    Product.updateOne({ _id: productId, market: marketId, status: STATUS_CONDITIONS }, { $set: updateOps })
        .exec()
        .then(result => {
            if (result.n === 0) {
                res.status(404).json({
                    message: 'No se encontró el producto'
                });
            } else {
                res.status(200).json({
                    message: 'Producto actualizado'
                });
            }
        })        
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

function deleteProduct (req, res) {
    const marketId = req.user.market;
    const productId = req.params.productId;
    Product.updateOne({ _id: productId, market: marketId, status: STATUS_CONDITIONS }, { status: 'deleted' })
        .exec()
        .then(result => {
            if (result.nModified > 0) {
                res.status(200).json({
                    message: 'Producto eliminado'
                });
            } else {
                res.status(404).json({
                    message: 'No se encontró el producto'
                })
            }
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

module.exports = {
    getAllProducts,
    createProduct,
    getProduct,
    updateProduct,
    deleteProduct
}