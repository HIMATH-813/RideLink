const { randomUUID } = require('node:crypto');
const mongoose = require('mongoose');

const profileSchema = new mongoose.Schema({
  firstName: { type: String, required: true, trim: true },
  lastName: { type: String, required: true, trim: true },
  phone: { type: String, required: true, trim: true },
  licenseNumber: { type: String, required: true, trim: true },
  licenseExpiry: { type: Date, required: true }
}, { _id: false });

const vehicleDetailsSchema = new mongoose.Schema({
  model: { type: String, required: true, trim: true },
  registrationNumber: {
    type: String,
    required: true,
    trim: true,
    uppercase: true,
    unique: true
  },
  vehicleType: { type: String, required: true, trim: true },
  color: { type: String, required: true, trim: true },
  capacity: { type: Number, required: true, min: 1 }
}, { _id: false });

const locationSchema = new mongoose.Schema({
  latitude: { type: Number, min: -90, max: 90 },
  longitude: { type: Number, min: -180, max: 180 },
  updatedAt: { type: Date, default: Date.now }
}, { _id: false });

const driverSchema = new mongoose.Schema({
  driverId: { type: String, required: true, unique: true, default: randomUUID },
  userId: { type: String, required: true, unique: true, index: true },
  profile: { type: profileSchema, required: true },
  vehicleDetails: { type: vehicleDetailsSchema, required: true },
  availabilityStatus: {
    type: String,
    enum: ['AVAILABLE', 'UNAVAILABLE', 'IN_RIDE'],
    default: 'UNAVAILABLE',
    index: true
  },
  location: { type: locationSchema, default: () => ({}) },
  serviceArea: { type: String, required: true, trim: true, index: true },
  rating: { type: Number, min: 0, max: 5, default: 0 }
}, { timestamps: true });

driverSchema.index({ availabilityStatus: 1, serviceArea: 1 });

module.exports = mongoose.model('Driver', driverSchema);