const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const JWT_KEY = process.env.JWT_KEY;
const expiresIn = { expiresIn: '7d' }

const Seller = require('../models/seller');

const { responseToMongooseError } = require('../helpers/responses')

function signin (req, res) {
    const body = req.body;
    Seller.findOne({ email: body.email })
        .exec()
        .then(seller => {
            if (seller === null){
                return res.status(401).json({
                    message: 'No se ha encontrado al usuario'
                });
            }   
            if (!bcrypt.compareSync(body.password, seller.password)){
                return res.status(401).json({
                    message: 'Contraseña errónea'
                });
            }
            if (seller.accountStatus !== 'active') {
                return res.status(400).json({
                    message: 'Su cuenta no se encuentra activa'
                });
            }
            const payload = {
                _id: seller._id,
                email: seller.email,
                firstName: seller.firstName,
                lastName: seller.lastName,
                market: seller.market
            }
            const token = jwt.sign(payload, JWT_KEY, expiresIn);
            const response = {
                token: token,
                user: {
                    firstName: seller.firstName,
                    lastName: seller.lastName
                }
            }
            return res.status(200).json(response);
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

function signup (req, res) {
    let body = req.body;
    Seller.findOne({ email: body.email })
        .exec()
        .then(user => {
            if (user) {
                return res.status(409).json({
                    message: 'Existe usuario con dicho correo, intente de nuevo'
                });
            }
            const seller = new Seller({
                email:body.email,
                password: bcrypt.hashSync(body.password, 10),
                firstName: body.firstName,
                lastName: body.lastName
            });
            seller.save()
                .then(result => {
                    res.status(201).json({
                        message: 'Perfil de vendedor creado satisfactoriamente'
                    })
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