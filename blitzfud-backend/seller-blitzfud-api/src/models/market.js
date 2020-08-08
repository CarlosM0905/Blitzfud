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
                'in-store', 
                'delivery',
                'both'
              ],
        default: 'in-store',
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
