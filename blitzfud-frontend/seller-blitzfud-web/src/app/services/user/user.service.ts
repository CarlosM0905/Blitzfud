/*===============> IMPORTACIONES <===============*/
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { map } from 'rxjs/operators';
import {Title} from '@angular/platform-browser';


/*===============> URL <===============*/
import { SERVICES_URL } from './../../config/config';

/*===============> INTERFACES <===============*/
import { Seller } from './../../interfaces/Seller';
import { User } from './../../interfaces/User';
import { Market } from './../../interfaces/Market';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  email: string;
  password: string;

  constructor(public http: HttpClient,
              public router: Router,
              private title: Title) {}
  /**
   * Define los datos para la solicitud a la API (url) y retorna el observable que registrará un vendedor
   * @param seller Datos del vendedor que se va a crear
   */
  registerSeller(seller: Seller): Observable<any> {
    const url = SERVICES_URL + '/auth/signup';

    /**
     * Obtiene los datos para crear un vendedor
     * email: Correo del vendedor
     * password: Contraseña del vendedor
     * firstName: Nombre del vendedor
     * lastName: Apellido del vendedor
     */
    const body = {
      email: seller.email,
      password: seller.password,
      firstName: seller.first_name,
      lastName: seller.last_name,
    };

    return this.http.post(url, body);
  }

  /**
   * Define los datos para la solicitud a la API (url) y retorna el observable que realizará el login de un usuario
   * @param user Datos del usuario que va a logearse
   */
  async login(user: User): Promise<any> {
    const urlAuth = SERVICES_URL + '/auth/signin';
    const urlMarket = SERVICES_URL + '/market';
    /**
     * Obtiene los datos para el login
     * email: Correo del usuario
     * password: Correo del usuario
     */
    const body = {
      email: user.email,
      password: user.password,
    };

    const userData: any = await this.http.post(urlAuth , body).toPromise();
    const token = userData.token;
    const headers = new HttpHeaders().set('Authorization', token);

    const marketData: any = await this.http.get(urlMarket, {headers}).toPromise()
    .catch(err => {
      // No tienes una tienda
      console.log(err);
    });

    const haveMarket = marketData ? true : false;
    const response = {
      user: userData.user,
      token: userData.token,
      haveMarket
    };
    this.chargeLocalStorage(response);
    return response;
  }

  /**
   * Guarda los datos del usuario
   * @param user Datos del usuario(email, password)
   */
  saveUserData(user: User): void {
    this.email = user.email;
    this.password = user.password;
  }

  /**
   * Obtiene los datos del usuario
   * @param user Datos del usuario(email, password)
   */
  getUserData(): User {
    return {
      email: this.email,
      password: this.password,
    };
  }

  /**
   * Elimina los datos del usuario, reemplazando los valores con un null
   */
  deleteUserData(): void {
    this.email = null;
    this.password = null;
  }

  /**
   * Carga los datos del usuario en el LocalStorage
   * @param response Estado de la carga de datos del LocalStorage
   */
  chargeLocalStorage(response: any): void {
    if (response.haveMarket) {
      localStorage.setItem('token', response.token);
      localStorage.setItem('user', JSON.stringify(response.user));
      localStorage.setItem('haveMarket', JSON.stringify(response.haveMarket));
    } else {
      localStorage.setItem('token', response.token);
      localStorage.setItem('user', JSON.stringify(response.user));
    }
  }

  /**
   * Retorna un booleano verificando si el usuario esta logeado o no
   */
  isLogged(): boolean {
    const token: string  = localStorage.getItem('token') ? localStorage.getItem('token') : '' ;
    return token.length > 5 ? true : false;
  }

  /**
   * Retorna un booleano que verifica si el usuario tiene tienda o no
   */
  haveMarket(): boolean{
    const haveMarket: boolean = JSON.parse(localStorage.getItem('haveMarket'));
    if (haveMarket){
     return true;
   }
   else{
     return false;
   }
  }

  /**
   * Deslogear al usuario, eliminando sus datos del LocalStorage, modificando el título y redirecionarlo a la página de login
   */
  logout(): void{
    localStorage.clear();
    this.deleteUserData();
    this.title.setTitle('Blitzfud');
    this.router.navigate(['/login']);
  }
}
