/*===============> IMPORTACIONES <===============*/
import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrls: ['./order-history.component.css'],
})
export class OrderHistoryComponent implements OnInit {

  // VARIABLES
  usuarios: any[] = [
    {
      nombre: 'Carlos Rodriguez',
      orden: '#0001',
      total: 'S/60.00',
      productos: '13 productos',
    },
    {
      nombre: 'Franz Carhuaricra',
      orden: '#0002',
      total: 'S/22.40',
      productos: '1 productos',
    },
    {
      nombre: 'David Albornoz',
      orden: '#0003',
      total: 'S/41.30',
      productos: '2 productos',
    },
    {
      nombre: 'Diego Tueros',
      orden: '#0004',
      total: 'S/95.00',
      productos: '5 productos',
    },
    {
      nombre: 'Alexander Soto',
      orden: '#0005',
      total: 'S/44.50',
      productos: '6 productos',
    },
    {
      nombre: 'Carlos Yaringao',
      orden: '#0006',
      total: 'S/35.00',
      productos: '4 productos',
    },
    {
      nombre: 'Carlos Yaringao',
      orden: '#0006',
      total: 'S/35.00',
      productos: '4 productos',
    },
    {
      nombre: 'Carlos Yaringao',
      orden: '#0006',
      total: 'S/35.00',
      productos: '4 productos',
    },
    {
      nombre: 'Carlos Yaringao',
      orden: '#0006',
      total: 'S/35.00',
      productos: '4 productos',
    },
  ];

  productos: any[] = [
    {
      producto: 'Leche Gloria en lata',
      cantidad: '400ml',
      unidades: '78un.',
    },
    {
      producto: 'Agua mineral San Mateo',
      cantidad: '450ml',
      unidades: '85un.',
    },
    {
      producto: 'Vodka',
      cantidad: '1L',
      unidades: '120un.',
    },
  ];

  constructor() {}

  ngOnInit(): void {}
}
