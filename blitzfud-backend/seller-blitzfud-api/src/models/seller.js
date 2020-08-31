const mongoose = require('mongoose');

const { STATUS } = require('../constants/account');

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
                STATUS.ACTIVE,
                STATUS.BANNED
              ],
        default: STATUS.ACTIVE
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