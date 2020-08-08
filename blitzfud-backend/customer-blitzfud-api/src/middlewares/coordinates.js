function is2DCoordinate (req, res, next) {
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

module.exports = is2DCoordinate