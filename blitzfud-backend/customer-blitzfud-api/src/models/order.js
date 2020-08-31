const mongoose = require('mongoose');

const locationSchema = require('./schemas/location');
const itemOrderSchema = require('./schemas/itemOrder');

const DeliveryProvider = require('./deliveryProvider');

const orderSchema = new mongoose.Schema({
    customer: {
        type: mongoose.Types.ObjectId,
        ref: 'Customer'
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

orderSchema.pre('save', function(next) {
    let total = 0;
    let totalQuantityOfProducts = 0;
    const items = this.items;
    for (item of items) {
        total += item.itemPrice;
        totalQuantityOfProducts += item.quantity;
    }
    if (this.deliveryMethod == 'delivery'){
        total += this.deliveryPrice;
    }
    this.totalAmount = total.toFixed(2);
    this.totalQuantityOfProducts = totalQuantityOfProducts;  
    next();
});

module.exports = mongoose.model('Order', orderSchema);