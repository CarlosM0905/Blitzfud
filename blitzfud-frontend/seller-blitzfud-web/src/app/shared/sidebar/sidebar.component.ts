/*=============== IMPORTACIONES ===============*/
import { Component, OnInit, Output, EventEmitter } from '@angular/core';

/*=============== SERVICIOS ===============*/
import { UserService } from './../../services/user/user.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})

export class SidebarComponent implements OnInit {

  constructor(private userService: UserService) { }

  ngOnInit(): void {
  }

 /**
  * Llama al servicio logout
  * para realizar el cierre de sesión
  */
  logout(): void{
    this.userService.logout();
  }

}
