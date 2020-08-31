const { Router } = require('express');
const router = Router();

const SearchController = require('../controllers/search');

const { checkCoordinatesInQuery } = require('../middlewares/coordinates');

router.get('/products', checkCoordinatesInQuery, SearchController.getProducts);
router.get('/market/:marketId/products', SearchController.getProductsOfMarket);

module.exports = router;