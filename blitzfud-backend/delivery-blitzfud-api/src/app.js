const express = require('express');
const app = express();

const morgan = require('morgan');
const bodyParser = require('body-parser');
const cloudinary = require('cloudinary').v2;

const Routes = require('./routes');
const Settings = require('./settings');
const cloudinaryConfig = require('./settings/cloudinary');
const ErrorsHandler = require('./controllers/errorHandler');

cloudinary.config(cloudinaryConfig);

app.use(Settings.cors);
app.use(morgan('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use('/auth', Routes.AuthenticationRoutes);
app.use('/uploads', Routes.UploadsRoutes);
app.use('/deliveryProvider', Routes.DeliveryProviderRoutes);
app.use('/jobInvitations', Routes.JobInvitationsRoutes);

app.use(ErrorsHandler.nonExistingEndpointHandler);
app.use(ErrorsHandler.errorHandler);

module.exports = app;