const mongoose = require('mongoose');

const pointSchema = require('./schemas/point');

const deliveryProviderSchema = new mongoose.Schema({
    phoneNumber: {
        type: String,
        required: true,
        unique: true
    },
    password: {
        type: String,
        required: true
    },
    accountStatus: {
        type: String,
        enum: [
                'active',
                'banned'
              ],
        default: 'active'
    },
    status: {
        type: String,
        enum: [
                'available',
                'busy'
              ],
        default: 'available'
    },
    firstName: {
        type: String,
        required: true
    },
    lastName: {
        type: String,
        required: true
    },

    markets: [{
        type: mongoose.Types.ObjectId,
        ref: 'Market'
    }],
    location: {
        type: pointSchema,
        required: true
    }
});

deliveryProviderSchema.index({ 'location': '2dsphere' });

module.exports = mongoose.model('DeliveryProvider', 
                                deliveryProviderSchema, 
                                'deliveryProviders');