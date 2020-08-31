const { Router } = require('express');
const router = Router();

const ProductsController = require('../controllers/products');

const PaginationMiddleware = require('../middlewares/pagination');

router.get('/:marketId/products', PaginationMiddleware.formatQuery, 
                                  ProductsController.getAllProducts);
router.get('/:marketId/products/:productId', ProductsController.getProduct);

module.exports = router;
