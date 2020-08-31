const mongoose = require('mongoose');

const locationSchema = require('./schemas/location');

const deliveryProviderSchema = new mongoose.Schema({
    nationalIdentityNumber: {
        type: String,
        required: true,
        unique: true
    },
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
    profilePhotoURL: {
        type: String
    },
    profilePhotoId: {
        type: String
    },

    markets: [{
        type: mongoose.Types.ObjectId,
        ref: 'Market'
    }],
    location: {
        type: locationSchema,
        required: true
    }
});

deliveryProviderSchema.index({ 'location': '2dsphere' });

module.exports = mongoose.model('DeliveryProvider', 
                                deliveryProviderSchema, 
                                'deliveryProviders');