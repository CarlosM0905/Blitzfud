// IMPORTS
import 'package:blitzfud4delivery/src/models/exception/response_api_exception.dart';
import 'package:blitzfud4delivery/src/widgets/bg_login.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:progress_dialog/progress_dialog.dart';
// UTILS
import 'package:blitzfud4delivery/src/utils/blitzfud_colors.dart';
import 'package:blitzfud4delivery/src/utils/blitzfud_constants.dart';
import 'package:blitzfud4delivery/src/utils/blitzfud_preference.dart';
import 'package:blitzfud4delivery/src/utils/blitzfud_utils.dart';
// PROVIDERS
import 'package:blitzfud4delivery/src/providers/restapi/auth_provider.dart';

class LoginPage extends StatefulWidget {
  @override
  _LoginPageState createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  // PROGRESS DIALOG
  ProgressDialog _progressDialog;
  // VARIABLES
  bool _autoValidate = false;
  bool _obscureText = true;
  int _phoneNumbers = 0;
  // KEYS
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  // CONTROLLERS
  final _inputPhoneNumberController = TextEditingController();
  final _inputPasswordController = TextEditingController();
  // PROVIDERS
  final _authProvider = new AuthProvider();
  // PREFERENCES
  final _prefs = new UserPreferences();

  @override
  void dispose() {
    // Limpia el controlador cuando el Widget se descarte
    _inputPhoneNumberController.dispose();
    _inputPasswordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    _progressDialog = initProgressDialog(context);

    final phoneNumber = ModalRoute.of(context).settings.arguments;
    if (phoneNumber != null) {
      _inputPhoneNumberController.text = phoneNumber;
      _phoneNumbers = phoneNumber.toString().length;
    }

    return Scaffold(
        body: Stack(
      children: [LoginBackground(), _header(context), _content(context)],
    ));
  }

