const express = require('express');
const { body, param, query, validationResult } = require('express-validator');
const {
  createOrUpdateProfile,
  getDriverProfile,
  updateAvailability,
  updateLocation,
  getEligibleDrivers
} = require('../controllers/driverController');
const { authenticate, authorizeRoles } = require('../middleware/authMiddleware');

const router = express.Router();

function validateRequest(req, res, next) {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({
      success: false,
      message: 'Request validation failed',
      data: null,
      errors: errors.array().map(({ path, msg }) => ({ field: path, message: msg }))
    });
  }
  return next();
}

const driverIdValidation = param('driverId').trim().notEmpty();

router.use(authenticate, authorizeRoles('DRIVER', 'ADMIN'));

router.put('/profile', [
  body('profile.firstName').trim().notEmpty(),
  body('profile.lastName').trim().notEmpty(),
  body('profile.phone').trim().notEmpty(),
  body('profile.licenseNumber').trim().notEmpty(),
  body('profile.licenseExpiry').isISO8601().toDate(),
  body('vehicleDetails.model').trim().notEmpty(),
  body('vehicleDetails.registrationNumber').trim().notEmpty(),
  body('vehicleDetails.vehicleType').trim().notEmpty(),
  body('vehicleDetails.color').trim().notEmpty(),
  body('vehicleDetails.capacity').isInt({ min: 1 }).toInt(),
  body('serviceArea').trim().notEmpty()
], validateRequest, createOrUpdateProfile);

router.get('/eligible', [
  query('serviceArea').trim().notEmpty(),
  query('latitude').optional().isFloat({ min: -90, max: 90 }).toFloat(),
  query('longitude').optional().isFloat({ min: -180, max: 180 }).toFloat(),
  query('maxDistanceKm').optional().isFloat({ min: 0, max: 100 }).toFloat(),
  query().custom((_, { req }) => {
    const hasLatitude = req.query.latitude !== undefined;
    const hasLongitude = req.query.longitude !== undefined;
    if (hasLatitude !== hasLongitude) {
      throw new Error('latitude and longitude must be provided together');
    }
    if (req.query.maxDistanceKm !== undefined && !hasLatitude) {
      throw new Error('maxDistanceKm requires latitude and longitude');
    }
    return true;
  })
], validateRequest, getEligibleDrivers);

router.get('/profile/:driverId', driverIdValidation, validateRequest, getDriverProfile);

router.patch('/:driverId/availability', [
  driverIdValidation,
  body('availabilityStatus').isIn(['AVAILABLE', 'UNAVAILABLE', 'IN_RIDE'])
], validateRequest, updateAvailability);

router.patch('/:driverId/location', [
  driverIdValidation,
  body('latitude').isFloat({ min: -90, max: 90 }).toFloat(),
  body('longitude').isFloat({ min: -180, max: 180 }).toFloat()
], validateRequest, updateLocation);

module.exports = router;