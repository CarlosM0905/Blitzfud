const urljoin = require('url-join');

const Seller = require('../models/seller');

const { STATUS } = require('../constants/account');
const ENDPOINT_PATH = urljoin(process.env.HOST, 'seller');

const { responseToMongooseError } = require('../helpers/responses');

function getSeller (req, res) {
    const sellerId = req.user._id;
    Seller.findById(sellerId)
        .select('firstName lastName')
        .exec()
        .then(doc => {
            const response = {
                firstName: doc.firstName,
                lastName: doc.lastName,
                request: {
                    type: 'PATCH',
                    body: [{
                        propName: 'String',
                        value: 'String'
                    }],
                    url: ENDPOINT_PATH
                }
            }
            res.status(200).json(response);
        })
        .catch(err => {
            responseToMongooseError(res, err);
        })
}

function updateSeller (req, res) {
    let body = req.body;
    const sellerId = req.user._id;
    const updateOps = {};
    for (const ops of body) {
        updateOps[ops.propName] = ops.value;
    }
    Seller.updateOne({ _id: sellerId, accountStatus: STATUS.ACTIVE }, { $set: updateOps })
        .exec()
        .then(result => {
            if (result.n === 0) {
                res.status(404).json({
                    message: 'No se encontró al vendedor',
                    request: {
                        type: 'GET',
                        url: ENDPOINT_PATH
                    }
                });
            } else {
                res.status(200).json({
                    message: 'Perfil de vendedor actualizado',
                    request: {
                        type: 'GET',
                        url: ENDPOINT_PATH
                    }
                });
            }
        })        
        .catch(err => {
            responseToMongooseError(res, err);
        });    
}

module.exports = {
    getSeller,
    updateSeller
}