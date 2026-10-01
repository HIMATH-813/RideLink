function notFoundHandler(req, res) {
  return res.status(404).json({
    success: false,
    message: 'Route not found',
    data: null
  });
}

function errorHandler(error, req, res, next) {
  if (res.headersSent) {
    return next(error);
  }

  let status = error.statusCode || error.status || 500;
  let message = error.message || 'Internal server error';

  if (error.type === 'entity.parse.failed') {
    status = 400;
    message = 'Invalid JSON request body';
  } else if (error.name === 'ValidationError') {
    status = 400;
    message = 'Request data failed validation';
  } else if (error.name === 'CastError') {
    status = 400;
    message = 'Invalid identifier or value';
  } else if (error.code === 11000) {
    status = 409;
    message = 'A driver or vehicle with this identifier already exists';
  }

  if (status >= 500) {
    message = 'Internal server error';
  }

  return res.status(status).json({ success: false, message, data: null });
}

module.exports = { notFoundHandler, errorHandler };