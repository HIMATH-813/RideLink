require('dotenv').config({ path: require('node:path').join(__dirname, '.env') });

const app = require('./app');
const connectToDatabase = require('./config/db');

async function startServer() {
  await connectToDatabase();
  const port = Number(process.env.PORT) || 3002;
  return app.listen(port, () => {
    console.log(`RideLink Driver Service listening on port ${port}`);
  });
}

if (require.main === module) {
  startServer().catch((error) => {
    console.error('Failed to start RideLink Driver Service:', error.message);
    process.exitCode = 1;
  });
}

module.exports = { startServer };