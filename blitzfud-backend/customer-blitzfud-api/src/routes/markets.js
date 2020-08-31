const { Router } = require('express');
const router = Router();

const MarketsController = require('../controllers/markets');

const { checkCoordinatesInQuery } = require('../middlewares/coordinates');

router.get('/', checkCoordinatesInQuery, MarketsController.getAllMarkets);
router.get('/:marketId', MarketsController.getMarket);

module.exports = router;