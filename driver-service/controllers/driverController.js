const Driver = require('../models/Driver');

function httpError(statusCode, message) {
  const error = new Error(message);
  error.statusCode = statusCode;
  return error;
}

function assertDriverAccess(driver, user) {
  if (user.role !== 'ADMIN' && String(driver.userId) !== user.userId) {
    throw httpError(403, 'You can only access your own driver profile');
  }
}

function distanceInKilometers(first, second) {
  const radians = (degrees) => (degrees * Math.PI) / 180;
  const latitudeDelta = radians(second.latitude - first.latitude);
  const longitudeDelta = radians(second.longitude - first.longitude);
  const startLatitude = radians(first.latitude);
  const endLatitude = radians(second.latitude);
  const haversine = Math.sin(latitudeDelta / 2) ** 2
    + Math.cos(startLatitude) * Math.cos(endLatitude)
      * Math.sin(longitudeDelta / 2) ** 2;

  return 6371 * 2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));
}

async function createOrUpdateProfile(req, res) {
  const userId = req.user.userId;
  const profileData = {
    profile: req.body.profile,
    vehicleDetails: req.body.vehicleDetails,
    serviceArea: req.body.serviceArea
  };
  const existingDriver = await Driver.findOne({ userId });
  let driver;
  let status;
  let message;

  if (existingDriver) {
    driver = await Driver.findOneAndUpdate(
      { userId },
      { $set: profileData },
      { new: true, runValidators: true }
    );
    status = 200;
    message = 'Driver profile updated';
  } else {
    driver = await Driver.create({ ...profileData, userId });
    status = 201;
    message = 'Driver profile created';
  }

  return res.status(status).json({ success: true, message, data: driver });
}

async function getDriverProfile(req, res) {
  const driver = await Driver.findOne({ driverId: req.params.driverId });
  if (!driver) {
    throw httpError(404, 'Driver profile not found');
  }

  assertDriverAccess(driver, req.user);
  return res.status(200).json({
    success: true,
    message: 'Driver profile retrieved',
    data: driver
  });
}

async function updateAvailability(req, res) {
  const existingDriver = await Driver.findOne({ driverId: req.params.driverId });
  if (!existingDriver) {
    throw httpError(404, 'Driver profile not found');
  }
  assertDriverAccess(existingDriver, req.user);

  const driver = await Driver.findOneAndUpdate(
    { driverId: req.params.driverId },
    { $set: { availabilityStatus: req.body.availabilityStatus } },
    { new: true, runValidators: true }
  );

  return res.status(200).json({
    success: true,
    message: 'Availability status updated',
    data: driver
  });
}

async function updateLocation(req, res) {
  const existingDriver = await Driver.findOne({ driverId: req.params.driverId });
  if (!existingDriver) {
    throw httpError(404, 'Driver profile not found');
  }
  assertDriverAccess(existingDriver, req.user);

  const driver = await Driver.findOneAndUpdate(
    { driverId: req.params.driverId },
    {
      $set: {
        location: {
          latitude: req.body.latitude,
          longitude: req.body.longitude,
          updatedAt: new Date()
        }
      }
    },
    { new: true, runValidators: true }
  );

  return res.status(200).json({
    success: true,
    message: 'Driver location updated',
    data: driver
  });
}

async function getEligibleDrivers(req, res) {
  const { serviceArea, latitude, longitude } = req.query;
  const drivers = await Driver.find({
    availabilityStatus: 'AVAILABLE',
    serviceArea
  }).lean();

  let eligibleDrivers = drivers;
  if (latitude !== undefined && longitude !== undefined) {
    const origin = { latitude: Number(latitude), longitude: Number(longitude) };
    const maxDistanceKm = req.query.maxDistanceKm === undefined
      ? 10
      : Number(req.query.maxDistanceKm);

    eligibleDrivers = drivers.filter((driver) => {
      const driverLocation = driver.location;
      if (typeof driverLocation?.latitude !== 'number'
        || typeof driverLocation?.longitude !== 'number') {
        return false;
      }
      return distanceInKilometers(origin, driverLocation) <= maxDistanceKm;
    });
  }

  return res.status(200).json({
    success: true,
    message: 'Eligible drivers retrieved',
    data: eligibleDrivers
  });
}

module.exports = {
  createOrUpdateProfile,
  getDriverProfile,
  updateAvailability,
  updateLocation,
  getEligibleDrivers
};