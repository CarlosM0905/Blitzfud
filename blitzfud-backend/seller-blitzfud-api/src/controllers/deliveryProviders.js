const urljoin = require('url-join');

const DeliveryProvider = require('../models/deliveryProvider');

const ENDPOINT_PATH = urljoin(process.env.HOST, 'deliveryProviders');

const { getMarketLocation } = require('../helpers/market');
const { responseToMongooseError } = require('../helpers/responses');

async function getAllDeliveryProviders (req, res) {
    const marketId = req.user.market;
    const offset = req.query.offset || 0;
    const limit = req.query.limit || 20;
    const coords = await getMarketLocation(marketId);
    DeliveryProvider.find({
            location: {
                $near: {
                    $geometry: {
                        type: 'Point',
                        coordinates: coords
                    },
                    $maxDistance: 5 * 1000  // 5km
                }
            },
            status: 'available',
            accountStatus: 'active'
        })
        .select('firstName lastName _id')
        .skip(offset)
        .limit(limit)
        .exec()
        .then(docs => {
            const response = {
                count: docs.length,
                deliveryProviders: docs.map(deliveryProvider => {
                    return {
                        firstName: deliveryProvider.firstName,
                        lastName: deliveryProvider.lastName,
                        _id: deliveryProvider._id
                    }
                }),
                request: {
                    type: 'GET',
                    url: ENDPOINT_PATH
                }
            }
            res.status(200).json(response);
        })
        .catch(err => {
            responseToMongooseError(res, err);
        }); 
}

module.exports = {
    getAllDeliveryProviders
}