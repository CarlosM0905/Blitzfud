const Market = require('../models/market');

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
            }
        })
        .select('products name _id')
        .populate({
            path: 'products',
            select: 'name unitOfMeasurement content maxQuantityPerOrder price _id',
            match: {
                nameLowercase: { $regex: '.*' + req.query.keyword.toLowerCase() + '.*' }, 
                status: 'available',
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
                        products: market.products,
                        name: market.name,
                        _id: market._id
                    }
                })
            }
            res.status(200).json(response);
        })
        .catch(err => {
            res.status(500).json({
                message: 'Error interno de servidor, reintente en unos minutos por favor',
                error: err
            })
        });
}

module.exports = {
    getProducts
}