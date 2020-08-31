// IMPORTS
import 'package:flutter/material.dart';
import 'package:geocoder/geocoder.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:google_maps_webservice/places.dart';
import 'package:flutter_google_places/flutter_google_places.dart';
// UTILS
import 'package:blitzfud4delivery/src/utils/blitzfud_colors.dart';

class MapPage extends StatefulWidget {
  @override
  _MapPageState createState() => _MapPageState();
}

class _MapPageState extends State<MapPage> {
  // Variables
  bool fromPlaces = false;
  // Posicion por defecto
  LatLng position = new LatLng(-12.0460038, -77.0327398);
  String address = 'Ubicación';
  // Controladores
  GoogleMapController _googleMapController;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          icon: Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () => Navigator.of(context).pop(),
        ),
        backgroundColor: COLOR_PRIMARY,
        title: Text('Define tu ubicación'),
      ),
      body: Stack(
        alignment: AlignmentDirectional.bottomCenter,
        fit: StackFit.expand,
        children: <Widget>[
          _googleMap(),
          _inputSearch(),
          _markerCenter(),
          _confirmButton(),
        ],
      ),
    );
  }

  // Crear vista mapa de Google
  Widget _googleMap() {
    return GoogleMap(
      onMapCreated: (controller) {
        _googleMapController = controller;
      },
      onCameraMove: (position) {
        this.position = position.target;
      },
      onCameraIdle: () async {
        if (fromPlaces) {
          fromPlaces = false;
          return;
        }

        final coordinates =
            new Coordinates(this.position.latitude, this.position.longitude);
        var addresses =
            await Geocoder.local.findAddressesFromCoordinates(coordinates);
        var first = addresses.first;
        setState(() {
          this.address = first.addressLine;
        });
      },
      initialCameraPosition: CameraPosition(
        target: position,
        zoom: 14,
      ),
    );
  }

  // Crear input para buscar lugares
  Widget _inputSearch() {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        GestureDetector(
          onTap: _redirectToSearch,
          child: Container(
            width: double.infinity,
            margin: EdgeInsets.only(top: 12, left: 16, right: 16),
            height: 50,
            decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(20),
                boxShadow: <BoxShadow>[
                  BoxShadow(
                      color: Colors.black12,
                      blurRadius: 3.0,
                      offset: Offset(0.0, 5.0),
                      spreadRadius: 3.0)
                ]),
            child: Row(
              children: [
                Container(
                  margin: EdgeInsets.symmetric(horizontal: 16),
                  child: Icon(
                    Icons.search,
                    color: COLOR_PRIMARY,
                  ),
                ),
                Expanded(
                    child: Container(
                  margin: EdgeInsets.only(right: 16),
                  child: Text(
                    address,
                    style: TextStyle(fontSize: 18),
                    overflow: TextOverflow.ellipsis,
                  ),
                ))
              ],
            ),
          ),
        )
      ],
    );
  }

  // Marcador que indica la ubicación actual
  Widget _markerCenter() {
    return Center(
      child: Icon(
        Icons.location_on,
        color: COLOR_PRIMARY,
        size: 40,
      ),
    );
  }

  // Botón para confirmar la ubicación
  Widget _confirmButton() {
    return Positioned(
      bottom: 16,
      child: Container(
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            RaisedButton(
              padding: EdgeInsets.symmetric(horizontal: 32, vertical: 8),
              child: Text('Aceptar'),
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(20)),
              color: COLOR_PRIMARY,
              textColor: Colors.white,
              onPressed: () {
                Navigator.of(context).pop({
                  'positionLat': position.latitude,
                  'positionLng': position.longitude,
                  'address': address
                });
              },
            ),
          ],
        ),
      ),
    );
  }

  // Redirige a la página de búsqueda de lugares
  void _redirectToSearch() async {
    Prediction prediction = await PlacesAutocomplete.show(
        context: context,
        apiKey: '{api_key}',
        mode: Mode.fullscreen,
        language: "es",
        components: [Component(Component.country, "pe")]);

    GoogleMapsPlaces _places =
        new GoogleMapsPlaces(apiKey: '{api_key}');
    PlacesDetailsResponse detail =
        await _places.getDetailsByPlaceId(prediction.placeId);
    double latitude = detail.result.geometry.location.lat;
    double longitude = detail.result.geometry.location.lng;
    String address = prediction.description;
    setState(() {
      position = new LatLng(latitude, longitude);
      fromPlaces = true;
      this.address = address;
    });

    _googleMapController.moveCamera(CameraUpdate.newCameraPosition(
        CameraPosition(target: position, zoom: 14)));
  }
}
