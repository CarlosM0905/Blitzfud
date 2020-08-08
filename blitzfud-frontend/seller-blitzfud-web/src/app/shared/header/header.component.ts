/*=============== IMPORTACIONES ===============*/
import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import {Title} from '@angular/platform-browser';

/*=============== SERVICIOS ===============*/
import { UserService } from './../../services/user/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})

export class HeaderComponent implements OnInit {
  /*=============== VARIABLES ===============*/
  firstName: string;
  lastName: string;
  visible: boolean;
  header: string;

  /**
   * Define las variables de la clase
   * @param userService Permite llamar a las funciones del servicios del usuario
   * @param router Permite realizar la navegación entre paginas
   * @param titleService Servicio que hace referencia al título de la pagina web
   */
  constructor(private userService: UserService, private router: Router, private titleService: Title) {
    this.visible = false;
    const userData = JSON.parse(localStorage.getItem('user'));
    this.firstName = userData.firstName;
    this.lastName = userData.lastName;
    this.changeTitle();
  }

  ngOnInit(): void {
  }

  /**
   * Llama al servicio logout
   * para realizar el cierre de sesión
   */
  logout(): void {
    this.userService.logout();
  }

  change(value: boolean): void {
  }

  /**
   * Cambia el título del header
   * dependiendo de la ruta que reciba
   */
  changeTitle(): void{
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const route: string = event.url.split('/')[2];

        switch (route){
          case 'home':
            this.header = 'Inicio';
            this.titleService.setTitle('Inicio');
            break;
          case 'products':
            this.header = 'Productos';
            this.titleService.setTitle('Productos');
            break;
          case 'distributors':
            this.header = 'Repartidores';
            this.titleService.setTitle('Repartidores');
            break;
          case 'orders-pending':
            this.header = 'Órdenes';
            this.titleService.setTitle('Órdenes');
            break;
          case 'history':
            this.header = 'Historial';
            this.titleService.setTitle('Historial');
            break;
          case 'customers':
            this.header = 'Clientes';
            this.titleService.setTitle('Clientes');
            break;
          case 'edit-profile':
            this.header = 'Editar Perfil';
            this.titleService.setTitle('Perfil');
            break;
          default:
              this.header = 'Bienvenido';
              this.titleService.setTitle('Blitzfud');
        }
        }
  });
  }
}
