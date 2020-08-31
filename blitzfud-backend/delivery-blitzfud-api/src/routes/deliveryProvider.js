const { Router } = require('express');
const router = Router();

const DeliveryProviderController = require('../controllers/deliveryProvider');

const checkAuth = require('../middlewares/authentication');

router.get('/', checkAuth, DeliveryProviderController.getDeliveryProvider);

module.exports = router;