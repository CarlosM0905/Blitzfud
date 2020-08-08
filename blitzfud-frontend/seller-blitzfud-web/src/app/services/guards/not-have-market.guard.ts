/*===============> IMPORTACIONES <===============*/
import { Injectable } from '@angular/core';
import {
  CanActivate, Router,
} from '@angular/router';

/*===============> SERVICIOS <===============*/
import { UserService } from './../user/user.service';

@Injectable({
  providedIn: 'root',
})
export class NotHaveMarketGuard implements CanActivate {
  constructor(private router: Router, private userService: UserService) {}

  /**
   * Función que retorna un booleano, verifica si el usuario
   * tiene una tienda registrada, si lo tiene lo redireccionará a la página dashboard
   */
  canActivate(): boolean{
    if (this.userService.haveMarket()){
      this.router.navigate(['/dashboard']);
      return false;
    }
    else{
      return true;
    }
  }
}
