const { Router } = require('express');
const router = Router();

const PurchaseOrdersController = require('../controllers/purchaseOrders');

const OrdersMiddleware = require('../middlewares/orders');
const checkAuth = require('../middlewares/authentication');

router.get('/', checkAuth, 
                PurchaseOrdersController.getAllPurchaseOrders);
router.post('/', checkAuth,
                 OrdersMiddleware.isValidFormat, 
                 OrdersMiddleware.hasDuplicateProducts,
                 OrdersMiddleware.giveFormat, 
                 PurchaseOrdersController.createPurchaseOrder);

module.exports = router;