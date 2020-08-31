const urljoin = require('url-join');

const Market = require('../models/market');

const ENDPOINT_PATH = urljoin(process.env.HOST, 'deliveryWorkers');

const { responseToMongooseError } = require('../helpers/responses');

function getAllDeliveryWorkers (req, res) {
    const marketId = req.user.market;
    Market.findById(marketId)
        .select('deliveryProviders')
        .populate('deliveryProviders', 'firstName lastName phoneNumber status \
                   profilePhotoURL')
        .exec()
        .then(docs => {
            const deliveryWorkers = docs.deliveryProviders;
            if (!deliveryWorkers){
                return res.status(200).json({
                    count: 0,
                    deliveryWorkers: [],
                    request: {
                        type: 'GET',
                        url: ENDPOINT_PATH
                    }
                })    
            }
            const response = {
                count: deliveryWorkers.length,
                deliveryWorkers: deliveryWorkers.map(deliveryProvider => {
                    return {
                        firstName: deliveryProvider.firstName,
                        lastName: deliveryProvider.lastName,
                        phoneNumber: deliveryProvider.phoneNumber,
                        status: deliveryProvider.status,
                        profilePhotoURL: deliveryProvider.profilePhotoURL,
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
    getAllDeliveryWorkers
}