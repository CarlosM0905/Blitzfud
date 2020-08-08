/*=============== IMPORTACIONES ===============*/
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

/*=============== SERVICIOS ===============*/
import { LoaderService } from '../../services/service.index';

/*=============== INTERFACES ===============*/
import { LoaderState } from './../../interfaces/Loader';


@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.css']
})
export class LoaderComponent implements OnInit, OnDestroy {

  /*=============== VARIABLES ===============*/
  show = false;
  private subscription: Subscription;


  constructor(private loaderService: LoaderService) { }

  /**
   * Mostrara el loader
   * mientras se este sucrito a la notificación
   */
  ngOnInit(): void {
    this.subscription = this.loaderService.loaderState
    .subscribe((state: LoaderState) => {
      this.show = state.show;
    });
  }

  /**
   * Elimina el componente
   * cuando se desuscriba de la notificación
   */
  ngOnDestroy(): void{
    this.subscription.unsubscribe();
  }

}
