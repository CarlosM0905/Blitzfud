/*===============> IMPORTACIONES <===============*/
import { Injectable, Injector } from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http';
import { Observable, pipe } from 'rxjs';
import { tap } from 'rxjs/operators';

/*===============> SERVICIOS <===============*/
import { LoaderService } from './loader.service';

@Injectable({
  providedIn: 'root',
})
export class LoaderInterceptorService implements HttpInterceptor {
  constructor(private loaderService: LoaderService) {}

  /**
   * Intercepta una petición http y retorna un observable que mostrará un evento en particular
   * @param req Define la petición http
   * @param next Da paso a un evento o funcionalidad continua
   */
  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    this.showLoader();

    return next.handle(req).pipe(
      tap(
        (event: HttpEvent<any>) => {
          if (event instanceof HttpResponse) {
            this.onEnd();
          }
        },
        (err: any) => {
          this.onEnd();
        }
      )
    );
  }

  /**
   * Evento disparado al final de la peticion HTTP
   */
  private onEnd(): void {
    this.hideLoader();
  }
  /**
   * Muestra la barra de carga
   */
  private showLoader(): void {
    this.loaderService.show();
  }
  /** Oculta la barra de carga */
  private hideLoader(): void {
    this.loaderService.hide();
  }
}
