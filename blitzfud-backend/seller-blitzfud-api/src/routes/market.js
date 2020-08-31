const { Router } = require('express');
const router = Router();

const MarketController = require('../controllers/market');

const checkAuth = require('../middlewares/authentication');
const DeliveryMiddleware = require('../middlewares/delivery');
const OwnershipMiddleware = require('../middlewares/ownership');
const CoordinatesMiddleware = require('../middlewares/coordinates');

router.get('/', checkAuth, MarketController.getMyMarket);
router.post('/', checkAuth, OwnershipMiddleware.ownsMarket, 
                            CoordinatesMiddleware.checkCoordinates,
                            DeliveryMiddleware.onCreationHasDelivery, 
                            MarketController.createMarket);
router.patch('/', checkAuth, DeliveryMiddleware.onUpdateHasDelivery,
                             MarketController.updateMarket)

module.exports = router;