const mongoose = require('mongoose');

const Customer = require('./customer');
const DeliveryProvider = require('./deliveryProvider');

const locationSchema = require('./schemas/location');
const itemOrderSchema = require('./schemas/itemOrder');

const { STATUS } = require('../constants/order');
const { DELIVERY_METHODS } = require('../constants/market');

const orderSchema = new mongoose.Schema({
    customer: {
        type: mongoose.Types.ObjectId,
        ref: Customer
    },
    market: {
        type: mongoose.Types.ObjectId,
        ref: 'Market'
    },
    deliveryProvider: {
        type: mongoose.Types.ObjectId,
        ref: DeliveryProvider
    },
    items: [{
        type: itemOrderSchema
    }],
    deliveryPoint: {
        type: locationSchema,
    },

    totalAmount: {
        type: Number
    },
    totalQuantityOfProducts: {
        type: Number
    },
    deliveryMethod: {
        type: String,
        enum: [ 
                DELIVERY_METHODS.PICKUP, 
                DELIVERY_METHODS.DELIVERY
              ],
        required: true
    },
    deliveryPrice:{
        type: Number
    },
    status: {
        type: String,
        enum: [ 
                STATUS.PREPROCESSING,
                STATUS.IN_PROGRESS,
                STATUS.DENIED,
                STATUS.SUCCESS,
                STATUS.CANCELLED
              ],
        default: STATUS.PREPROCESSING
    }
}, {
    timestamps: true 
});

module.exports = mongoose.model('Order', orderSchema);