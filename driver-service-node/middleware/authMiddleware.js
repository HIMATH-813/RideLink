const jwt = require('jsonwebtoken');

function sendAuthError(res, status, message) {
  return res.status(status).json({ success: false, message, data: null });
}

function authenticate(req, res, next) {
  const authorization = req.get('authorization');
  const token = authorization?.startsWith('Bearer ')
    ? authorization.slice(7)
    : null;

  if (!token) {
    return sendAuthError(res, 401, 'Bearer token is required');
  }

  if (!process.env.JWT_SECRET) {
    return next(new Error('JWT_SECRET is not configured'));
  }

  try {
    const payload = jwt.verify(token, process.env.JWT_SECRET);
    const userId = payload.userId || payload.sub || payload.id;
    if (!userId || !payload.role) {
      return sendAuthError(res, 401, 'Token must include user identity and role');
    }

    req.user = { ...payload, userId: String(userId), role: String(payload.role).toUpperCase() };
    return next();
  } catch (error) {
    return sendAuthError(res, 401, 'Invalid or expired token');
  }
}

function authorizeRoles(...roles) {
  const allowedRoles = roles.map((role) => role.toUpperCase());
  return (req, res, next) => {
    if (!allowedRoles.includes(req.user?.role)) {
      return sendAuthError(res, 403, 'You do not have permission to access this resource');
    }
    return next();
  };
}

module.exports = { authenticate, authorizeRoles };