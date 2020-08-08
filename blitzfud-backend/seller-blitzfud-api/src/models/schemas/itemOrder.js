const mongoose = require('mongoose');

const itemOrderSchema = new mongoose.Schema({
    name: {
        type: String,
    },
    price: {
        type: Number,
    },
    quantity: {
        type: Number,
        required: true
    },
    itemPrice: {
        type: Number
    },    
    product: {
        type: mongoose.Types.ObjectId,
        ref: 'Product'
    }
});

module.exports = itemOrderSchema;