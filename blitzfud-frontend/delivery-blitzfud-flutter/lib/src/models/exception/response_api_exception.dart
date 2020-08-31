class ResponseAPIException implements Exception {
  String _message;

  ResponseAPIException(this._message) {}

  @override
  String toString() {
    return _message;
  }
}
