const { Router } = require('express');
const router = Router();

const checkAuth = require('../middlewares/authentication');

const DeliveryWorkersController = require('../controllers/deliveryWorkers');

router.get('/', checkAuth, 
                DeliveryWorkersController.getAllDeliveryWorkers);

module.exports = router;