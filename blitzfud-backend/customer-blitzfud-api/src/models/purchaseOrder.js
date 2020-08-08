const mongoose = require('mongoose');

const Order = require('./order');

const purchaseOrderSchema = new mongoose.Schema({
    totalAmount: {
        type: Number
    },
    payedAmount: {
        type: Number
    },
    createdAt: {
        type: Date,
        default: Date.now()
    },

    customer: {
        type: mongoose.Types.ObjectId,
        ref: 'Customer'
    },
    orders: [{
        type: mongoose.Types.ObjectId,
        ref: 'Order'
    }]
});

purchaseOrderSchema.pre('save', function(next) {
    Order.find({
            _id: { $in: this.orders }
        })
        .select('totalAmount')
        .exec()
        .then(docs => {
            const total = docs.map(doc => doc.totalAmount)
                              .reduce((v1, v2) => v1 + v2);
            this.totalAmount = total;
            next();
        })
});

module.exports = mongoose.model('PurchaseOrder', 
                                purchaseOrderSchema,
                                'purchaseOrders');