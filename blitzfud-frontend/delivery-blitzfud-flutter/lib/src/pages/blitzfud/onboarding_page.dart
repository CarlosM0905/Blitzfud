// IMPORTS
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
// UTILS
import 'package:blitzfud4delivery/src/utils/blitzfud_colors.dart';
import 'package:blitzfud4delivery/src/utils/blitzfud_constants.dart';
import 'package:blitzfud4delivery/src/utils/blitzfud_preference.dart';
// WIDGETS
import 'package:blitzfud4delivery/src/widgets/onboarding_widget.dart';
import 'package:blitzfud4delivery/src/widgets/slideshow_widget.dart';

// OnBoarding - Guia de usuario
class OnboardingPage extends StatelessWidget {
  final prefs = new UserPreferences();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: Column(
        children: <Widget>[
          Expanded(
            child: SlideshowWidget(
              bulletPrimario: 15,
              bulletSecundario: 12,
              colorPrimario: COLOR_PRIMARY,
              colorSecundario: Colors.grey,
              slides: <Widget>[
                itemOnBoarding(
                  'onboarding1.svg',
                  'Ingresa tu ubicación',
                  'Establece tu ubicación y tiendas cercanas a ti, te enontraran.',
                ),
                itemOnBoarding(
                  'onboarding2.svg',
                  'Recibe ofertas de contrato',
                  'Espera a que un vendedor te contacte y te ofrezca un contrato.',
                ),
                itemOnBoarding(
                  'onboarding3.svg',
                  'Haz alianzas',
                  'Acepta una oferta de contrato y mantente en contacto con el vendedor para realizar envios.',
                ),
                itemOnBoarding(
                  'onboarding4.svg',
                  'Realiza envios',
                  'Lleva las ordenes de tu tienda aliada a un cliente y genera ingresas por delivery.',
                ),
              ],
            ),
          ),
          Container(
            margin: EdgeInsets.only(bottom: 16.0),
            child: ButtonTheme(
              minWidth: 150.0,
              height: 35.0,
              child: RaisedButton(
                child: Text(
                  'Omitir',
                  style: TextStyle(fontSize: 20),
                ),
                color: COLOR_PRIMARY,
                textColor: Colors.white,
                shape: StadiumBorder(),
                onPressed: () {
                  prefs.onboardingSeen = true;
                  Navigator.pushReplacementNamed(context, LOGIN_PAGE);
                },
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget itemOnBoarding(String svg, String title, String information) {
    return OnboardingWidget(
      SvgPicture.asset('assets/svgs/$svg'),
      title,
      information,
    );
  }
}
