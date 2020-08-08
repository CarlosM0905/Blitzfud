/*===============> IMPORTACIONES <===============*/
import { Component, OnInit, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-pending-orders',
  templateUrl: './pending-orders.component.html',
  styleUrls: ['./pending-orders.component.css']
})

export class PendingOrdersComponent implements OnInit {

  // FAKE DATA
  delivery_orders: any[] = [
    {
      client_name: 'Franz Carhuaricra',
      order_number: '#0001',
      time_since_ordered: '5 min',
      amount: '43.60',
      quantity_products: '5'
    },
    {
      client_name: 'Carlos Rodriguez',
      order_number: '#0002',
      time_since_ordered: '7 min',
      amount: '27.30',
      quantity_products: '4'
    },
    {
      client_name: 'Diego Tueros',
      order_number: '#0003',
      time_since_ordered: '11 min',
      amount: '98.20',
      quantity_products: '15'
    },
    {
      client_name: 'David Albornoz',
      order_number: '#0004',
      time_since_ordered: '19 min',
      amount: '27.90',
      quantity_products: '5'
    },
  ]

  market_orders: any[] = [
    {
      client_name: 'Carlos Yaringaño',
      order_number: '#0005',
      time_since_ordered: '11 min',
      amount: '21.00',
      quantity_products: '2'
    },
    {
      client_name: 'Alex Soto',
      order_number: '#0006',
      time_since_ordered: '14 min',
      amount: '62.00',
      quantity_products: '11'
    },
    {
      client_name: 'Dany Torrealva',
      order_number: '#0007',
      time_since_ordered: '15 min',
      amount: '16.00',
      quantity_products: '1'
    },
    {
      client_name: 'Alex Soto',
      order_number: '#0008',
      time_since_ordered: '17 min',
      amount: '17.00',
      quantity_products: '2'
    },
  ]

  order_detail: any[] = [
    {
      client_name: 'Franz Carhuaricra',
      order_number: '#0001',
      time_since_ordered: '5 min',
      total_amount: '43.60',
      quantity_products: '5',
    },
  ]

  order_detail_products: any[] =[
    {
      quantity_of_product: 2,
      name_of_product: 'Milo en lata',
      price_of_product: 25.30,
    },
    {
      quantity_of_product: 1,
      name_of_product: 'Pan de molde Bimbo',
      price_of_product: 7.00,
    },
    {
      quantity_of_product: 3,
      name_of_product: 'Atún A-1',
      price_of_product: 6.30,
    },
    {
      quantity_of_product: 2,
      name_of_product: 'Inka Cola Personal',
      price_of_product: 3.50,
    },
    {
      quantity_of_product: 4,
      name_of_product: 'Galleta Casino',
      price_of_product: 1.50,
    },
  ];

  constructor() {}

  ngOnInit(): void {}
}
