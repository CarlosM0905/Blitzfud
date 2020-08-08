const { Router } = require('express');
const router = Router();

const checkAuth = require('../middlewares/authentication');
const PaginationMiddleware = require('../middlewares/pagination');

const DeliveryProvidersController = require('../controllers/deliveryProviders');

router.get('/', checkAuth, 
                PaginationMiddleware.formatQuery, 
                DeliveryProvidersController.getAllDeliveryProviders);

module.exports = router;