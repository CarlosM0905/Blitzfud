// IMPORTS
import 'package:flutter/material.dart';
// UTILS
import 'package:blitzfud4delivery/src/utils/blitzfud_colors.dart';

class RegisterBackground extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      height: double.infinity,
      width: double.infinity,
      child: CustomPaint(
        painter: _RegisterBackgroundPainter(),
      ),
    );
  }
}

class _RegisterBackgroundPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final paint = new Paint();
    final pathTop = new Path();
    final pathBottom = new Path();

    final heightTop = 125.0;
    final heigthBottomLeft = 70.0;
    final heigthBottomRight = 12.0;

    paint.color = COLOR_PRIMARY;
    paint.style = PaintingStyle.fill;

    pathTop.lineTo(0, heightTop);
    pathTop.quadraticBezierTo(
        size.width * 0.5, heightTop + 50.0, size.width, heightTop);
    pathTop.lineTo(size.width, 0);

    pathBottom.moveTo(0, size.height);
    pathBottom.lineTo(0, size.height - heigthBottomLeft);
    pathBottom.quadraticBezierTo(
        size.width * 0.6,
        size.height - heigthBottomLeft - 60.0,
        size.width,
        size.height - heigthBottomRight);
    pathBottom.lineTo(size.width, size.height);

    canvas.drawPath(pathTop, paint);
    canvas.drawPath(pathBottom, paint);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) {
    return true;
  }
}
