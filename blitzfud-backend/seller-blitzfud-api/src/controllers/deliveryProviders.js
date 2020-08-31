const urljoin = require('url-join');

const DeliveryProvider = require('../models/deliveryProvider');

const ACCOUNT_CONSTANTS = require('../constants/account');
const JOB_INVITATION_CONSTANTS = require('../constants/jobInvitation');
const DELIVERY_PROVIDER_CONSTANTS = require('../constants/deliveryProvider');

const ENDPOINT_PATH = urljoin(process.env.HOST, 'deliveryProviders');
const JOB_INVITATION_PATH = urljoin(process.env.HOST, 'jobInvitations');

const { responseToMongooseError } = require('../helpers/responses');
const { getMarketLocation, getDeliveryWorkers } = require('../helpers/market');

function checkJobInvitation (deliveryProvider) {
    const jobInvitation = deliveryProvider.jobInvitation;
    if (jobInvitation.length > 0) {
        jobInvitation[0].request = {
            type: 'GET',
            url: urljoin(JOB_INVITATION_PATH, jobInvitation[0]._id.toString())
        }
        return jobInvitation[0];
    } else {
        return { status: JOB_INVITATION_CONSTANTS.STATUS.NOT_INVITED }
    }
}

async function getAllDeliveryProviders (req, res) { 
    const marketId = req.user.market;
    
    const offset = req.query.offset || 0;
    const limit = req.query.limit || 20;

    const coords = await getMarketLocation(marketId);
    const myDeliveryWorkers = await getDeliveryWorkers(marketId);

    DeliveryProvider.aggregate([
            {
                $geoNear: {
                    near: {
                        type: 'Point',
                        coordinates: coords
                    },
                    spherical: true,
                    maxDistance: 5 * 1000 , // 5km
                    distanceField: 'distance'
                }
            }, 
            {
                $match: {
                    _id: { $nin: myDeliveryWorkers },
                    status: DELIVERY_PROVIDER_CONSTANTS.STATUS.AVAILABLE,
                    accountStatus: ACCOUNT_CONSTANTS.STATUS.ACTIVE
                }
            }, 
            {
                $lookup: {
                    from: 'jobInvitations',
                    localField: '_id',
                    foreignField: 'deliveryProvider',
                    as: "jobInvitation"
                }
            },
            {
                $skip: offset
            }, 
            {
                $limit: limit
            }, 
            {
                $project: {
                    'firstName': 1,
                    'lastName': 1,
                    'profilePhotoURL': 1,
                    'distance': 1,
                    'jobInvitation._id': 1,
                    'jobInvitation.status': 1
                }
            }
        ])
        .exec()
        .then(doc => {
            const response = {
                count: doc.length,
                deliveryProviders: doc.map(deliveryProvider => {
                    return {
                        firstName: deliveryProvider.firstName,
                        lastName: deliveryProvider.lastName,
                        profilePhotoURL: deliveryProvider.profilePhotoURL,
                        jobInvitation: checkJobInvitation(deliveryProvider),
                        distance: deliveryProvider.distance, 
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