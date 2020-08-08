const { Router } = require('express');
const router = Router();

const OrdersController = require('../controllers/orders');

const checkAuth = require('../middlewares/authentication');

router.get('/:orderId', checkAuth, OrdersController.getOrder);

module.exports = router;