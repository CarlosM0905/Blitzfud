const mongoose = require('mongoose');

const pointSchema = require('./schemas/point');

const marketSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true
    },
    description: {
        type: String
    },
    deliveryMethods: {
        type: String,
        enum: [ 
                'pickup', 
                'delivery',
                'both'
              ],
        required: true
    },
    deliveryPrice: {
        type: Number
    },
    
    location: {
        type: pointSchema,
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
