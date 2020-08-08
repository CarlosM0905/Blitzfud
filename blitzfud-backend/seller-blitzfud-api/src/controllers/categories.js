const Category = require('../models/category');

function getAllCategories (req, res) {
    Category.find()
        .select('name description _id')
        .exec()
        .then(docs => {
            const response = {
                count: docs.length,
                categories: docs
            }
            res.status(200).json(response);
        })
        .catch(err => {
            res.status(500).json({
                message: 'Error interno de servidor, reintente en unos minutos por favor',
                error: err
            });
        });
    }

module.exports = {
    getAllCategories
}