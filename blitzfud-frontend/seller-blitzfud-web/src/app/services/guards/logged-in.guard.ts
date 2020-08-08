/*===============> IMPORTACIONES <===============*/
import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

/*===============> SERVICIOS <===============*/
import { UserService } from './../user/user.service';
@Injectable({
  providedIn: 'root'
})
export class LoggedInGuard implements CanActivate {
  constructor(private router: Router,
              private userService: UserService){}
  /**
   * Función que retorna un booleano, verifica si el usuario se encuentra logeado
   */
  canActivate(): boolean{
      if (this.userService.isLogged()){
        this.router.navigate(['/dashboard']);
        return false;
      }
      else{
        return true;
      }
  }
}
