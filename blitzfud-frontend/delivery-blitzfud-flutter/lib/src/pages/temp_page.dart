// IMPORTS
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
// UTILS
import 'package:blitzfud4delivery/src/utils/blitzfud_colors.dart';
import 'package:blitzfud4delivery/src/utils/blitzfud_constants.dart';
import 'package:blitzfud4delivery/src/utils/blitzfud_preference.dart';

class TempPage extends StatelessWidget {
  final prefs = new UserPreferences();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text(
          '¡Bienvenido!',
          style: TextStyle(fontSize: 22),
        ),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Center(child: SvgPicture.asset('assets/svgs/in_construction.svg')),
          Container(
            padding: EdgeInsets.only(top: 40),
            child: Text(
              'App en construcción',
              style: TextStyle(fontSize: 22),
            ),
          )
        ],
      ),
      floatingActionButton: FloatingActionButton(
          backgroundColor: COLOR_PRIMARY,
          onPressed: () async {
            await prefs.clear();
            Navigator.pushReplacementNamed(context, LOGIN_PAGE);
          },
          child: Icon(Icons.power_settings_new)),
    );
  }
}
