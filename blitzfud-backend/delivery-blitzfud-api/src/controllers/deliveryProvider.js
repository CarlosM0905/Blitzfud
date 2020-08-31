const urljoin = require('url-join');

const DeliveryProvider = require('../models/deliveryProvider');

const ENDPOINT_PATH = urljoin(process.env.HOST, 'deliveryProvider');

const { responseToMongooseError } = require('../helpers/responses');

function getDeliveryProvider (req, res) {
    const deliveryProviderId = req.user._id;
    DeliveryProvider.findById(deliveryProviderId)
        .select('nationalIdentityNumber phoneNumber accountStatus status \
                 firstName lastName profilePhotoURL location')
        .then(deliveryProvider => {
            const response = {
                nationalIdentityNumber: deliveryProvider.nationalIdentityNumber,
                phoneNumber: deliveryProvider.phoneNumber,
                accountStatus: deliveryProvider.accountStatus,
                status: deliveryProvider.status,
                firstName: deliveryProvider.firstName,
                lastName: deliveryProvider.lastName,
                profilePhotoURL: deliveryProvider.profilePhotoURL,
                location: deliveryProvider.location,
                _id: deliveryProvider._id,
                request: {
                    type: 'GET',
                    body: ENDPOINT_PATH
                }
            }
            res.status(200).json(response);
        })
        .catch(err => {
            responseToMongooseError(res, err);
        })
}

module.exports = {
    getDeliveryProvider
}
