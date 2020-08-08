const urljoin = require('url-join');

const Market = require('../models/market');
const JobInvitation = require('../models/jobInvitation');
const DeliveryProvider = require('../models/deliveryProvider');

const ENDPOINT_PATH = urljoin(process.env.HOST, 'jobInvitations');

const { responseToMongooseError } = require('../helpers/responses');

function getAllJobInvitations (req, res) {
    const deliveryProviderId = req.user._id;
    let conditions = { 
        deliveryProvider: deliveryProviderId,
        status: { $in: ['proposed', 'accepted'] }
    }
    if (req.query.status){
        conditions['status'] = req.query.status;
    }
    JobInvitation.find(conditions)
        .populate('market', 'name location')
        .populate('seller', 'firstName lastName')
        .exec()
        .then(docs => {
            const response = {
                count: docs.length,
                jobInvitations: docs.map(jobInvitation => {
                    return {
                        market: {
                            name: jobInvitation.market.name,
                            _id: jobInvitation.market._id,
                        },
                        seller: {
                            firstName: jobInvitation.seller.firstName,
                            lastName: jobInvitation.seller.lastName,
                            _id: jobInvitation.seller._id,
                        },
                        status: jobInvitation.status,
                        proposal: jobInvitation.proposal,
                        createdAt: jobInvitation.createdAt,
                        _id: jobInvitation._id,
                    }
                }),
                request: {
                    type: 'POST',
                    url: ENDPOINT_PATH
                }
            }
            res.status(200).json(response);
        })
        .catch(err => {
            responseToMongooseError(res, err);
        })
}

async function addDeliveryProviderToMarket (res, deliveryProviderId, marketId) {
    return Market.updateOne({
            _id: marketId
        }, {
            $push: { deliveryProviders: deliveryProviderId }
        })
        .exec()
        .then(result => {
            return result;
        })
        .catch(err => {
            responseToMongooseError(res, err);
        })
}

async function addMarketToDeliveryProvider (res, deliveryProviderId, marketId) {
    return DeliveryProvider.updateOne({
            _id: deliveryProviderId
        }, {
            $push: { markets: marketId }
        })
        .exec()
        .then(result => {
            return result;
        })
        .catch(err => {
            responseToMongooseError(res, err);
        })
}

async function updateJobInvitation (req, res) {
    const deliveryProviderId = req.user._id;
    const jobInvitationId = req.params.jobInvitationId;
    const status = req.body.status;
    JobInvitation.findOneAndUpdate({
            _id: jobInvitationId,
            deliveryProvider: deliveryProviderId,
            status: 'proposed'
        }, {
            status: status
        })
        .then(async (doc) => {
            if (!doc) {
                return res.status(404).json({
                    message: 'No se encontró la invitación'
                });
            }
            if (status === 'accepted') {
                const marketId = doc.market;
                const resultMarket = await addDeliveryProviderToMarket(
                    res, deliveryProviderId, marketId
                );
                const resultDeliveryProvider = await addMarketToDeliveryProvider(
                    res, deliveryProviderId, marketId
                );
                if (resultMarket.nModified > 0 && resultDeliveryProvider.nModified > 0){
                    res.status(200).json({
                        message: 'Invitación actualizada con éxito'
                    });
                } else {
                    res.status(400).json({
                        message: 'Actualización no válida'
                    });
                }
            } else {
                res.status(200).json({
                    message: 'Invitación actualizada con éxito'
                });
            }
        })
        .catch(err => {
            responseToMongooseError(res, err);
        })
}

module.exports = {
    getAllJobInvitations,
    updateJobInvitation
}