  // Cabecera de la página
  Widget _header(BuildContext context) {
    final size = MediaQuery.of(context).size;

    return Container(
      margin: EdgeInsets.only(top: size.height * 0.1),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              SvgPicture.asset(
                'assets/svgs/logo_white.svg',
                width: size.width * 0.06,
                height: size.height * 0.06,
              ),
              Container(
                margin: EdgeInsets.only(top: 8, left: 15),
                child: Text(
                  'Blitzfud',
                  style: TextStyle(
                      color: Colors.white, fontSize: size.height * 0.05),
                ),
              ),
            ],
          )
        ],
      ),
    );
  }

  // Contenido de la página
  Widget _content(BuildContext context) {
    final size = MediaQuery.of(context).size;

    return SingleChildScrollView(
      child: Column(
        children: <Widget>[
          SafeArea(
              child: Container(
            height: size.height * 0.2,
          )),
          _loginForm(context),
          SizedBox(height: size.height * 0.01),
          _buttonToRegister(context)
        ],
      ),
    );
  }

  // Formulario de inicio de sesion
  Widget _loginForm(BuildContext context) {
    final size = MediaQuery.of(context).size;

    return Container(
      width: size.width * 0.85,
      padding:
          EdgeInsets.only(top: size.height * 0.035, bottom: size.height * 0.05),
      decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(30.0),
          boxShadow: [
            BoxShadow(
                color: Colors.black26,
                blurRadius: 7.0,
                offset: Offset(0.0, 4.0),
                spreadRadius: 1.0)
          ]),
      child: Form(
        key: _formKey,
        autovalidate: _autoValidate,
        child: Column(
          children: <Widget>[
            Text('¡Bienvenido de vuelta!',
                style: TextStyle(
                    fontSize: size.height * 0.035,
                    color: COLOR_TEXT_INFORMATION)),
            SizedBox(height: size.height * 0.04),
            _inputPhoneNumber(size),
            SizedBox(height: size.height * 0.03),
            _inputPassword(size),
            SizedBox(height: size.height * 0.05),
            _submitButton(context),
          ],
        ),
      ),
    );
  }

  // Input numero de telefono
  Widget _inputPhoneNumber(Size size) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: size.height * 0.025),
      child: TextFormField(
        controller: _inputPhoneNumberController,
        style: TextStyle(fontSize: size.height * 0.025),
        keyboardType: TextInputType.number,
        maxLength: 9,
        decoration: InputDecoration(
            icon: Icon(Icons.phone_android, color: COLOR_PRIMARY),
            labelText: 'Número de celular',
            counterText: '$_phoneNumbers/9'),
        onChanged: (value) {
          setState(() {
            _phoneNumbers = value.length;
          });
        },
        validator: (String value) {
          if (value.isEmpty) {
            return 'Escriba su número';
          }

          return null;
        },
      ),
    );
  }

  // Input contraseña
  Widget _inputPassword(Size size) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: size.height * 0.025),
      child: Column(
        children: <Widget>[
          TextFormField(
            controller: _inputPasswordController,
            style: TextStyle(fontSize: size.height * 0.025),
            decoration: InputDecoration(
              icon: Icon(Icons.lock_outline, color: COLOR_PRIMARY),
              labelText: 'Contraseña',
              suffixIcon: InkWell(
                onTap: () => setState(() {
                  _obscureText = !_obscureText;
                }),
                child: Icon(Icons.remove_red_eye),
              ),
            ),
            obscureText: _obscureText,
            validator: (String value) {
              if (value.isEmpty) {
                return 'Escriba su contraseña';
              }

              return null;
            },
          ),
        ],
      ),
    );
  }

  // Boton iniciar sesion
  Widget _submitButton(BuildContext context) {
    final size = MediaQuery.of(context).size;

    void _validateInputs() {
      if (_formKey.currentState.validate()) {
        login();
      } else {
        setState(() {
          _autoValidate = true;
        });
      }
    }

    return RaisedButton(
      child: Container(
        padding:
            EdgeInsets.symmetric(horizontal: size.width * 0.15, vertical: 7.5),
        child: Text(
          'Entrar',
          style: TextStyle(
              fontSize: size.height * 0.025,
              fontWeight: FontWeight.w400,
              letterSpacing: 1.5),
        ),
      ),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15.0)),
      elevation: 0.0,
      color: COLOR_PRIMARY,
      textColor: Colors.white,
      onPressed: login,
    );
  }

  // Iniciar sesion
  void login() async {
    final phoneNumber = "+51${_inputPhoneNumberController.text}";
    final password = _inputPasswordController.text;

    try {
      await _progressDialog.show();

      final responseAPI = await _authProvider.login(phoneNumber, password);

      print('response: ${responseAPI.message}');
      await _progressDialog.hide();

      showToast(responseAPI.message);
      Navigator.pushNamedAndRemoveUntil(context, HOME_PAGE, (r) => false);
      // Navigator.pushReplacementNamed(context, HOME_PAGE);
    } on ResponseAPIException catch (e) {
      await _progressDialog.hide();
      print('custom excepton: $e');
      _showDialog(context, e.toString());
    } catch (e) {
      await _progressDialog.hide();
      print('Error durante el inicio de sesion:  $e');
      _showDialog(context, e.toString());
    }
  }

  // Botton ir al registro
  Widget _buttonToRegister(BuildContext context) {
    final size = MediaQuery.of(context).size;

    return Container(
      margin: EdgeInsets.only(top: size.height * 0.03),
      child: Column(
        children: [
          Container(
            padding: EdgeInsets.symmetric(
                horizontal: size.width * 0.15, vertical: 7.5),
            child: Text(
              '¿No tienes una cuenta?',
              style: TextStyle(
                  fontSize: size.height * 0.025,
                  fontWeight: FontWeight.w400,
                  letterSpacing: 1.5),
            ),
          ),
          RaisedButton(
            child: Text(
              'Registrate',
              style: TextStyle(
                  color: COLOR_PRIMARY,
                  fontWeight: FontWeight.w400,
                  fontSize: size.height * 0.025,
                  letterSpacing: 1.4),
            ),
            shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(15.0)),
            color: Colors.white,
            elevation: 0.0,
            onPressed: () =>
                Navigator.pushReplacementNamed(context, REGISTER_PAGE),
          ),
        ],
      ),
    );
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
