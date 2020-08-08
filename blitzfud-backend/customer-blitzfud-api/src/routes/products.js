const { Router } = require('express');
const router = Router();

const ProductsController = require('../controllers/products');

router.get('/:marketId/products', ProductsController.getAllProducts);
router.get('/:marketId/products/:productId', ProductsController.getProduct);

module.exports = router;
