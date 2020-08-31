const { Router } = require('express');
const router = Router();

const AuthenticationController = require('../controllers/authentication');

router.post('/signin', AuthenticationController.signin);
router.post('/signup', AuthenticationController.signup); 

module.exports = router;
