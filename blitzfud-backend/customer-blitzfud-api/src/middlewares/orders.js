const { Types } = require('mongoose');

const Product = require('../models/product');

const Responses = require('../helpers/responses');

function isValidFormat (req, res, next) {
    const items = req.body;
    if (items.constructor === Array) {
        next();
    } else {
        Responses.responseToInvalidFormat(res);
    }
}

function hasDuplicateProducts (req, res, next) {
    const subcarts = req.body;
    const originalLength = subcarts
                                .map(subcart => subcart.items)
                                .reduce((group1, group2) => group1.concat(group2))
                                .map(item => item.product)
                                .length;
    const filteredLength = subcarts
                                .map(subcart => subcart.items)
                                .reduce((group1, group2) => group1.concat(group2))
                                .map(item => item.product)
                                .filter((v, i, a) => a.indexOf(v) === i)
                                .length;
    if (originalLength == filteredLength){
        next();
    } else {
        res.status(400).json({
            message: 'Solo están permitidos productos únicos en el arreglo'
        });
    }
}

function giveFormat (req, res, next) {
    let subcarts = req.body;
    const customerId = req.user._id;
    const productsId = subcarts.map(subcart => Types.ObjectId(subcart.items[0].product));
    Product.aggregate([
            { $match: 
                { _id: { $in: productsId }, status: 'available' }
            },
            { $addFields: 
                { '__order': { $indexOfArray: [productsId, '$_id'] } }
            },
            { $sort: 
                { '__order': 1 }
            },
            { $lookup: 
                { from: 'markets', localField: 'market', foreignField: '_id', as: 'market' } 
            },
            { $project: { 
                  'market._id': 1,
                  'market.marketStatus': 1, 
                  'market.deliveryMethods': 1, 
                  'market.deliveryPrice': 1 
                } 
            } 
        ])
        .exec()
        .then(docs => {
            if (docs.length != productsId.length){
                return res.status(400).json({
                    message: 'Producto en orden a solicitar no existe'
                });
            }
            for (let i = 0; i < docs.length; i++){
                subcarts[i]['customer'] = customerId;
                subcarts[i]['market'] = docs[i].market[0]._id;

                let selectedMethod = subcarts[i].deliveryMethod;
                let availableMethods = docs[i].market[0].deliveryMethods;
                
                let marketId = docs[i].market[0]._id;
                let marketStatus = docs[i].market[0].marketStatus;

                if (marketStatus === 'closed'){
                    return res.status(400).json({
                        message: `La tienda con id ${marketId} se encuentra cerrada`
                    });
                }
                if (selectedMethod == 'delivery' && 
                    (['delivery', 'both'].includes(availableMethods))){
                    if (!subcarts[i]['deliveryPoint']) {
                        return Responses.responseToInvalidFormat(res);
                    }
                    subcarts[i]['deliveryPrice'] = docs[i].market[0].deliveryPrice;
                } else if (selectedMethod == 'pickup' && 
                    (['pickup', 'both'].includes(availableMethods))) {
                    delete subcarts[i]['deliveryPoint'];
                } else {
                    return res.status(400).json({
                        message: 'La tienda no cuenta con el servicio de entrega indicado'
                    });
                }
            }
            req.body = subcarts;
            next();
        })
        .catch(err => {
            Responses.responseToMongooseError(res, err);
        });
}

module.exports = {
    isValidFormat,
    hasDuplicateProducts,
    giveFormat
};