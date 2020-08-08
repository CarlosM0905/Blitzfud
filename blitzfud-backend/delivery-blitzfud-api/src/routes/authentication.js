const { Router } = require('express');
const router = Router();

const AuthenticationController = require('../controllers/authentication');

const CoordinatesMiddleware = require('../middlewares/coordinates');

router.post('/signin', AuthenticationController.signin);
router.post('/signup', CoordinatesMiddleware.checkCoordinates,
                       CoordinatesMiddleware.formatCoordinates,
                       AuthenticationController.signup);

module.exports = router;