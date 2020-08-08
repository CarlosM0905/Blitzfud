const Market = require('../models/market');

function getAllMarkets (req, res) {
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
            }
        })
        .select('name description deliveryMethods deliveryPrice location products _id')
        .populate({
            path: 'products',
            select: 'name unitOfMeasurement content maxQuantityPerOrder price _id',
            match: { 
                status: 'available',
                highlight: true, 
            }
        })
        .exec()
        .then(docs => {
            docs = docs.filter(doc => doc.products.length > 0);
            const response = {
                count: docs.length,
                markets: docs.map(doc => {
                    return {
                        name: doc.name,
                        description: doc.description,
                        deliveryMethods: doc.deliveryMethods,
                        deliveryPrice: doc.deliveryPrice,
                        location: doc.location,
                        products: doc.products,
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

function getMarket (req, res) {
    const marketId = req.params.marketId;
    Market.findById(marketId)
        .select('name description deliveryMethods deliveryPrice location products _id')
        .populate({
            path: 'products',
            select: 'name unitOfMeasurement content maxQuantityPerOrder price _id',
            match: { 
                status: 'available',
                highlight: true 
            },
            options: { limit: 10 }
        })
        .exec()
        .then(doc => {
            if (doc) {
                res.status(200).json({
                    name: doc.name,
                    description: doc.description,
                    deliveryMethods: doc.deliveryMethods,
                    deliveryPrice: doc.deliveryPrice,
                    location: doc.location,
                    products: doc.products,
                })
            } else {
                res.status(404).json({
                    message: 'No se ha encontrado una tienda con dicho identificador'
                })
            } 
        })
        .catch(err => {
            res.status(500).json({
                message: 'Error interno de servidor, reintente en unos minutos por favor',
                error: err
            });
        });
}

module.exports = {
    getAllMarkets,
    getMarket
}
