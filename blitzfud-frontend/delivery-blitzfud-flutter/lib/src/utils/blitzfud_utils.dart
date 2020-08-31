import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:progress_dialog/progress_dialog.dart';

import 'package:blitzfud4delivery/src/utils/blitzfud_colors.dart';

bool isSuccessfull(int statusCode) {
  return statusCode.toString().startsWith("2");
}

ProgressDialog initProgressDialog(BuildContext context) {
  final progressDialog = ProgressDialog(context, isDismissible: false);
  progressDialog.style(message: 'Espere un momento');

  return progressDialog;
}

void showToast(String msg) {
  Fluttertoast.showToast(
      msg: msg,
      toastLength: Toast.LENGTH_SHORT,
      backgroundColor: COLOR_PRIMARY,
      textColor: Colors.white);
}
