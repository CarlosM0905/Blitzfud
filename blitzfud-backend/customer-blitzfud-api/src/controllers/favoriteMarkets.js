const Customer = require('../models/customer');

function getAllMarkets (req, res) {
    const customerId = req.user._id;
    Customer.findById(customerId)
        .select('favoriteMarkets')
        .populate({
            path: 'favoriteMarkets',
            select: 'name  _id'
        })
        .exec()
        .then(doc => {
            if (!(doc.favoriteMarkets === undefined || doc.favoriteMarkets.length == 0)) {
                let favoriteMarkets = doc.favoriteMarkets
                const response = {
                    count: favoriteMarkets.length,
                    markets: favoriteMarkets.map(market => {
                        return {
                            name: market.name,
                            _id: market._id,
                        }
                    })
                }
                res.status(200).json(response);
            } else {
                res.status(200).json({
                    count: 0,
                    markets: []
                });
            }
        })
        .catch(err => {
            res.status(500).json({
                message: 'Error interno de servidor, reintente en unos minutos por favor',
                error: err
            });
        });
}

function addMarket (req, res) {
    let marketId = req.body.marketId;
    const customerId = req.user._id;
    let conditions = {
        _id: customerId,
        'favoriteMarkets': { $ne: marketId }
    };
    let update = {
        $push: { favoriteMarkets: marketId }
    }
    Customer.updateOne(conditions, update)
        .then(result => {
            if (result.nModified > 0) {
                res.status(201).json({
                    message: 'Tienda añadida a tus favoritas',
                });
            } else {
                res.status(409).json({
                    message: 'Tienda ya se encuentra en tus favoritas'
                });
            }
        })
        .catch(err => {
            res.status(500).json({
                message: 'Error interno de servidor, reintente en unos minutos por favor',
                error: err
            });
        });
}

function removeMarket (req, res) {
    let marketId = req.params.marketId;
    const customerId = req.user._id;
    Customer.updateOne({ _id: customerId }, { $pull: { 'favoriteMarkets': marketId } })
        .then(result => {
            if (result.nModified > 0){
                res.status(200).json({
                    message: 'Tienda removida de tus favoritas',
                });
            } else {
                res.status(404).json({
                    message: 'Tienda no se encuentra en tus favoritas'
                });                
            }

        })
        .catch(err => {
            res.status(500).json({
                message: 'Error interno de servidor, reintente en unos minutos por favor',
                error: err
            });
        })
}


module.exports = {
    getAllMarkets,
    addMarket,
    removeMarket
}