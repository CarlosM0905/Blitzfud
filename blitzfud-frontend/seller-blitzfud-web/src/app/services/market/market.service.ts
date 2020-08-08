/*===============> IMPORTACIONES <===============*/
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpHeaders, HttpClient } from '@angular/common/http';

/*===============> URL  <===============*/
import { SERVICES_URL } from './../../config/config';

/*===============> INTERFACES <===============*/
import { Market } from './../../interfaces/Market';

@Injectable({
  providedIn: 'root',
})
export class MarketService {
  constructor(public http: HttpClient) {}

  /**
   * Define los datos para la solicitud a la API (url, token y headers) y se retorna el observable que cree una tienda
   * @param market Datos de la tienda a crearse
   */
  createMarket(market: Market): Observable<any> {
    const url = SERVICES_URL + '/market';
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', token);

    let deliveryMethods = 'both';
    if (!market.pickup) {
      deliveryMethods = 'delivery';
    }
    if (!market.delivery) {
      deliveryMethods = 'pickup';
    }

    /**
     * Obtiene los datos para crear la tienda
     * Name: Nombre de la tienda
     * Description: Descripción de la tienda
     * DeliveryMethods: Metodo de entrega de sus productos
     * Location: Localización de la tienda, que tiene 2 valores (latitud y longitud)
     */
    const body = {
      name: market.name,
      description: market.description,
      deliveryMethods,
      deliveryPrice: market.deliveryPrice,
      location: [market.latitude, market.longitude],
    };
    return this.http.post(url, body, { headers });
  }

  /**
   * Define los datos para la solicitud a la API (url, token y headers) y se retorna el observable que obtenga una tienda
   */
  getMarket(): Observable<any>{
    const url = SERVICES_URL + '/market';
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', token);

    return this.http.get(url, {headers});
  }
}
