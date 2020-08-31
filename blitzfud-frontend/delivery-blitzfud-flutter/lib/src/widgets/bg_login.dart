import 'package:blitzfud4delivery/src/utils/blitzfud_colors.dart';
import 'package:flutter/material.dart';

class LoginBackground extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      height: double.infinity,
      width: double.infinity,
      child: CustomPaint(
        painter: _LoginBackgroundPainter(),
      ),
    );
  }
}

// Pintador del fondo de la pagina
class _LoginBackgroundPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final pencil = Paint();
    // Propiedades
    pencil.color = COLOR_PRIMARY;
    pencil.style = PaintingStyle.fill;
    pencil.strokeWidth = 10;
    final path = new Path();
    path.lineTo(0, size.height * 0.6);
    path.quadraticBezierTo(
        size.width * 0.5, size.height * 0.70, size.width, size.height * 0.6);
    path.lineTo(size.width, 0);
    canvas.drawPath(path, pencil);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) {
    return true;
  }
}
