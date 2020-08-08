const { Router } = require('express');
const router = Router();

const FavoriteMarketsController = require('../controllers/favoriteMarkets');

const checkAuth = require('../middlewares/authentication');

router.get('/', checkAuth, FavoriteMarketsController.getAllMarkets);
router.post('/', checkAuth, FavoriteMarketsController.addMarket);
router.delete('/:marketId', checkAuth, FavoriteMarketsController.removeMarket);

module.exports = router;