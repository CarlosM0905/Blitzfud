const { Router } = require('express');
const router = Router();

const SearchController = require('../controllers/search');

const is2DCoordinate = require('../middlewares/coordinates');

router.get('/products', is2DCoordinate, SearchController.getProducts);

module.exports = router;