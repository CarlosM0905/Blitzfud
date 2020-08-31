// IMPORTS
import 'package:blitzfud4delivery/src/models/exception/response_api_exception.dart';
import 'package:blitzfud4delivery/src/utils/blitzfud_utils.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:firebase_auth/firebase_auth.dart';
// PROVIDERS
import 'package:blitzfud4delivery/src/providers/firebase/phone_provider.dart';
import 'package:blitzfud4delivery/src/providers/restapi/auth_provider.dart';
// UTILS
import 'package:blitzfud4delivery/src/utils/blitzfud_colors.dart';
import 'package:blitzfud4delivery/src/utils/blitzfud_constants.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:progress_dialog/progress_dialog.dart';

class VerifyPhonePage extends StatefulWidget {
  // PROGRESS DIALOG
  @override
  _VerifyPhonePageState createState() => _VerifyPhonePageState();
}

class _VerifyPhonePageState extends State<VerifyPhonePage> {
  ProgressDialog _progressDialog;

  Map<String, dynamic> arguments;

  String verificationId;

  final _auth = FirebaseAuth.instance;

  final _codeController = TextEditingController();

  final _phoneNumberProvider = new PhoneNumberProvider();

  final _authProvider = new AuthProvider();

  Future<bool> signUpFirebase(String phone, BuildContext context) {
    _auth.verifyPhoneNumber(
      phoneNumber: phone,
      verificationCompleted: (phoneAuthCredential) async {
        setState(() {
          _codeController.text = phoneAuthCredential.smsCode;
        });
        _register(context, phoneAuthCredential);
      },
      verificationFailed: (error) {},
      codeSent: (verificationId, forceResendingToken) {
        this.verificationId = verificationId;
      },
      codeAutoRetrievalTimeout: (verificationId) {},
    );
  }

  @override
  Widget build(BuildContext context) {
    _progressDialog = initProgressDialog(context);
    arguments = ModalRoute.of(context).settings.arguments;

    signUpFirebase(arguments['phoneNumber'], context);

    return Scaffold(
      appBar: AppBar(
        backgroundColor: COLOR_PRIMARY,
        title: Text('Ya casi terminamos'),
        leading: IconButton(
          icon: Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      body: SingleChildScrollView(
        child: Column(
          children: <Widget>[
            Container(
              margin: EdgeInsets.symmetric(vertical: 12),
              child: Text(
                'Verifica tu número de celular',
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
              ),
            ),
            Container(
              margin: EdgeInsets.symmetric(horizontal: 50),
              child: Text(
                'Blitzfud te enviará un SMS para verificar tu número de celular',
                textAlign: TextAlign.center,
              ),
            ),
            Container(
              margin: EdgeInsets.symmetric(horizontal: 30, vertical: 8),
              child: TextField(
                controller: _codeController,
                keyboardType: TextInputType.phone,
                decoration: InputDecoration(
                  icon: Icon(Icons.vpn_key, color: COLOR_PRIMARY),
                  labelText: 'Código',
                ),
                onChanged: (value) {},
              ),
            ),
            Container(
              margin: EdgeInsets.only(top: 8, bottom: 32),
              child: RaisedButton(
                padding: EdgeInsets.symmetric(horizontal: 60, vertical: 8),
                child: Text('Verificar'),
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(20)),
                color: COLOR_PRIMARY,
                textColor: Colors.white,
                onPressed: () => _verifyPhoneNumber(context),
              ),
            ),
            Container(
                margin:
                    EdgeInsets.only(top: 12, left: 16, right: 16, bottom: 32),
                child: SvgPicture.asset(
                  'assets/svgs/verify_phone.svg',
                ))
          ],
        ),
      ),
    );
  }

  void _verifyPhoneNumber(BuildContext context) async {
    AuthCredential credential = PhoneAuthProvider.credential(
        verificationId: verificationId, smsCode: _codeController.text);

    _register(context, credential);
  }

  void _register(BuildContext context, AuthCredential credential) async {
    try {
      _progressDialog.show();
      await _auth.signInWithCredential(credential);

      await _auth.signOut();

      await _phoneNumberProvider.create(arguments['phoneNumber']);

      await _authProvider.register(
          arguments['nationalIdentityNumber'],
          arguments['phoneNumber'],
          arguments['password'],
          arguments['firstName'],
          arguments['lastName'],
          arguments['locationLat'],
          arguments['locationLng'],
          arguments['address']);

      final String phoneNumber = arguments['phoneNumber'];

      await _progressDialog.hide();

      showToast('Ya puedes iniciar sesión');
      Navigator.pushNamedAndRemoveUntil(context, LOGIN_PAGE, (r) => false,
          arguments: phoneNumber.substring(3));
      // Navigator.pushReplacementNamed(context, LOGIN_PAGE,
      //     arguments: phoneNumber.substring(3));
    } on ResponseAPIException catch (e) {
      await _progressDialog.hide();
      print('custom excepton: $e');
    } catch (e) {
      await _progressDialog.hide();
      print('Error: $e');
      _showDialog(context, e.toString());
    }
  }

  void _showDialog(BuildContext context, String message) {
    showDialog(
        context: context,
        builder: (_) => new AlertDialog(
              title: new Text("Vaya..."),
              content: new Text(message),
              actions: <Widget>[
                FlatButton(
                  child: Text('Ok'),
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                )
              ],
            ));
  }
}
