import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { NotificationService } from '../services/notification/notification.service';


@Injectable({
  providedIn: 'root'
})
export class ErrorInterceptorService implements HttpInterceptor{

  constructor(public notificationService: NotificationService) { }
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        console.log(error);
        if (error.status === 401){
          this.notificationService.createIconNotification('error', 'No puedes ingresar :(', error.error.message, 'bottomRight');
        }
        else if (error.status === 409){
          this.notificationService.createIconNotification('error', 'Ha sucedido un error', error.error.message, 'bottomRight');
        }
        else if (error.status === 0){
          this.notificationService.createIconNotification('error', 'Ha sucedido un error', 'Sin conexión a internet', 'bottomRight');
        }
        else if (error.status === 500 || error.status === 501){
          this.notificationService.createIconNotification('error', 'Ha sucedido un error', 'Error del servidor', 'bottomRight');
        }
        return throwError(error);
      })
    );
  }

}
