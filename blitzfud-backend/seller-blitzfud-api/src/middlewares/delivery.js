const { responseToInvalidFormat } = require('../helpers/responses');

function onCreationHasDelivery (req, res, next) {
    console.log(req.body)
    let body = req.body;
    if (body['deliveryMethods'] === 'delivery' ||
        body['deliveryMethods'] === 'both') {
        if (body['deliveryPrice']){
            next();
        } else {
            responseToInvalidFormat(res);
        }
    } else {
        next();
    }
}

function onUpdateHasDelivery (req, res, next) {
    let body = req.body;
    const updateOps = {};
    for (const ops of body) {
        updateOps[ops.propName] = ops.value;
    }
    if (updateOps['deliveryMethods'] === 'delivery' ||
        updateOps['deliveryMethods'] === 'both') {
        if (updateOps['deliveryPrice']){
            next();
        } else {
            responseToInvalidFormat(res);
        }
    } else {
        next();
    }
}

module.exports = {
    onCreationHasDelivery,
    onUpdateHasDelivery
}