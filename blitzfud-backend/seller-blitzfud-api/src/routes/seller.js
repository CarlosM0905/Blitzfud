const { Router } = require('express');
const router = Router();

const SellerController = require('../controllers/seller');

const checkAuth = require('../middlewares/authentication');

router.get('/', checkAuth, SellerController.getSeller);
router.patch('/', checkAuth, SellerController.updateSeller);

module.exports = router;
