import 'package:google_maps_flutter/google_maps_flutter.dart';

class LocationAPI {
  String _address;
  List<double> _coordinates;

  LocationAPI(this._address, this._coordinates) {}

  LocationAPI.fromJsonMap(Map<String, dynamic> json) {
    _address = json['address'] ?? '';
    _coordinates = [json['coordinates'][0], json['coordinates'][1]];
  }

  Map<String, dynamic> toJson() => {
        "address": _address,
        "coordinates": _coordinates,
      };

  get address => _address;

  get coordinates => _coordinates;

  get coordinatesLatLng => new LatLng(_coordinates[1], _coordinates[0]);
}
