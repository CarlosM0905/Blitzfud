const { Router } = require('express');
const router = Router();

const AuthenticationController = require('../controllers/authentication');

const CoordinatesMiddleware = require('../middlewares/coordinates');

router.post('/signin', AuthenticationController.signin);
router.post('/signup', CoordinatesMiddleware.checkCoordinates,
                       AuthenticationController.signup);

module.exports = router;