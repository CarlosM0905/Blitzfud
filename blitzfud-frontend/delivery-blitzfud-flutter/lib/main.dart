// IMPORTS
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
// UTILS
import 'package:blitzfud4delivery/src/utils/blitzfud_colors.dart';
import 'package:blitzfud4delivery/src/utils/blitzfud_constants.dart';
import 'package:blitzfud4delivery/src/utils/blitzfud_preference.dart';
// ROUTES
import 'package:blitzfud4delivery/src/routes/app_routing.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final prefs = new UserPreferences();
  await prefs.initPrefs();

  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: Firebase.initializeApp(),
      builder: (context, snapshot) {
        return MaterialApp(
          debugShowCheckedModeBanner: false,
          title: 'Blitzfud Delivery',
          initialRoute: SPLASH_PAGE,
          routes: getApplicationRoutes(),
          theme: ThemeData(primaryColor: COLOR_PRIMARY, fontFamily: 'Poppins'),
        );
      },
    );
  }
}
