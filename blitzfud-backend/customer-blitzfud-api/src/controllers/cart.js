const urljoin = require('url-join');

const Customer = require('../models/customer');
const Product = require('../models/product');

const MARKETS_PATH = urljoin(process.env.HOST, 'markets');
const ENDPOINT_PATH = urljoin(process.env.HOST, 'cart');

const { responseToMongooseError } = require('../helpers/responses');

function getAllItems (req, res) {
    const customerId = req.user._id;
    Customer.findById(customerId)
        .select('shoppingCart')
        .populate({
            path: 'shoppingCart.market',
            select: 'name deliveryMethods deliveryPrice'
        })
        .populate({
            path: 'shoppingCart.items.product',
            select: 'name unitOfMeasurement content price market _id'
        })
        .exec()
        .then(doc => {
            if (doc.shoppingCart === undefined || doc.shoppingCart.length == 0) {
                return res.status(200).json({
                    count: 0,
                    subcarts: [],
                    request: {
                        type: 'POST', 
                        url: ENDPOINT_PATH,
                        body : {
                            productId: 'ObjectId',
                            quantity: 'Number'
                        }
                    }
                });
            }
            let cart = doc.shoppingCart.filter(subcart =>
                subcart.items.length != 0
            );
            const response = {
                count: cart.length,
                subcarts: cart.map(subcart => {
                    return {
                        market: {
                            name: subcart.market.name,
                            deliveryMethods: subcart.market.deliveryMethods,
                            deliveryPrice: subcart.market.deliveryPrice,
                            _id: subcart.market._id,
                            request: {
                                type: 'GET',
                                url: urljoin(MARKETS_PATH, subcart.market._id.toString())
                            }
                        },
                        items: subcart.items.map(item => {
                            return {
                                product: item.product,
                                quantity: item.quantity,
                                request: {
                                    type: 'GET',
                                    URL: urljoin(MARKETS_PATH, item.product.market.toString(), 
                                                '/products', item.product._id.toString())
                                }
                            }
                        })
                    }
                }),
                request: {
                    type: 'POST', 
                    url: ENDPOINT_PATH,
                    body : {
                        productId: 'ObjectId',
                        quantity: 'Number'
                    }
                }
            }
            res.status(200).json(response);
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

async function getMarketOfProduct (productId) {
    return Product.findById(productId)
                .select('market')
                .exec()
                .then(doc => {
                    if (doc) {
                        return doc.market.toString();
                    } else {
                        return null;
                    }
                });
}

async function isMarketInCart (customerId, marketId) {
    return Customer.findById(customerId)
                .select('shoppingCart.market')
                .exec()
                .then(doc => {
                    const shoppingCart = doc.shoppingCart;
                    if (shoppingCart !== undefined){
                        for (let i = 0; i < shoppingCart.length; i++) {
                            if (shoppingCart[i].market == marketId){
                                return true;
                            }
                        }
                        return false;
                    } else {
                        return false;
                    }
                });
}

function getConditionsAddItemToNewStore (item, customerId, marketId) {
    const subCart = {
        market: marketId,
        items: [item]
    }
    const conditions = {
        _id: customerId
    }
    const update = {
        $push: { shoppingCart: subCart }
    }
    return [conditions, update];
}

function getConditionsAdditemToExistingStore (item, customerId, marketId) {
    const conditions = {
        _id: customerId,
        'shoppingCart.items.product': { $ne: item.product },
        'shoppingCart.market': marketId
    }
    const update = {
        $push: { 'shoppingCart.$.items': item }
    }
    return [conditions, update];
}

async function addItem (req, res) {
    const customerId = req.user._id;
    const productId = req.body.productId;
    const item = {
        product: req.body.productId,
        quantity: req.body.quantity
    }
    let marketId;
    let conditions, update;
    try {
        marketId = await getMarketOfProduct(productId);
        if (!marketId) {
            return res.status(400).json({
                message: 'Producto no existe'
            });
        }
        if (await isMarketInCart(customerId, marketId)) {
            [conditions, update] = getConditionsAdditemToExistingStore(item, customerId, marketId);
        } else {
            [conditions, update] = getConditionsAddItemToNewStore(item, customerId, marketId);
        }
    } catch (err) {
        return responseToMongooseError(res, err);
    }
    Customer.updateOne(conditions, update)
        .then(result => {
            if (result.nModified > 0) {
                res.status(201).json({
                    message: 'Item añadido satisfactoriamente',
                    request: {
                        type: 'GET',
                        url: ENDPOINT_PATH
                    }
                });
            } else {
                res.status(409).json({
                    message: 'Producto se encuentra ya en el carrito'
                });
            }
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

async function updateItem (req, res) {
    const productId = req.params.productId;
    const quantity = req.body.quantity;
    const customerId = req.user._id;
    const marketId = await getMarketOfProduct(productId);
    const conditions = {
        _id: customerId,
        'shoppingCart': {
            '$elemMatch': {
                'market': marketId, 
                'items.product': productId
            }
        }
    }
    const update = {
        'shoppingCart.$[outer].items.$[inner].quantity': quantity
    }
    const arrayFilters = {
        'arrayFilters': [{ 'outer.market': marketId }, { 'inner.product': productId }] 
    }  
    Customer.updateOne(conditions, update, arrayFilters)
        .then(result => {
            if (result.nModified > 0){
                res.status(200).json({
                    message: 'Item actualizado con éxito',
                    request: {
                        type: 'GET',
                        url: ENDPOINT_PATH
                    }
                });
            } else {
                res.status(404).json({
                    message: 'Producto no se encuentra en el carrito'
                });                
            }
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

async function removeItem (req, res) {
    const productId = req.params.productId;
    const customerId = req.user._id;
    const marketId = await getMarketOfProduct(productId);
    const conditions = {
        _id: customerId,
        'shoppingCart.market': marketId
    };
    const update = {
        $pull: {
            'shoppingCart.$.items': {
                product: productId
            }
        }
    };
    Customer.updateOne(conditions, update)
        .then(result => {
            if (result.nModified > 0){
                res.status(200).json({
                    message: 'Item removido del carrito',
                    request: {
                        type: 'POST', 
                        url: ENDPOINT_PATH,
                        body : {
                            productId: 'ObjectId',
                            quantity: 'Number'
                        }
                    }
                });
            } else {
                res.status(409).json({
                    message: 'Producto no se encuentra en el carrito'
                });                
            }
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

function emptyCart (req, res) {
    const customerId = req.user._id;
    const conditions = {
        _id: customerId
    }
    const update = { 
        $unset: { 
            shoppingCart : ''
        } 
    }
    Customer.findOneAndUpdate(conditions, update)
        .then(doc => {
            if (doc.shoppingCart.length > 0){
                let numberOfItems = doc.shoppingCart
                                    .map(subcart => subcart.items.length)
                                    .reduce((sum1, sum2) => sum1 + sum2);
                if (numberOfItems > 0) {
                    return res.status(200).json({
                            message: 'Carrito vaciado',
                            request: {
                                type: 'POST', 
                                url: ENDPOINT_PATH,
                                body : {
                                    productId: 'ObjectId',
                                    quantity: 'Number'
                                }
                            }
                        });
                } 
            }   
            res.status(404).json({
                message: 'Carrito se encuentra vacío'
            });                   
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

function removeMarket (req, res) {
    const customerId = req.user._id;
    const marketId = req.params.marketId;
    const conditions = {
        '_id': customerId,
        'shoppingCart.items': { $gt: [] }
    };
    const update = {
        $pull: {
            'shoppingCart': {
                market: marketId
            }
        }
    };
    Customer.updateOne(conditions, update)
        .then(result => {
            if (result.nModified > 0){
                res.status(200).json({
                    message: 'Producto(s) eliminados satisfactoriamente'
                });                
            } else {
                res.status(404).json({
                    message: 'Tienda no ha sido encontrada en tu carrito'
                })
            }
        })
        .catch(err => {
            responseToMongooseError(res, err);
        });
}

module.exports = {
    getAllItems,
    addItem,
    updateItem,
    emptyCart,
    removeItem,
    removeMarket
}