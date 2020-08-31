// IMPORTS
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'dart:async';
// MODELS
import 'package:blitzfud4delivery/src/models/auth/dni_model.dart';

class DNIProvider {
  String _token =
      'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im1pbmF2NDE2MDVAYXJhc2oubmV0In0.tPkXvEoYghWqjNG36jQ8YQxmtzusnEP47Zo17cfHTWQ';
  String _url = 'dniruc.apisperu.com';

  Future<DNI> getDNI(String dni) async {
    final url = Uri.https(_url, 'api/v1/dni/$dni', {
      'token': _token,
    });
    try {
      final resp = await http.get(url);
      final decodedData = json.decode(resp.body);
      final information = new DNI.fromJsonMap(decodedData);
      return information;
    } catch (e) {
      return null;
    }
  }
}
