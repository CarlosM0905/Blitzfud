const JobInvitation = require('../models/jobInvitation');

function hasBeenInvited (req, res, next) {
    const marketId = req.user.market;
    const deliveryProvider = req.body.deliveryProvider;
    JobInvitation.findOne({
            market: marketId,
            deliveryProvider: deliveryProvider
        })
        .exec()
        .then(doc => {
            if (doc) {
                return res.status(400).json({
                    message: 'Invitación ya ha sido enviada'
                })
            } else {
                next();
            }
        })
        .catch(err => {
            responseToMongooseError(res, err);
        })
}

module.exports = {
    hasBeenInvited
}