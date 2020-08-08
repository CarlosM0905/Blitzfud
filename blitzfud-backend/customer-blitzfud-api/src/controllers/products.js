const Market = require('../models/market');
const Product = require('../models/product');

function getAllProducts (req, res) {
    const marketId = req.params.marketId;
    Market.findById(marketId)
        .select('products')
        .populate({
            path: 'products',
            select: 'name unitOfMeasurement content maxQuantityPerOrder price highlight category _id',
            match: { 
                status: 'available'
            }
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
                        category: doc.category,
                        _id: doc._id,
                    }
                })
            }
            res.status(200).json(response);
        })
        .catch(err => {
            res.status(500).json({
                message: 'Error interno de servidor, reintente en unos minutos por favor',
                error: err
            });
        });
}

function getProduct (req, res) {
    const productId = req.params.productId;
    Product.findById(productId)
        .select('name description unitOfMeasurement content maxQuantityPerOrder price highlight status market category _id')
        .populate()
        .exec()
        .then(doc => {
            if (doc) {
                res.status(200).json(doc)
            } else {
                res.status(404).json({
                    message: 'No se ha encontrado un producto con dicho id'
                })
            } 
        })
        .catch(err => {
            res.status(500).json({
                message: 'Error interno de servidor, reintente en unos minutos por favor',
                error: err
            })
        });
}

module.exports = {
    getAllProducts,
    getProduct
}