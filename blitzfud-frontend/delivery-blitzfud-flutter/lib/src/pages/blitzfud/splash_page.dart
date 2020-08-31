// IMPORTS
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
// UTILS
import 'package:blitzfud4delivery/src/utils/blitzfud_constants.dart';
import 'package:blitzfud4delivery/src/utils/blitzfud_preference.dart';
// BACKGROUND
import 'package:blitzfud4delivery/src/widgets/bg_splash.dart';

class SplashScreenPage extends StatefulWidget {
  @override
  _SplashScreenPageState createState() => _SplashScreenPageState();
}

class _SplashScreenPageState extends State<SplashScreenPage> {
  final prefs = new UserPreferences();

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    Future.delayed(Duration(milliseconds: 2000), () {
      if (!prefs.onboardingSeen) {
        Navigator.pushReplacementNamed(context, ONBOARDING_PAGE);
      } else {
        if (prefs.existsSession) {
          Navigator.pushReplacementNamed(context, HOME_PAGE);
        } else {
          Navigator.pushReplacementNamed(context, LOGIN_PAGE);
        }
      }
    });

    return SafeArea(
      child: Scaffold(
        body: Container(
            child: SvgPicture.asset(
          'assets/svgs/splash_screen.svg',
          fit: BoxFit.fill,
        )),
      ),
    );
  }

  Widget _content() {
    return Container(
      width: double.infinity,
      height: double.infinity,
      child: Column(
        children: [
          _header(),
          _footer(),
        ],
      ),
    );
  }

  Widget _header() {
    return Expanded(
        flex: 67,
        child: Column(
          children: [
            Expanded(
              child: Container(
                padding: EdgeInsets.all(60),
                child: SvgPicture.asset(
                  'assets/svgs/logo_blue.svg',
                  width: double.infinity,
                ),
              ),
            ),
            Transform.rotate(
                angle: -0.28,
                child: SvgPicture.asset(
                  'assets/svgs/delivery_man.svg',
                  width: 80,
                  height: 80,
                  alignment: Alignment.bottomCenter,
                ))
          ],
        ));
  }

  Widget _footer() {
    return Expanded(
        flex: 33,
        child: Column(
          children: [
            Text(
              'Blitzfud',
              style: TextStyle(color: Colors.white, fontSize: 34),
            ),
            Text(
              'for Delivery',
              style: TextStyle(color: Colors.white, fontSize: 20),
            ),
            Expanded(
              child: Container(),
            ),
            Container(
              margin: EdgeInsets.only(bottom: 16),
              child: Text(
                'By Spika',
                style: TextStyle(color: Colors.white, fontSize: 16),
              ),
            ),
          ],
        ));
  }
}
