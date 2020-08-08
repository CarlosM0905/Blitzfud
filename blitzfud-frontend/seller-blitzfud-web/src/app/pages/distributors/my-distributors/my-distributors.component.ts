/*===============> IMPORTACIONES <===============*/
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-my-distributors',
  templateUrl: './my-distributors.component.html',
  styleUrls: ['./my-distributors.component.css']
})
export class MyDistributorsComponent implements OnInit {

  // FAKE DATA
  public distributors = [
    {
      name: 'Franz Carhuaricra Marcelo',
      status: 'En linea',
      phone: '961412312'
    },
    {
      name: 'Diego Tueros Huapaya',
      status: 'En linea',
      phone: '961412312'
    },
    {
      name: 'Carlos Jose Yaringaño',
      status: 'En linea',
      phone: '961412312'
    },
    {
      name: 'Carlos Rodriguez Lazo',
      status: 'En linea',
      phone: '961412312'
    },
    {
      name: 'Franz Carhuaricra Marcelo',
      status: 'En linea',
      phone: '961412312'
    }
  ];
  constructor() { }

  ngOnInit(): void {}

}
