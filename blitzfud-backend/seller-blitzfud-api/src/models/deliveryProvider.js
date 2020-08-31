const mongoose = require('mongoose');

const locationSchema = require('./schemas/location');

const ACCOUNT_CONSTANTS = require('../constants/account');
const DELIVERY_PROVIDER_CONSTANTS = require('../constants/deliveryProvider');

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
                ACCOUNT_CONSTANTS.STATUS.ACTIVE,
                ACCOUNT_CONSTANTS.STATUS.BANNED
              ],
        default: ACCOUNT_CONSTANTS.STATUS.ACTIVE
    },
    status: {
        type: String,
        enum: [
                DELIVERY_PROVIDER_CONSTANTS.STATUS.AVAILABLE,
                DELIVERY_PROVIDER_CONSTANTS.STATUS.BUSY
              ],
        default: DELIVERY_PROVIDER_CONSTANTS.STATUS.AVAILABLE
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