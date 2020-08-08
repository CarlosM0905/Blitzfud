const { Router } = require('express');
const router = Router();

const MarketsController = require('../controllers/markets');

const is2DCoordinate = require('../middlewares/coordinates');

router.get('/', is2DCoordinate, MarketsController.getAllMarkets);
router.get('/:marketId', MarketsController.getMarket);

module.exports = router;