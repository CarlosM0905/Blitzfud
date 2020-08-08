const mongoose = require('mongoose');
const itemSchema = require('./item');

const subCartSchema = new mongoose.Schema({
    market: {
        type: mongoose.Types.ObjectId,
        ref: 'Market'
    },
    items: [{
        type: itemSchema
    }]
});

module.exports = subCartSchema;