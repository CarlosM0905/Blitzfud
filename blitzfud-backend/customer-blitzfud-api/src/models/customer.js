const mongoose = require('mongoose');

const pointSchema = require('./schemas/point');
const subCartSchema = require('./schemas/subCart');

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
                'active',
                'banned'
              ],
        default: 'active'
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
        type: pointSchema,
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

customerSchema.virtual('purchaseOrders', {
    ref: 'PurchaseOrder',
    localField: '_id',
    foreignField: 'customer', 
});

module.exports = mongoose.model('Customer', customerSchema);