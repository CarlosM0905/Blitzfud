const { responseToInvalidFormat } = require('../helpers/responses');

function checkCoordinatesInQuery (req, res, next) {
    let location = req.query;

    let lat = Number(location.lat);
    let lon = Number(location.lon);
    
    if(!isNaN(lat) && !isNaN(lon)){
        req.query.lat = lat;
        req.query.lon = lon;
        return next();
    }
    
    return res.status(400).json({
        message: "Mal formato de coordenadas"
    });
}

function checkCoordinatesInBody (req, res, next) {
    let location = req.body.location;
    if (!location.coordinates){
        return responseToInvalidFormat(res);
    }
    if (!location.address){
        return responseToInvalidFormat(res);
    }
    if (location.coordinates.constructor === Array) {
        if (location.coordinates.length == 2) {
            let lon = Number(location.coordinates[0]);
            let lat = Number(location.coordinates[1]);
            if(!isNaN(lon) && !isNaN(lat)){
                return next();
            }
        }
    }
    return res.status(400).json({
        message: 'Mal formato de coordenadas'
    });
}

module.exports = {
    checkCoordinatesInQuery,
    checkCoordinatesInBody
}