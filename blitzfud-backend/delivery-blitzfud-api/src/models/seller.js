const mongoose = require('mongoose');

const sellerSchema = new mongoose.Schema({
    email: { 
        type: String, 
        required: true, 
        unique: true 
    },
    password: { 
        type: String, 
        required: true 
    },
    accountStatus: {
        type: String,
        enum: [
                'active',
                'banned'
              ],
        default: 'active'
    },
    firstName: {
        type: String,
        required: true
    },
    lastName: {
        type: String, 
        required: true
    },

    market: {
        type: mongoose.Types.ObjectId,
        ref: 'Market'
    }
});

module.exports = mongoose.model('Seller', sellerSchema);