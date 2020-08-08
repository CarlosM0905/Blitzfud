const urljoin = require('url-join');

const JobInvitation = require('../models/jobInvitation');

const ENDPOINT_PATH = urljoin(process.env.HOST, 'jobInvitations');

const { responseToMongooseError } = require('../helpers/responses');

function getAllJobInvitations (req, res) {
    const marketId = req.user.market;
    let conditions = { market: marketId }
    if (req.query.status){
        conditions['status'] = req.query.status;
    }
    JobInvitation.find(conditions)
        .populate('deliveryProvider', 'firstName lastName')
        .exec()
        .then(docs => {
            const response = {
                count: docs.length,
                jobInvitations: docs.map(jobInvitation => {
                    return {
                        deliveryProvider: {
                            firstName: jobInvitation.deliveryProvider.firstName,
                            lastName: jobInvitation.deliveryProvider.lastName,
                            _id: jobInvitation.deliveryProvider._id,
                        },
                        status: jobInvitation.status,
                        proposal: jobInvitation.proposal,
                        createdAt: jobInvitation.createdAt,
                        _id: jobInvitation._id,
                    }
                }),
                request: {
                    type: 'POST',
                    url: ENDPOINT_PATH,
                    body: {
                        deliveryProvider: 'ObjectId',
                        proposal: 'String'
                    }
                }
            }
            res.status(200).json(response);
        })
        .catch(err => {
            responseToMongooseError(res, err);
        })
}

function createJobInvitation (req, res) {
    const sellerId = req.user._id;
    const marketId = req.user.market;
    const proposal = req.body.proposal;
    const deliveryProvider = req.body.deliveryProvider;
    const jobInvitation = new JobInvitation ({
        seller: sellerId,
        market: marketId,
        deliveryProvider: deliveryProvider,
        proposal: proposal
    });
    jobInvitation.save()
        .then(result => {
            res.status(201).json({
                message: 'Propuesta enviada con éxito'
            });
        })
        .catch(err => {
            responseToMongooseError(res, err);
        })
}

function updateJobInvitation (req, res) {
    const marketId = req.user.market;
    const jobInvitationId = req.params.jobInvitationId;
    const status = req.body.status;
    JobInvitation.updateOne({
            _id: jobInvitationId,
            market: marketId,
            status: 'proposed'
        }, {
            status: status
        })
        .then(result => {
            if (result.n === 0) {
                return res.status(404).json({
                    message: 'No se encontró la invitación'
                });
            } 
            if (result.nModified > 0) {
                res.status(200).json({
                    message: 'Invitación actualizada'
                });
            } else {
                res.status(400).json({
                    message: 'Actualización no válida'
                });
            }
        })
        .catch(err => {
            responseToMongooseError(res, err);
        })
}

module.exports = {
    getAllJobInvitations,
    createJobInvitation,
    updateJobInvitation
}