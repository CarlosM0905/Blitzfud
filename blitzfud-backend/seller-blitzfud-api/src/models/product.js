const mongoose = require('mongoose');

const PRODUCT_CONSTANTS = require('../constants/product');

const productSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true
    },
    nameLowercase: {
        type: String
    },
    unitOfMeasurement: {
        type: String,
        enum: PRODUCT_CONSTANTS.UNITS_OF_MEASUREMENT
    },
    content: {
        type: Number
    },
    maxQuantityPerOrder: {
        type: Number,
        min: 1,
        max: PRODUCT_CONSTANTS.MAX_QUANTITY_PER_ORDER,
        default: PRODUCT_CONSTANTS.MAX_QUANTITY_PER_ORDER
    },
    price: {
        type: Number,
        required: true
    },
    highlight: {
        type: Boolean,
        default: false
    },
    status: {
        type: String,
        enum: [
                PRODUCT_CONSTANTS.STATUS.AVAILABLE,
                PRODUCT_CONSTANTS.STATUS.OUT_OF_STOCK, 
                PRODUCT_CONSTANTS.STATUS.DELETED
              ],
        default: PRODUCT_CONSTANTS.STATUS.AVAILABLE
    },
    
    market: {
        type: mongoose.Types.ObjectId,
        ref: 'Market',
        required: true       
    },
    category: {
        type: mongoose.Types.ObjectId,
        ref: 'Category',
        required: true
    }
});

productSchema.pre('save', function(next) {
    this.nameLowercase = this.name.toLowerCase();
    next();
});

module.exports = mongoose.model('Product', productSchema);