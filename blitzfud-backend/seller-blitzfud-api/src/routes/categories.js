const { Router } = require('express');
const router = Router();

const CategoriesController = require('../controllers/categories');

router.get('/', CategoriesController.getAllCategories);

module.exports = router;