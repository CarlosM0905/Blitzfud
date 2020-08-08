/*================ IMPORTACIONES ================*/
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-hero',
  templateUrl: './hero.component.html',
  styleUrls: ['./hero.component.css']
})
export class HeroComponent implements OnInit {

  /*================ VARIABLES ================*/
  imageUrls = [
    'assets/images/online-shop.svg',
    'assets/images/store.svg',
  ];
  date: Date = new Date();
  footerText = `Blitzfud © - ${this.date.getFullYear()}`;

  constructor() { }

  ngOnInit(): void {
  }

}
