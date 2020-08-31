const express = require('express');
const morgan = require('morgan');
const bodyParser = require('body-parser');

const app = express();

const Routes = require('./routes');
const Settings = require('./settings');
const ErrorsHandler = require('./controllers/errorHandler');

app.use(morgan('dev'));
app.use(Settings.cors);
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use('/auth', Routes.AuthenticationRoutes);
app.use('/categories', Routes.CategoriesRoutes);
app.use('/markets', Routes.MarketsRoutes);
app.use('/markets', Routes.ProductsRoutes);
app.use('/cart', Routes.CartRoutes);
app.use('/orders', Routes.OrdersRoutes);
app.use('/purchases', Routes.PurchasesRoutes);
app.use('/purchaseOrders', Routes.PurchaseOrdersRoutes);
app.use('/favoriteMarkets', Routes.FavoriteMarketsRoutes);
app.use('/search', Routes.SearchRoutes);

app.use(ErrorsHandler.nonExistingEndpointHandler);
app.use(ErrorsHandler.errorHandler);

module.exports = app;