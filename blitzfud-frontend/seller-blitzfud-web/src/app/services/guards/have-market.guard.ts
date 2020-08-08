/*===============> IMPORTACIONES <===============*/
import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

/*===============> SERVICIOS <===============*/
import { UserService } from './../user/user.service';
@Injectable({
  providedIn: 'root',
})
export class HaveMarketGuard implements CanActivate {
  constructor(private router: Router, private userService: UserService) {}
  /**
   * Función que retorna un booleano, verificando si este tiene
   * o no una tienda, si no lo tiene lo redireccionará a la página register market
   */
  canActivate(): boolean {
    if (this.userService.haveMarket()) {
      return true;
    } else {
      this.router.navigate(['/register-market']);
      return false;
    }
  }
}
