// IMPORTS
import 'package:blitzfud4delivery/src/utils/blitzfud_utils.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
// PROVIDERS
import 'package:blitzfud4delivery/src/providers/firebase/phone_provider.dart';
import 'package:blitzfud4delivery/src/providers/reniecapi/dni_provider.dart';
// UTILS
import 'package:blitzfud4delivery/src/utils/blitzfud_colors.dart';
import 'package:blitzfud4delivery/src/utils/blitzfud_constants.dart';
// MODELS
import 'package:blitzfud4delivery/src/models/auth/dni_model.dart';
// WIDGETS
import 'package:blitzfud4delivery/src/widgets/bg_register.dart';
// PAGES
import 'package:blitzfud4delivery/src/pages/map/map_page.dart';
import 'package:progress_dialog/progress_dialog.dart';

class RegisterPage extends StatefulWidget {
  @override
  _RegisterPageState createState() => _RegisterPageState();
}

class _RegisterPageState extends State<RegisterPage> {
  // PROGRESS DIALOG
  ProgressDialog _progressDialog;
  // VARIABLES
  bool _obscureText = true;
  bool _cargando = false;
  bool _autoValidate = false;
  int _phoneNumbers = 0;
  int _dniNumbers = 0;
  LatLng position;
  String address = "Selecciona una ubicación";
  String firstName;
  String lastName;
  // KEYS
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  final GlobalKey<ScaffoldState> _scaffoldKey = new GlobalKey<ScaffoldState>();
  // PROVIDERS
  final dniProvider = new DNIProvider();
  final _phoneNumberProvider = new PhoneNumberProvider();
  final snackBar = SnackBar(content: Text('Ups! DNI no encontrado'));
  // CONTROLLERS
  TextEditingController _inputDNIController = new TextEditingController();
  TextEditingController _inputNameController = new TextEditingController();
  TextEditingController _phoneController = TextEditingController();
  TextEditingController _ubicationController = TextEditingController();
  TextEditingController _inputPasswordController = TextEditingController();

  @override
  void dispose() {
    // Limpieza de controladores
    _inputDNIController.dispose();
    _inputNameController.dispose();
    _phoneController.dispose();
    _ubicationController.dispose();
    _inputPasswordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    _progressDialog = initProgressDialog(context);
    _ubicationController.text = address;
    return Scaffold(
      key: _scaffoldKey,
      body: Stack(
        children: <Widget>[RegisterBackground(), _header(), _content(context)],
      ),
    );
  }

