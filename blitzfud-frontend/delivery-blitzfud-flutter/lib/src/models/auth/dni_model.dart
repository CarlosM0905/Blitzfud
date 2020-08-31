class DNI {
  String dni;
  String nombres;
  String apellidoPaterno;
  String apellidoMaterno;
  String codVerifica;

  DNI({
    this.dni,
    this.nombres,
    this.apellidoPaterno,
    this.apellidoMaterno,
    this.codVerifica,
  });

  DNI.fromJsonMap(Map<String, dynamic> json) {
    dni = json['dni'];
    nombres = json['nombres'];
    apellidoPaterno = json['apellidoPaterno'];
    apellidoMaterno = json['apellidoMaterno'];
    codVerifica = json['codVerifica'];
  }
}
