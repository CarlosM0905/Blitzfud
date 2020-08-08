/*===============> IMPORTACIONES <===============*/
import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

/*===============> SERVICIOS <===============*/
import { UserService } from './../user/user.service';
@Injectable({
  providedIn: 'root',
})
export class LoginGuard implements CanActivate {
  constructor(private userService: UserService,
              private router: Router) {}

  /**
   * Función que retorna un booleano, verifica si el usuario
   * se no se encuentre logeado, sino lo está, lo redireccionará a la página de login
   */
  canActivate(): boolean {
    if (this.userService.isLogged()) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}
