const fs = require('fs-extra');
const cloudinary = require('cloudinary').v2;

const DeliveryProvider = require('../models/deliveryProvider');

const { responseToMongooseError } = require('../helpers/responses');

async function uploadProfilePhoto (req, res) {
    let cloudinaryResult;
    const deliveryProviderId = req.user._id;
    try {
        cloudinaryResult = await cloudinary.uploader.upload(req.file.path);
        await fs.unlink(req.file.path);
    } catch (err) {
        await fs.unlink(req.file.path);
        return res.status(500).json({
            error: err
        });
    }
    DeliveryProvider.findByIdAndUpdate(deliveryProviderId, {
            profilePhotoURL: cloudinaryResult.secure_url,
            profilePhotoId: cloudinaryResult.public_id
        })
        .then(deliveryProvider => {
            const profilePhotoId = deliveryProvider.profilePhotoId;
            if (profilePhotoId) {
                cloudinary.uploader.destroy(profilePhotoId);
            }
            return res.status(201).json({
                message: 'Photo uploaded'
            });
        })
        .catch(err => {
            responseToMongooseError(res, err);
        })
}

module.exports = {
    uploadProfilePhoto
}