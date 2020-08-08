function responseToInvalidFormat (res) {
    res.status(400).json({
        message: 'Mal formato de solicitud'
    });
}

function responseToMongooseError (res, err) {
    if (err.kind === 'ObjectId') {
		res.status(404).json({
			'message': 'ID no existe'
		})
	} else {
        res.status(500).json({
            message: 'Error interno de servidor, reintente en unos minutos por favor',
            error: err
        });
    }
}

module.exports = {
    responseToInvalidFormat,
    responseToMongooseError
};