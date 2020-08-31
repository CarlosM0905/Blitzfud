const { Router } = require('express');
const router = Router();

const CartController = require('../controllers/cart');

const checkAuth = require('../middlewares/authentication');

router.get('/', checkAuth, CartController.getAllItems);
router.post('/', checkAuth, CartController.addItem);
router.patch('/:productId', checkAuth, CartController.updateItem);
router.delete('/', checkAuth, CartController.emptyCart);
router.delete('/:productId', checkAuth, CartController.removeItem);
router.delete('/market/:marketId', checkAuth, CartController.removeMarket)

module.exports = router;