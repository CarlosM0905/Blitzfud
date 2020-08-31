const mongoose = require('mongoose');

const locationSchema = require('./schemas/location');

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
                'open',
                'closed'
              ],
        default: 'open'
    },
    deliveryMethods: {
        type: String,
        enum: [ 
                'pickup', 
                'delivery',
                'both'
              ],
        default: 'pickup',
        required: true
    },
    deliveryPrice: {
        type: Number
    },
    
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
