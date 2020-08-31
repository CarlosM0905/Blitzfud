import 'package:blitzfud4delivery/src/models/auth/location_model.dart';

class User {
  String _firstName;
  String _lastName;
  LocationAPI _location;

  User() {}

  User.initialValues(this._firstName, this._lastName, this._location) {}

  User.fromJsonMap(Map<String, dynamic> json) {
    _firstName = json['firstName'] ?? '';
    _lastName = json['lastName'] ?? '';
    _location = LocationAPI.fromJsonMap(json['location']);
  }

  Map<String, dynamic> toJson() => {
        "firstName": _firstName,
        "lastName": _lastName,
        "location": _location.toJson(),
      };

  get firstName => _firstName;

  get lastName => _lastName;

  get locationAPI => _location;
}
