import 'dart:convert';
import 'package:blitzfud4delivery/src/models/api/response_api.dart';
import 'package:blitzfud4delivery/src/models/auth/user_model.dart';
import 'package:blitzfud4delivery/src/models/exception/response_api_exception.dart';
import 'package:blitzfud4delivery/src/utils/blitzfud_preference.dart';
import 'package:blitzfud4delivery/src/utils/blitzfud_utils.dart';
import 'package:http/http.dart' as http;

import 'package:blitzfud4delivery/src/utils/blitzfud_constants.dart';

// TODO: SAVE TOKEN AND USER IN PREFERENCES
class AuthProvider {
  final prefs = new UserPreferences();
  final url = "auth";

  Future<ResponseAPI> login(String phoneNumber, String password) async {
    final body = {'phoneNumber': phoneNumber, 'password': password};
    final resp = await http.post("$BACKEND_BASE_URL/$url/signin", body: body);
    final decodedData = json.decode(resp.body);

    if (!isSuccessfull(resp.statusCode)) {
      throw new ResponseAPIException(decodedData['message']);
    }

    // SAVE DATA IN PREFERENCES
    prefs.token = decodedData['token'];
    prefs.user = new User.fromJsonMap(decodedData['user']);

    return new ResponseAPI('Bienvenido ${prefs.user.firstName}');
  }

  Future<ResponseAPI> register(
      String nationalIdentityNumber,
      String phoneNumber,
      String password,
      String firstName,
      String lastName,
      double locationLat,
      double locationLng,
      String address) async {
    List<double> coordinates = [locationLng, locationLat];

    Map<String, dynamic> body = {
      'nationalIdentityNumber': nationalIdentityNumber,
      'phoneNumber': phoneNumber,
      'password': password,
      'firstName': firstName,
      'lastName': lastName,
      'location': {'coordinates': coordinates, 'address': address}
    };

    String bodyJson = json.encode(body);

    Map<String, String> headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
    };

    final resp = await http.post("$BACKEND_BASE_URL/$url/signup",
        body: bodyJson, headers: headers);
    final decodedData = json.decode(resp.body);

    if (!isSuccessfull(resp.statusCode)) {
      throw new ResponseAPIException(decodedData['message']);
    }

    return new ResponseAPI.fromJsonMap(decodedData);
  }
}