  // Cabecera de la pagina
  Widget _header() {
    return Container(
      margin: EdgeInsets.only(top: 12),
      width: double.infinity,
      height: 100,
      child: Center(
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            SvgPicture.asset('assets/svgs/logo_white.svg'),
            Container(
              margin: EdgeInsets.only(left: 8, top: 8),
              child: Text('Blitzfud',
                  style: TextStyle(color: Colors.white, fontSize: 36)),
            ),
          ],
        ),
      ),
    );
  }

  // Contenido de la pagina
  Widget _content(BuildContext context) {
    return SingleChildScrollView(
      child: Column(
        children: <Widget>[
          _registerForm(context),
          _footer(),
        ],
      ),
    );
  }

  // Formulario de registro
  Widget _registerForm(BuildContext context) {
    return Container(
      margin: EdgeInsets.only(top: 110, left: 20, right: 20, bottom: 20),
      padding: EdgeInsets.symmetric(vertical: 20),
      decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(20),
          boxShadow: <BoxShadow>[
            BoxShadow(
                color: Colors.black26,
                blurRadius: 3.0,
                offset: Offset(0.0, 5.0),
                spreadRadius: 3.0)
          ]),
      child: Form(
        key: _formKey,
        autovalidate: _autoValidate,
        child: Column(
          children: <Widget>[
            Text('Create una cuenta', style: TextStyle(fontSize: 20.0)),
            _inputDNI(),
            _inputPhone(context),
            _inputFullName(),
            _inputUbication(),
            _inputPassword(),
            _submitButton()
          ],
        ),
      ),
    );
  }

  // Input DNI
  Widget _inputDNI() {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 20.0),
      child: Row(children: [
        Expanded(
          flex: 8,
          child: TextFormField(
            maxLength: 8,
            controller: _inputDNIController,
            keyboardType: TextInputType.phone,
            decoration: InputDecoration(
              icon: Icon(Icons.credit_card, color: COLOR_PRIMARY),
              labelText: 'DNI',
              counterText: '$_dniNumbers/8',
            ),
            onChanged: (value) {
              setState(() {
                _dniNumbers = value.length;
              });
            },
            validator: (String value) {
              if (value.isEmpty) {
                return 'Escriba su DNI';
              } else if (value.length > 9) {
                return 'DNI invalido';
              } else {
                return null;
              }
            },
          ),
        ),
        Expanded(
          flex: 2,
          child: RawMaterialButton(
              child: (!_cargando)
                  ? Icon(
                      Icons.find_replace,
                      color: COLOR_PRIMARY,
                    )
                  : Container(
                      width: 25.0,
                      height: 25.0,
                      child: CircularProgressIndicator(
                          valueColor:
                              new AlwaysStoppedAnimation<Color>(COLOR_PRIMARY)),
                    ),
              shape: CircleBorder(),
              padding: EdgeInsets.all(10.0),
              onPressed: () {
                FocusScope.of(context).requestFocus(new FocusNode());
                setState(() {
                  _cargando = true;
                });
                dniProvider.getDNI(_inputDNIController.text).then((value) {
                  if (value == null) {
                    setState(() {
                      _cargando = false;
                    });
                    _scaffoldKey.currentState.showSnackBar(snackBar);
                  } else {
                    _inputNameController.text = _linkNameLastName(value);
                    setState(() {
                      _cargando = false;
                    });
                  }
                });
              }),
        )
      ]),
    );
  }

  // Input telefono
  Widget _inputPhone(BuildContext context) {
    final width = MediaQuery.of(context).size.width;

    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        _flag(context),
        Text('+51'),
        Container(
          padding: EdgeInsets.symmetric(horizontal: 20.0),
          width: width * 0.7,
          child: TextFormField(
            controller: _phoneController,
            keyboardType: TextInputType.phone,
            maxLength: 9,
            decoration: InputDecoration(
                labelText: 'Número de celular',
                counterText: '$_phoneNumbers/9'),
            onChanged: (value) {
              setState(() {
                _phoneNumbers = value.length;
              });
            },
            validator: (String value) {
              if (value.isEmpty) {
                return 'Escriba su DNI';
              }

              return null;
            },
          ),
        ),
      ],
    );
  }

  // Imagen bandera
  Widget _flag(BuildContext context) {
    final width = MediaQuery.of(context).size.width;
    return Container(
      padding: EdgeInsets.all(5),
      width: width * 0.1,
      child: SvgPicture.asset(
        'assets/svgs/peru.svg',
        height: width * 0.05,
      ),
    );
  }

  // Input nombre completo (deshabilitado)
  Widget _inputFullName() {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 20.0),
      child: TextFormField(
        controller: _inputNameController,
        readOnly: true,
        keyboardType: TextInputType.emailAddress,
        decoration: InputDecoration(
          icon: Icon(Icons.person, color: COLOR_PRIMARY),
          labelText: 'Nombre y apellidos',
        ),
      ),
    );
  }

  // Input ubicacion (deshabilitado)
  Widget _inputUbication() {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 20.0),
      child: TextFormField(
        controller: _ubicationController,
        keyboardType: TextInputType.emailAddress,
        decoration: InputDecoration(
          icon: Icon(Icons.location_on, color: COLOR_PRIMARY),
          labelText: 'Ubicación',
        ),
        readOnly: true,
        onTap: () async {
          final results = await Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => MapPage()),
          );

          if (results != null) {
            position =
                new LatLng(results['positionLat'], results['positionLng']);
            setState(() {
              address = results['address'];
              _ubicationController.text = address;
            });
          }
        },
      ),
    );
  }

  // Input contraseña
  Widget _inputPassword() {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 20.0),
      child: TextFormField(
        controller: _inputPasswordController,
        obscureText: _obscureText,
        decoration: InputDecoration(
          labelText: 'Contraseña',
          icon: Icon(Icons.lock_outline, color: COLOR_PRIMARY),
          suffixIcon: IconButton(
            icon: Icon(Icons.remove_red_eye, color: COLOR_PRIMARY),
            onPressed: () {
              setState(() {
                _obscureText = !_obscureText;
              });
            },
          ),
        ),
        validator: (String value) {
          if (value.isEmpty) {
            return 'Escriba su contraseña';
          }

          return null;
        },
      ),
    );
  }

  // Boton crear usuario
  Widget _submitButton() {
    void _validateInputs() {
      if (_formKey.currentState.validate()) {
        _validatePhone();
      } else {
        setState(() {
          _autoValidate = true;
        });
      }
    }

    return Container(
      margin: EdgeInsets.only(top: 60, bottom: 40),
      child: RaisedButton(
          padding: EdgeInsets.symmetric(horizontal: 80, vertical: 15),
          child: Text('Registrarme'),
          shape:
              RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
          color: COLOR_PRIMARY,
          textColor: Colors.white,
          onPressed: () {
            _validateInputs();
          }),
    );
  }

  // Pie de la pagina
  Widget _footer() {
    return Container(
      margin: EdgeInsets.only(top: 100, left: 12),
      width: double.infinity,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.end,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text('¿Ya tienes una cuenta?', style: TextStyle(color: Colors.white)),
          Container(
            margin: EdgeInsets.only(bottom: 8, top: 4),
            child: GestureDetector(
              onTap: () {
                Navigator.pushReplacementNamed(context, LOGIN_PAGE);
              },
              child: Text(
                'Inicia Sesión',
                style: TextStyle(
                    fontWeight: FontWeight.bold,
                    color: Colors.white,
                    fontSize: 18),
              ),
            ),
          )
        ],
      ),
    );
  }

  // Funcion validar telefono
  void _validatePhone() async {
    final phoneNumber = "+51${_phoneController.text}";

    await _progressDialog.show();
    bool exists = await _phoneNumberProvider.exists(phoneNumber);
    await _progressDialog.hide();

    if (exists) {
      showDialog(
          context: context,
          builder: (_) => new AlertDialog(
                title: new Text("Vaya..."),
                content: new Text("El número ya está tomado"),
                actions: <Widget>[
                  FlatButton(
                    child: Text('Ok'),
                    onPressed: () {
                      Navigator.of(context).pop();
                    },
                  )
                ],
              ));
    } else {
      final arguments = {
        'nationalIdentityNumber': _inputDNIController.text,
        'phoneNumber': phoneNumber,
        'password': _inputPasswordController.text,
        'firstName': firstName,
        'lastName': lastName,
        'locationLat': position.latitude,
        'locationLng': position.longitude,
        'address': address
      };

      Navigator.pushNamed(context, VERIFY_PHONE_PAGE, arguments: arguments);
    }
  }

  String _linkNameLastName(DNI dni) {
    firstName = dni.nombres;
    lastName = '${dni.apellidoPaterno} ${dni.apellidoMaterno}';
    return '${dni.nombres} ${dni.apellidoPaterno} ${dni.apellidoMaterno}';
  }
}
