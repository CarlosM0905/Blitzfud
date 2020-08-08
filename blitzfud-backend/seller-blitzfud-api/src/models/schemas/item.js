const mongoose = require('mongoose');

const itemSchema = new mongoose.Schema({
    product: {
        type: mongoose.Types.ObjectId,
        ref: 'Product'
    },
    quantity: {
        type: Number
    }   
});

module.exports = itemSchema;