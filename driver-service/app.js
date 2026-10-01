const path = require('node:path');
const express = require('express');
const cors = require('cors');
const swaggerUi = require('swagger-ui-express');
const driverRoutes = require('./routes/driverRoutes');
const swaggerDocument = require('./swagger.json');
const { notFoundHandler, errorHandler } = require('./middleware/errorMiddleware');

const app = express();

app.use(cors());
app.use(express.json({ limit: '1mb' }));
app.get('/health', (req, res) => {
  res.status(200).json({ success: true, message: 'Driver service is healthy', data: null });
});
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocument));
app.use('/api/drivers', driverRoutes);
app.use(notFoundHandler);
app.use(errorHandler);

module.exports = app;