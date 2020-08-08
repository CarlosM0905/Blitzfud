const mongoose = require('mongoose');

const Product = require('../product');

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

itemOrderSchema.pre('save', function(next) {
    Product.findById(this.product)
        .select('name price')
        .exec()
        .then(doc => {
            this.name = doc.name;
            this.price = doc.price;
            this.itemPrice = (this.price * this.quantity).toFixed(2);
            next();
        });
})

module.exports = itemOrderSchema;