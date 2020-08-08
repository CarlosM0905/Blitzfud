function checkCoordinates (req, res, next) {
    let location = req.body.location;
    if (location.constructor === Array) {
        if (location.length == 2) {
            let lat = Number(location[0]);
            let lon = Number(location[1]);
            if(!isNaN(lat) && !isNaN(lon)){
                return next();
            }
        }
    }
    return res.status(400).json({
        message: 'Mal formato de coordenadas'
    });
}


function formatCoordinates (req, res, next) {
    const location = req.body.location;
    req.body.location = {
        coordinates: location
    }
    next();
}

module.exports = {
    checkCoordinates,
    formatCoordinates
}