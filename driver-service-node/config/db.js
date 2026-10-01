const mongoose = require('mongoose');

async function connectToDatabase() {
  const { MONGO_URI } = process.env;
  if (!MONGO_URI) {
    throw new Error('MONGO_URI is required');
  }

  await mongoose.connect(MONGO_URI);
  return mongoose.connection;
}

module.exports = connectToDatabase;