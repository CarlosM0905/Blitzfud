const mongoose = require('mongoose');

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

module.exports = mongoose.model('PurchaseOrder', 
                                purchaseOrderSchema,
                                'purchaseOrders');