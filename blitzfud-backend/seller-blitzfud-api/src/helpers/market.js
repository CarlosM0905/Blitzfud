const Market = require('../models/market');

async function getMarketLocation (marketId) {
    return Market.findById(marketId)
                 .select('location')
                 .exec()
                 .then(doc => {
                     return doc.location.coordinates;
                 })
                 .catch(err => {
                     responseToMongooseError(res, err);
                 });    
}

async function getDeliveryWorkers (marketId) {
    return Market.findById(marketId)
                 .select('deliveryProviders')
                 .exec()
                 .then(doc => {
                     return doc.deliveryProviders || [];
                 })
                 .catch(err => {
                     responseToMongooseError(res, err);
                 }); 
}

module.exports = {
    getMarketLocation,
    getDeliveryWorkers
}