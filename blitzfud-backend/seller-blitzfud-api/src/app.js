const express = require('express');
const app = express();

const morgan = require('morgan');
const bodyParser = require('body-parser');

const Routes = require('./routes');
const Settings = require('./settings');
const ErrorsHandler = require('./controllers/errorHandler');

app.use(Settings.cors);
app.use(morgan('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));

app.use('/auth', Routes.AuthenticationRoutes);
app.use('/market', Routes.MarketRoutes);
app.use('/products', Routes.ProductsRoutes);
app.use('/categories', Routes.CategoriesRoutes);
app.use('/orders', Routes.OrdersRoutes);
app.use('/deliveryProviders', Routes.DeliveryProvidersRoutes);
app.use('/jobInvitations', Routes.JobInvitationsRoutes);
app.use('/deliveryWorkers', Routes.DeliveryWorkersRoutes);

app.use(ErrorsHandler.nonExistingEndpointHandler);
app.use(ErrorsHandler.errorHandler);

module.exports = app;