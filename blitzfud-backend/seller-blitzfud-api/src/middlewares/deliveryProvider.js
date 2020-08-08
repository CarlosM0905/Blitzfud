const DeliveryProvider = require('../models/deliveryProvider');

const { getMarketLocation } = require('../helpers/market');
const { responseToMongooseError } = require('../helpers/responses');

async function isNear (req, res, next) {
    const marketId = req.user.market;
    const deliveryProvider = req.body.deliveryProvider;
    const coords = await getMarketLocation(marketId);
    DeliveryProvider.findOne({
            location: {
                $near: {
                    $geometry: {
                        type: 'Point',
                        coordinates: coords
                    },
                    $maxDistance: 5 * 1000  // 5km
                }
            },
            _id: deliveryProvider,
            status: 'available',
            accountStatus: 'active'
        })
        .exec()
        .then(doc => {
            if (doc) {
                next();
            } else {
                return res.status(400).json({
                    message: 'No se ha encontrado a repartidor'
                })
            }
        })
        .catch(err => {
            responseToMongooseError(res, err);
        }); 
}

module.exports = {
    isNear
}