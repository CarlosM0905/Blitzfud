const constants = {
    STATUS: {
        AVAILABLE: 'available',
        OUT_OF_STOCK: 'out-of-stock',
        DELETED: 'deleted'
    },
    UNITS_OF_MEASUREMENT: [
        'mg', 'g', 'kg', 'ml', 'l', 'un'
    ],
    MAX_QUANTITY_PER_ORDER: 100
}

module.exports = Object.freeze(constants);