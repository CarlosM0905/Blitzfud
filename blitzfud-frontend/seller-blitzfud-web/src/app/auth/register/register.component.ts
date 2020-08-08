/*================ IMPORTACIONES ================*/
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

/*================ INTERFACES ================*/
import { Seller } from './../../interfaces/Seller';

/*================ SERVICIOS ================*/
import { UserService } from './../../services/service.index';
import { NotificationService } from './../../services/service.index';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  /*================ VARIABLES ================*/
  formSeller: FormGroup;
  seller: Seller;
  passwordVisible = false;
  password?: string;
  passwordRepeatVisible = false;
  passwordRepeat?: string;

  /**
   * Defin variables a usarse en la clase
   * @param formBuilder Construye los formularios
   * @param userService Llama a los servicios de un usuario
   * @param router Permite navegar entre rutas
   * @param notificationService Muestra tipos de notificaciones
   */
  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private router: Router,
    private notificationService: NotificationService
  ) {}

  /**
   * Llama a la funcion initializeFormUser
   * al iniciar la visualizacion de la pagina
   */
  ngOnInit(): void {
    this.initializeFormSeller();
  }
  /**
   * Verifica si el nombre es invalido y si el campo
   * ha sido seleccionado retornando un booleano
   */
  get firstNameInvalid(): boolean {
    return (
      this.formSeller.get('first_name').invalid &&
      this.formSeller.get('first_name').touched
    );
  }

  /**
   * Verifica si el apellido es invalido y si el campo
   * ha sido seleccionado retornando un booleano
   */
  get lastNameInvalid(): boolean {
    return (
      this.formSeller.get('last_name').invalid &&
      this.formSeller.get('last_name').touched
    );
  }

  /**
   * Verifica si el correo es invalido y si el campo
   * ha sido seleccionado retornando un booleano
   */
  get emailInvalid(): boolean {
    return (
      this.formSeller.get('email').invalid &&
      this.formSeller.get('email').touched
    );
  }

  /**
   * Verifica si la contraseña es invalida y si el campo
   * ha sido seleccionado retornando un booleano
   */
  get passwordInvalid(): boolean {
    return (
      this.formSeller.get('password').invalid &&
      this.formSeller.get('password').touched
    );
  }

  /**
   * Crea una validacion para verificar si las contraseñas son iguales
   * retornando un booleano
   */
  get passwordRepeatInvalid(): boolean {
    const password = this.formSeller.get('password').value;
    const passwordRepeat = this.formSeller.get('passwordRepeat').value;
    return password === passwordRepeat ? false : true;
  }

  /**
   * Inicializa el formulario del vendedor con valores por defecto
   * establece validaciones basicas (tamalo minimo, campos obligatorios, formato de correo)
   * y establece la validacion personalizada (contraseñas iguales)
   */
  initializeFormSeller(): void {
    this.formSeller = this.formBuilder.group(
      {
        first_name: ['', [Validators.required, Validators.minLength(5)]],
        last_name: ['', [Validators.required, Validators.minLength(5)]],
        email: ['', [Validators.required, Validators.email]],
        password: ['', Validators.required],
        passwordRepeat: ['', [Validators.required]],
      },
      { validators: this.inputMatch('password', 'passwordRepeat') }
    );
  }

  /**
   * Si el formulario es valido, se guardan los datos del vendedor
   * en la variable "seller", se llama al servicio para registrarlo.
   * Se le notifica y redirecciona al login de ser exitoso el registro
   */
  submitSellerData(): void {
    if (this.formSeller.valid) {
      this.seller = {
        first_name: this.formSeller.get('first_name').value,
        last_name: this.formSeller.get('last_name').value,
        email: this.formSeller.get('email').value,
        password: this.formSeller.get('password').value,
      };

      this.userService
        .registerSeller(this.seller)
        .toPromise()
        .then((resp) => {
          this.notificationService.createIconNotification('success', '¡Bienvenido a Blitzfud!', 'Ingresa con tu cuenta para comenzar :)', 'bottomRight');
          this.router.navigate(['/login']);
        })
        .catch((e) => {
          console.log(e);
        });
    } else {
      console.log('Registro invalido');
    }
  }

  /**
   * Verifica que dos elementos de tipo cadena de un formulario sean iguales
   * @param value1 Cadena uno
   * @param value2  Cadena dos
   */
  inputMatch(value1: string, value2: string): any {
    return (group: FormGroup) => {
      const pass1 = group.controls[value1].value;
      const pass2 = group.controls[value2].value;

      if (pass1 === pass2) {
        return null;
      }

      return {
        inputMatch: true,
      };
    };
  }
}
