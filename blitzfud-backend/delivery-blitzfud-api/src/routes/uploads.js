const { Router } = require('express');
const router = Router();

const UploadsController = require('../controllers/uploads');

const checkAuth = require('../middlewares/authentication');
const ImagesMiddleware = require('../middlewares/images');

router.post('/profilePhoto', checkAuth, 
                             ImagesMiddleware.uploadImage.single('image'),                       
                             UploadsController.uploadProfilePhoto);

module.exports = router;