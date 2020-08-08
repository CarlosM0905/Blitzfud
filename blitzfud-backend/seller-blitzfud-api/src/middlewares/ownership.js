const Seller = require('../models/seller');

const { responseToMongooseError } = require('../helpers/responses');

function ownsMarket (req, res, next) {
    const sellerId = req.user._id;
    Seller.findById(sellerId)
        .select('market')
        .exec()
        .then(doc => {
            if (doc.market === undefined) {
                return next();
            } else {
                return res.status(400).json({
                    message: 'Ya existe tienda asociada a la cuenta'
                });
            } 
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

module.exports = {
    ownsMarket
};