const mongoose = require('mongoose');

const Customer = require('./customer');
const DeliveryProvider = require('./deliveryProvider');

const pointSchema = require('./schemas/point');
const itemOrderSchema = require('./schemas/itemOrder');

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
        type: pointSchema,
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
                'pickup', 
                'delivery'
              ],
        required: true
    },
    deliveryPrice:{
        type: Number
    },
    status: {
        type: String,
        enum: [ 
                'preprocessing', 
                'in-progress',
                'denied', 
                'success',
                'cancelled'
              ],
        default: 'preprocessing'
    }
}, {
    timestamps: true 
});

module.exports = mongoose.model('Order', orderSchema);