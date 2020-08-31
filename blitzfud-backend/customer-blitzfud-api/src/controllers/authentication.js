const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const JWT_KEY = process.env.JWT_KEY;
const expiresIn = { expiresIn: '7d' };

const Customer = require('../models/customer');

const { responseToMongooseError } = require('../helpers/responses')

function signin (req, res) {
    let body = req.body;
    Customer.findOne({ phoneNumber: body.phoneNumber })
        .exec()
        .then(customer => {
            if (customer === null){
                return res.status(401).json({
                    message: 'No se ha encontrado al usuario'
                });
            }   
            if (!bcrypt.compareSync(body.password, customer.password)){
                return res.status(401).json({
                    message: 'Contraseña errónea'
                });
            }
            if (customer.accountStatus !== 'active') {
                return res.status(400).json({
                    message: 'Su cuenta no se encuentra activa'
                });
            }
            let payload = {
                _id: customer._id,
                phoneNumber: customer.phoneNumber,
                firstName: customer.firstName,
                lastName: customer.lastName
            }
            const token = jwt.sign(payload, JWT_KEY, expiresIn);
            const response = {
                token: token,
                user: {
                    firstName: customer.firstName,
                    lastName: customer.lastName,
                    location: {
                        address: customer.location.address,
                        coordinates: customer.location.coordinates
                    }
                },
            }
            return res.status(200).json(response);
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

function signup (req, res) {
    let body = req.body;
    Customer.findOne({ phoneNumber: body.phoneNumber })
        .exec()
        .then(user => {
            if (user) {
                return res.status(409).json({
                    message: 'Existe usuario con dicho número, intente de nuevo'
                });
            }
            const customer = new Customer({
                phoneNumber: body.phoneNumber,
                email: body.email,
                password: bcrypt.hashSync(body.password, 10),
                firstName: body.firstName,
                lastName: body.lastName,
                location: body.location
            });
            customer.save()
                .then(result => {
                    res.status(201).json({
                        message: 'Perfil de cliente creado satisfactoriamente'
                    });
                })
                .catch(err => {
                    responseToMongooseError(res, err);
                });            
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

module.exports = {
    signin,
    signup
}