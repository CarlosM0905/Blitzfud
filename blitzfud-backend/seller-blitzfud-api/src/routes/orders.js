const { Router } = require('express');
const router = Router();

const OrdersController = require('../controllers/orders');

const checkAuth = require('../middlewares/authentication');

router.get('/', checkAuth, OrdersController.getAllOrders);
router.post('/:orderId', checkAuth, 
                         OrdersController.updateOrder);

module.exports = router;