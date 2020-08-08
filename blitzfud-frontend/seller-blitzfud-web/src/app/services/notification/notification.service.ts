/*===============> IMPORTACIONES <===============*/
import { Injectable } from '@angular/core';
import {
  NzNotificationPlacement,
  NzNotificationService,
} from 'ng-zorro-antd/notification';

@Injectable({
  providedIn: 'root',
})

export class NotificationService {
  constructor(private notification: NzNotificationService) {}

  /**
   * Crea una notificación básica
   * Titulo: titulo de la notificación 'Advertencia!'
   * @param position Posición de la notificación 'bottomRight'
   * @param message Descripción de la notificación
   */
  createBasicNotification(
    position: NzNotificationPlacement,
    message: string
  ): void {
    this.notification.blank('Advertencia!', message, { nzPlacement: position });
  }

  /**
   * Crea una notificación con un icono
   * @param type Tipo de notificación 'success', 'error', 'info'
   * @param title Titulo de la notificación
   * @param message Descripcion de la notificación
   * @param position Posición de la notificación 'bottomRight'
   */
  createIconNotification(
    type: string,
    title: string,
    message: string,
    position: NzNotificationPlacement
  ): void {
    this.notification.create(type, title, message, {
      nzPlacement: position,
    });
  }
}
