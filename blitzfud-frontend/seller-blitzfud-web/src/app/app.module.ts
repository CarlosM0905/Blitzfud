/*================ IMPORTACIONES ================*/
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { environment } from 'src/environments/environment';

/*================ MODULOS ================*/
import { AuthModule } from './auth/auth.module';
import { PagesModule } from './pages/pages.module';
import { SharedModule } from './shared/shared.module';
import { NgZorroModule } from './ng-zorro/ng-zorro.module';
import { AgmCoreModule } from '@agm/core';
import { AppRoutingModule } from './app.routes';

/*================ COMPONENTES ================*/
import { AppComponent } from './app.component';

/*================ SERVICIOS ================*/
import { ServiceModule } from './services/service.module';

/*================ INTERCEPTORES ================*/
import { ErrorInterceptorService } from './interceptors/error-interceptor.service';
import { LoaderInterceptorService } from './services/loader/loader-interceptor.service';
@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    AppRoutingModule,
    AuthModule,
    SharedModule,
    PagesModule,
    NgZorroModule,
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    ServiceModule,
    AgmCoreModule.forRoot({
      apiKey: environment.MAPS_API_KEY,
      libraries: ['places']
    })
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: LoaderInterceptorService, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptorService, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
