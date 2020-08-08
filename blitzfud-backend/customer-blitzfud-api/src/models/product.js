const mongoose = require('mongoose');

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
        enum: ['mg', 'g', 'kg', 'ml', 'l', 'un']
    },
    content: {
        type: Number
    },
    maxQuantityPerOrder: {
        type: Number,
        min: 1,
        max: 100,
        default: 100
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
                'available',
                'out-of-stock', 
                'deleted'
              ],
        default: 'available'
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