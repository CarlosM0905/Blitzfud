/*================ IMPORTACIONES ================*/
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

/*================ INTERFACES ================*/
import { User } from './../../interfaces/User';

/*================ SERVICIOS ================*/
import { UserService } from './../../services/service.index';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  /*================ VARIABLES ================*/
  formUser: FormGroup;
  user: User;
  passwordVisible = false;
  password?: string;

  /**
   * Define las variables de la clase
   * @param formBuilder Construye el formulario
   * @param userService Permite llamar a las funciones del servicios del usuario
   * @param router Permite realizar la navegación entre paginas
   */
  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private router: Router) {}

  /**
   * Llama a la funcion initializeFormUser
   * al iniciar la visualizacion de la pagina
   */
  ngOnInit(): void {
    this.initializeFormUser();
  }

  /**
   * Verifica si el correo es invalido y si el campo ha sido seleccionado
   * retornando un booleano
   */
  get emailInvalid(): boolean {
    return this.formUser.get('email').invalid && this.formUser.get('email').touched;
  }

  /**
   * Verifica si la contraseña es invalida y si el campo ha sido seleccionado
   * retornando un booleano
   */
  get passwordInvalid(): boolean{
    return this.formUser.get('password').invalid && this.formUser.get('password').touched;
  }

  /**
   * Inicializa el formulario del usuario al definir valores por
   * defecto y definir validaciones (campo obligatorio y formato de correo)
   */
  initializeFormUser(): void {
    this.formUser = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }
  /**
   * Recoge los datos del formulario, los guarda en "user"
   * llama al metodo login del formulario, este recibe los datos
   * retornando una respuesta que permitira definir a que ruta se
   * navegara (dashboard o register-market)
   */
  submitUserData(): void {
    this.user = {
      email: this.formUser.get('email').value,
      password: this.formUser.get('password').value,
    };
    this.userService.login(this.user)
    .then((response: any) => {
      console.log(response.haveMarket);
      if (response.haveMarket){
        this.router.navigate(['/dashboard']);
      }
      else{
        this.userService.saveUserData(this.user);
        this.router.navigate(['/register-market']);
      }
    }).catch((error) => {
      console.log(error);
    });
  }
}
