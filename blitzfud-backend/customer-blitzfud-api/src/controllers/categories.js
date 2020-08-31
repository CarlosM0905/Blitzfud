const Category = require('../models/category');

const { responseToMongooseError } = require('../helpers/responses');

function getAllCategories (req, res) {
    Category.find()
        .select('name description _id')
        .exec()
        .then(docs => {
            const response = {
                count: docs.length,
                categories: docs.map(doc => {
                    return {
                        name: doc.name,
                        description: doc.description,
                        _id: doc._id,
                    }
                })
            }
            res.status(200).json(response);
        })
        .catch(err => {
            responseToMongooseError(res, err);
        })
    }

module.exports = {
    getAllCategories
}