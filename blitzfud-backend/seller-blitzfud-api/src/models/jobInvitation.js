const mongoose = require('mongoose');

const jobInvitationSchema = new mongoose.Schema({
    seller: {
        type: mongoose.Types.ObjectId,
        ref: 'Seller',
        required: true
    },
    market: {
        type: mongoose.Types.ObjectId,
        ref: 'Market',
        required: true
    },
    deliveryProvider: {
        type: mongoose.Types.ObjectId,
        ref: 'DeliveryProvider',
        required: true
    },
    status: {
        type: String,
        enum: [
                'proposed',
                'accepted',
                'denied',
                'expired',
                'cancelled'
              ],
        default: 'proposed'
    },
    proposal: {
        type: String,
        required: true
    }
}, { 
    timestamps: true 
});

module.exports = mongoose.model('JobInvitation', 
                                jobInvitationSchema,
                                'jobInvitations')