import 'package:flutter/material.dart';

import 'package:blitzfud4delivery/src/utils/blitzfud_colors.dart';

class SplashBackground extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      height: double.infinity,
      width: double.infinity,
      child: CustomPaint(
        painter: _SplashBackgroundPainter(),
      ),
    );
  }
}

// Pintador del fondo
class _SplashBackgroundPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final pencil = Paint();
    final path = new Path();

    pencil.color = COLOR_PRIMARY;
    pencil.style = PaintingStyle.fill;

    path.moveTo(0, size.height * 0.55);
    path.quadraticBezierTo(
        size.width * 0.5, size.height * 0.75, size.width, size.height * 0.55);
    path.lineTo(size.width, size.height);
    path.lineTo(0, size.height);
    canvas.drawPath(path, pencil);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) {
    return true;
  }
}
