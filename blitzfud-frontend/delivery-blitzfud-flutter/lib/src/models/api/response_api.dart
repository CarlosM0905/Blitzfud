class ResponseAPI {
  String _message;

  ResponseAPI(this._message);

  ResponseAPI.fromJsonMap(Map<String, dynamic> json) {
    _message = json['message'] ?? 'Sin respuesta';
  }

  String get message => _message;
}
