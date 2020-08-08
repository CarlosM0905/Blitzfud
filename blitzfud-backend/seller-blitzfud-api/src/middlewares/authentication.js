const jwt = require('jsonwebtoken');
const JWT_KEY = process.env.JWT_KEY;

function checkAuth (req, res, next) {
    const token = req.headers.authorization;
    if (!token){
        return res.status(401).json({
            message: 'Autenticación ha fallado, inicie sesión'
        });
    }
    jwt.verify(token, JWT_KEY, (err, decodedToken) => {
        if (err){
            return res.status(401).json({
                message: 'Autenticación ha fallado, inicie sesión'
            });
        }
        req.user = decodedToken;
        return next();
    })
} 

module.exports = checkAuth