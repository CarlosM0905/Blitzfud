const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const JWT_KEY = process.env.JWT_KEY;
const expiresIn = { expiresIn: '7d' }

const DeliveryProvider = require('../models/deliveryProvider');
const { responseToMongooseError } = require('../helpers/responses');

function signin (req, res) {
    const body = req.body;
    DeliveryProvider.findOne({ phoneNumber: body.phoneNumber })
        .exec()
        .then(deliveryProvider => {
            if (deliveryProvider === null) {
                return res.status(401).json({
                    message: 'No se ha encontrado al usuario'
                });
            }
            if (!bcrypt.compareSync(body.password, deliveryProvider.password)) {
                return res.status(401).json({
                    message: 'Contraseña errónea'
                });
            }
            if (deliveryProvider.accountStatus !== 'active') {
                return res.status(400).json({
                    message: 'Su cuenta no se encuentra activa'
                });
            }
            let payload = {
                _id: deliveryProvider._id,
                nationalIdentityNumber: deliveryProvider.nationalIdentityNumber,
                phoneNumber: deliveryProvider.phoneNumber,
                firstName: deliveryProvider.firstName,
                lastName: deliveryProvider.lastName,
                cellphoneNumber: deliveryProvider.cellphoneNumber
            }
            const token = jwt.sign(payload, JWT_KEY, expiresIn);
            const response = {
                token: token,
                user: {
                    firstName: deliveryProvider.firstName,
                    lastName: deliveryProvider.lastName,
                    location: {
                        address: deliveryProvider.location.address,
                        coordinates: deliveryProvider.location.coordinates
                    },
                },
            }
            return res.status(200).json(response);
        })
        .catch(err => {
            responseToMongooseError(res, err)
        });
}

function signup (req, res) {
    const body = req.body;
    DeliveryProvider.findOne({ 
            $or: [
                    { phoneNumber: body.phoneNumber },
                    { nationalIdentityNumber: body.nationalIdentityNumber }
                 ] 
        })
        .exec()
        .then(existingProvider => {
            if (existingProvider) {
                if (existingProvider.phoneNumber == body.phoneNumber && 
                    existingProvider.nationalIdentityNumber == body.nationalIdentityNumber){
                    return res.status(409).json({
                        message: 'Existe un repartidor registrado con dicho DNI y número de celular'
                    });
                } else {
                    if (existingProvider.phoneNumber == body.phoneNumber){
                        return res.status(409).json({
                            message: 'Existe un repartidor registrado con dicho número de celular'
                        });
                    } else {
                        return res.status(409).json({
                            message: 'Existe un repartidor registrado con dicho número de DNI'
                        });
                    }
                }
            }
            const deliveryProvider = new DeliveryProvider({
                nationalIdentityNumber: body.nationalIdentityNumber,
                phoneNumber: body.phoneNumber,
                password: bcrypt.hashSync(body.password, 10),
                firstName: body.firstName,
                lastName: body.lastName,
                location: body.location,
            });
            deliveryProvider.save()
                .then(result => {
                    res.status(201).json({
                        message: 'Perfil de repartidor creado satisfactoriamente'
                    });
                })
                .catch(err => {
                    responseToMongooseError(res, err)
                });
            
        })
        .catch(err => {
            responseToMongooseError(res, err)
        });
}

module.exports = {
    signin,
    signup
}