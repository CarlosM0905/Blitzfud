const { Router } = require('express');
const router = Router();

const ProductsController = require('../controllers/products');

const checkAuth = require('../middlewares/authentication');
const PaginationMiddleware = require('../middlewares/pagination');

router.get('/', checkAuth, PaginationMiddleware.formatQuery, 
                           ProductsController.getAllProducts);
router.post('/', checkAuth, ProductsController.createProduct);
router.get('/:productId', checkAuth, ProductsController.getProduct);
router.patch('/:productId', checkAuth, ProductsController.updateProduct);
router.delete('/:productId', checkAuth, ProductsController.deleteProduct);

module.exports = router;
