const path = require('path');
const multer = require('multer');
const { v4: uuidv4 } = require('uuid');

const { isValidExtension } = require('../helpers/images');

const VALID_MIMETYPE = ['image/png', 'image/jpg', 'image/gif', 'image/jpeg'];

const uploadImage = multer({
    storage: multer.diskStorage({
        destination: path.join(__dirname,'../../uploads'),
        filename: (req, file, cb) => {
            cb(null, uuidv4() + path.extname(file.originalname));
        }
    }),
    limits: {
        fileSize: 1024 * 1024 * 5
    },
    fileFilter: (req, file, cb) => {
        isValidExtension(file, VALID_MIMETYPE) ? cb(null, true) : cb(error);
    }
});

module.exports = {
    uploadImage
}