import 'dart:convert';

import 'package:blitzfud4delivery/src/models/auth/user_model.dart';
import 'package:shared_preferences/shared_preferences.dart';

class UserPreferences {
  static final UserPreferences _instance = new UserPreferences._internal();

  factory UserPreferences() {
    return _instance;
  }

  UserPreferences._internal();

  SharedPreferences prefs;

  initPrefs() async {
    this.prefs = await SharedPreferences.getInstance();
  }

  bool get onboardingSeen {
    return prefs.getBool('onboardingSeen') ?? false;
  }

  set onboardingSeen(bool value) {
    prefs.setBool('onboardingSeen', value);
  }

  String get token {
    return prefs.getString('token') ?? 'empty';
  }

  set token(String token) {
    prefs.setString('token', token);
  }

  bool get existsSession {
    return this.token != 'empty';
  }

  set user(User user) {
    prefs.setString('user', json.encode(user.toJson()));
  }

  User get user {
    final userString = prefs.getString('user') ?? 'empty';

    if (userString == 'empty') return new User();

    return new User.fromJsonMap(json.decode(userString));
  }

  clear() async {
    await prefs.clear();
    onboardingSeen = true;
  }
}
