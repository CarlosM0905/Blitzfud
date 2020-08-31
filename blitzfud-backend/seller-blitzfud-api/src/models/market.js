const mongoose = require('mongoose');

const locationSchema = require('./schemas/location');

const { STATUS, DELIVERY_METHODS } = require('../constants/market');

const marketSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true
    },
    description: {
        type: String
    },
    marketStatus: {
        type: String,
        enum: [
                STATUS.OPEN,
                STATUS.CLOSED
              ],
        default: STATUS.OPEN
    },
    deliveryMethods: {
        type: String,
        enum: [ 
                DELIVERY_METHODS.PICKUP, 
                DELIVERY_METHODS.DELIVERY, 
                DELIVERY_METHODS.BOTH
              ],
        default: DELIVERY_METHODS.PICKUP,
        required: true
    },
    deliveryPrice: {
        type: Number
    },
    
    deliveryProviders: [{
        type: mongoose.Types.ObjectId,
        ref: 'DeliveryProvider'
    }],
    location: {
        type: locationSchema,
        required: true
    }
}, { 
    toJSON: { 
        virtuals: true 
    } 
});

marketSchema.index({ 'location': '2dsphere' });

marketSchema.virtual('products', {
    ref: 'Product',
    localField: '_id',
    foreignField: 'market', 
});

module.exports = mongoose.model('Market', marketSchema);
