// IMPORTS
import 'package:flutter/material.dart';
// PAGES
import 'package:blitzfud4delivery/src/pages/blitzfud/splash_page.dart';
import 'package:blitzfud4delivery/src/pages/blitzfud/onboarding_page.dart';
import 'package:blitzfud4delivery/src/auth/login_page.dart';
import 'package:blitzfud4delivery/src/auth/register_page.dart';
import 'package:blitzfud4delivery/src/auth/verify_phone_page.dart';
import 'package:blitzfud4delivery/src/pages/temp_page.dart';
import 'package:blitzfud4delivery/src/pages/map/map_page.dart';
// UTILS
import 'package:blitzfud4delivery/src/utils/blitzfud_constants.dart';

Map<String, WidgetBuilder> getApplicationRoutes() {
  return <String, WidgetBuilder>{
    SPLASH_PAGE: (BuildContext context) => SplashScreenPage(),
    ONBOARDING_PAGE: (BuildContext context) => OnboardingPage(),
    LOGIN_PAGE: (BuildContext context) => LoginPage(),
    REGISTER_PAGE: (BuildContext context) => RegisterPage(),
    VERIFY_PHONE_PAGE: (BuildContext context) => VerifyPhonePage(),
    HOME_PAGE: (BuildContext context) => TempPage(),
    MAP_PAGE: (BuildContext context) => MapPage(),
  };
}
