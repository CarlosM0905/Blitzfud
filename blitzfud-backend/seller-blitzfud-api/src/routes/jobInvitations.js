const { Router } = require('express');
const router = Router();

const checkAuth = require('../middlewares/authentication');
const JobInvitationMiddleware = require('../middlewares/jobInvitation');
const DeliveryProviderMiddleware = require('../middlewares/deliveryProvider');

const JobInvitationsController = require('../controllers/jobInvitations');

router.get('/', checkAuth,
                JobInvitationsController.getAllJobInvitations);
router.post('/', checkAuth,
                 DeliveryProviderMiddleware.isNear,
                 JobInvitationMiddleware.hasBeenInvited,
                 JobInvitationsController.createJobInvitation);
router.patch('/:jobInvitationId', 
                 checkAuth,
                 JobInvitationsController.updateJobInvitation);

module.exports = router;