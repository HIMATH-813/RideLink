process.env.JWT_SECRET = 'test-secret';

const request = require('supertest');
const jwt = require('jsonwebtoken');
const app = require('../app');
const Driver = require('../models/Driver');

jest.mock('../models/Driver', () => ({
  find: jest.fn(),
  findOne: jest.fn(),
  findOneAndUpdate: jest.fn(),
  create: jest.fn()
}));

function authorization(userId = 'user-123', role = 'DRIVER') {
  const token = jwt.sign({ userId, role }, process.env.JWT_SECRET);
  return { Authorization: `Bearer ${token}` };
}

const validProfile = {
  profile: {
    firstName: 'Ravi',
    lastName: 'Perera',
    phone: '+94112223344',
    licenseNumber: 'B1234567',
    licenseExpiry: '2030-01-01T00:00:00.000Z'
  },
  vehicleDetails: {
    model: 'Toyota Prius',
    registrationNumber: 'ABC-1234',
    vehicleType: 'CAR',
    color: 'White',
    capacity: 4
  },
  serviceArea: 'Colombo'
};

describe('Driver API', () => {
  beforeEach(() => jest.resetAllMocks());

  test('rejects requests without a bearer token', async () => {
    const response = await request(app).get('/api/drivers/eligible?serviceArea=Colombo');

    expect(response.status).toBe(401);
    expect(response.body).toMatchObject({ success: false, data: null });
  });

  test('validates profile input before writing', async () => {
    const response = await request(app)
      .put('/api/drivers/profile')
      .set(authorization())
      .send({ serviceArea: 'Colombo' });

    expect(response.status).toBe(400);
    expect(Driver.create).not.toHaveBeenCalled();
  });

  test('creates a profile for the authenticated driver', async () => {
    const createdDriver = { driverId: 'driver-1', userId: 'user-123', ...validProfile };
    Driver.findOne.mockResolvedValue(null);
    Driver.create.mockResolvedValue(createdDriver);

    const response = await request(app)
      .put('/api/drivers/profile')
      .set(authorization())
      .send({ ...validProfile, availabilityStatus: 'AVAILABLE', rating: 5 });

    expect(response.status).toBe(201);
    expect(response.body).toMatchObject({ success: true, data: createdDriver });
    expect(Driver.create).toHaveBeenCalledWith(expect.objectContaining({ userId: 'user-123' }));
    expect(Driver.create.mock.calls[0][0]).not.toHaveProperty('availabilityStatus');
    expect(Driver.create.mock.calls[0][0]).not.toHaveProperty('rating');
  });

  test('returns available drivers filtered by service area and radius', async () => {
    const nearby = { driverId: 'nearby', location: { latitude: 6.9271, longitude: 79.8612 } };
    const farAway = { driverId: 'far', location: { latitude: 7.2906, longitude: 80.6337 } };
    Driver.find.mockReturnValue({ lean: jest.fn().mockResolvedValue([nearby, farAway]) });

    const response = await request(app)
      .get('/api/drivers/eligible?serviceArea=Colombo&latitude=6.9271&longitude=79.8612&maxDistanceKm=5')
      .set(authorization());

    expect(response.status).toBe(200);
    expect(response.body.data).toEqual([nearby]);
    expect(Driver.find).toHaveBeenCalledWith({
      availabilityStatus: 'AVAILABLE',
      serviceArea: 'Colombo'
    });
  });
});