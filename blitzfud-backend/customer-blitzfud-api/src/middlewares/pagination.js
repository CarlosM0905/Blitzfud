function formatQuery (req, res, next) {
    req.query.offset = Number(req.query.offset);
    req.query.limit = Number(req.query.limit);
    next();
}

module.exports = {
    formatQuery
}