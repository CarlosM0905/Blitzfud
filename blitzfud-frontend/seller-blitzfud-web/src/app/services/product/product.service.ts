/*===============> IMPORTACIONES <===============*/
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpHeaders, HttpClient } from '@angular/common/http';

/*===============> URL <===============*/
import { SERVICES_URL } from './../../config/config';

/*===============> INTERFACES <===============*/
import { Product } from './../../interfaces/Product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(public http: HttpClient) { }

  /**
   * Define los datos para la solicitud a la API (url, token y headers)
   * y se retorna el observable que obtendrá todas las categorias de la tienda
   */
  getCategories(): Observable<any>{
    const url = SERVICES_URL + '/categories';
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', token);

    return this.http.get(url, {headers});
  }

  /**
   * Define los datos para la solicitud a la API (url, token y headers) y se retorna el observable que obtendra los productos de la tienda
   */
  getProducts(): Observable<any>{
    const url = SERVICES_URL + '/products';
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', token);

    return this.http.get(url, {headers});
  }

  /**
   * Define los datos para la solicitud a la API (url, token y headers) y se retorna el observable que creará un producto de la tienda
   * @param product Objeto producto
   */
  createProduct(product: Product): Observable<any>{
    const url = SERVICES_URL + '/products';
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', token);
    /**
     * Obtiene los datos para crear un producto
     * name: Nombre del producto
     * unitOfMeasurement: Unidad de medida del producto
     * content: Contenido del producto
     * maxQuantityPerOrder: Cantidad maxima por producto
     * price: Precio del producto
     * highlight: Resaltar producto
     * category: Categoria del producto
     */
    const body = {
      name: product.name,
      unitOfMeasurement: product.unitOfMeasurement,
      content: product.contentOfProduct,
      maxQuantityPerOrder: product.maxQuantity,
      price: product.price,
      highlight: product.highlight,
      category: product.category
    };

    return this.http.post(url, body, {headers});
  }

  /**
   * Define los datos para la solicitud a la API (url, token y headers) y se retorna el observable que actualizará el producto de la tienda
   * @param dataToEdit Datos que se actualizará del producto
   * @param productId ID del producto
   */
  updateProduct(dataToEdit: any, productId: string): Observable<any>{
    const url = SERVICES_URL + `/products/${productId}`;
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.patch(url, dataToEdit, {headers});
  }

  /**
   * Define los datos para la solicitud a la API (url, token y headers) y se retorna el observable que eliminará el producto de la tienda
   * @param productId ID del producto
   */
  deleteProduct(productId: string): Observable<any>{
    const url = SERVICES_URL + `/products/${productId}`;
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', token);

    return this.http.delete(url, {headers});
  }
}
