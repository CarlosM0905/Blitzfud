const { Router } = require('express');
const router = Router();

const PurchasesController = require('../controllers/purchases');

const checkAuth = require('../middlewares/authentication');

router.get('/', checkAuth, 
                PurchasesController.getAllPurchases);

module.exports = router;