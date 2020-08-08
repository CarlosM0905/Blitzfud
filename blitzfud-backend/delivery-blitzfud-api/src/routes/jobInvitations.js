const { Router } = require('express');
const router = Router();

const JobInvitationsController = require('../controllers/jobInvitations');

const checkAuth = require('../middlewares/authentication');

router.get('/', checkAuth, JobInvitationsController.getAllJobInvitations);
router.patch('/:jobInvitationId', checkAuth, JobInvitationsController.updateJobInvitation);

module.exports = router;