const mongoose = require('mongoose');

const locationSchema = require('./schemas/location');
const subCartSchema = require('./schemas/subCart');

const { STATUS } = require('../constants/account');

const customerSchema = new mongoose.Schema({
    phoneNumber: { 
        type: String, 
        required: true, 
        unique: true 
    },
    email: { 
        type: String, 
    },
    password: { 
        type: String, 
        required: true 
    },
    accountStatus: {
        type: String,
        enum: [
                STATUS.ACTIVE,
                STATUS.BANNED
              ],
        default: STATUS.ACTIVE
    },
    firstName: {
        type: String,
        required: true
    },
    lastName: {
        type: String, 
        required: true
    },
    
    location: {
        type: locationSchema,
    },
    shoppingCart: [{
        type: subCartSchema
    }], 
    favoriteMarkets: [{
        type: mongoose.Types.ObjectId,
        ref: 'Market'
    }]
}, { 
    toJSON: { 
        virtuals: true 
    } 
});

customerSchema.virtual('orders', {
    ref: 'Order',
    localField: '_id',
    foreignField: 'customer', 
});

module.exports = mongoose.model('Customer', customerSchema);