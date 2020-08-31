const { Router } = require('express');
const router = Router();

const SalesController = require('../controllers/sales');

const checkAuth = require('../middlewares/authentication');

router.get('/', checkAuth, SalesController.getAllSales);
router.get('/:saleId', checkAuth, 
                         SalesController.getSale);

module.exports = router;