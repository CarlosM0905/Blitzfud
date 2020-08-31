// IMPORTS
import 'package:flutter/material.dart';
// UTILS
import 'package:blitzfud4delivery/src/utils/blitzfud_colors.dart';

class OnboardingWidget extends StatelessWidget {
  final Widget slide;
  final String title;
  final String information;

  OnboardingWidget(this.slide, this.title, this.information);

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        Expanded(
          child: Container(
              margin: EdgeInsets.all(24), width: double.infinity, child: slide),
        ),
        Text(
          title,
          style: TextStyle(
              fontSize: 22, color: COLOR_PRIMARY, fontWeight: FontWeight.bold),
        ),
        Container(
          margin: EdgeInsets.symmetric(horizontal: 30, vertical: 8),
          child: Text(
            information,
            style: TextStyle(
                fontSize: 16.2,
                color: COLOR_TEXT_INFORMATION,
                fontWeight: FontWeight.w600),
            textAlign: TextAlign.center,
          ),
        )
      ],
    );
  }
}
