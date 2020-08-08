/*===============> IMPORTACIONES <===============*/
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

/*===============> SERVICIOS <===============*/
import { MarketService } from './../../services/market/market.service';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {

  /*================>  VARIABLES <================*/
  formUser: FormGroup;
  formMarket: FormGroup;
  images = [
    '../../assets/images/edit_market_blitzy.svg',
    '../../assets/images/edit_profile_blitzy.svg'
  ];
  imageView = this.images[1];

  constructor(private formBuilder: FormBuilder,
              private marketService: MarketService){}

  ngOnInit(): void {
    this.initializeForms();
    this.setValues();
  }

  setImageMarket(): void{
    this.imageView = this.images[0];
  }

  setImageProfile(): void{
    this.imageView = this.images[1];
  }

  /**
   * Para el usuario y la tienda:
   * Define valores por defecto y aplica validaciones
   */
  initializeForms(): void{
    this.formUser = this.formBuilder.group({
      first_name: ['', [Validators.required, Validators.min(3)]],
      last_name: ['', [Validators.required, Validators.min(3)]]
    });

    this.formMarket = this.formBuilder.group({
      name: ['', [Validators.required, Validators.min(10)]],
      description: ['', [Validators.required, Validators.min(20)]],
      pickup: [false],
      delivery: [false],
      deliveryPrice: [0],
      marketStatus: ['']
    });
  }

  /**
   * Para el usuario y la tienda:
   * Setea los valores tomados del Local Storage
   */
  async setValues(): Promise<any>{
    const userData = JSON.parse(localStorage.getItem('user'));
    const marketData = await this.marketService.getMarket().toPromise();
    this.formUser.patchValue({
      first_name: userData.firstName,
      last_name: userData.lastName
    });

    console.log(marketData);
    this.formMarket.patchValue({
      name: marketData.name,
      description: marketData.description
    });

    if (marketData.deliveryMethods === 'both'){
      this.formMarket.patchValue({
        pickup: true,
        delivery: true,
        deliveryPrice: marketData.deliveryPrice
      });
    }
    else if (marketData.deliveryMethods === 'pickup'){
      this.formMarket.patchValue({
        pickup: true,
      });
    }
    else{
      this.formMarket.patchValue({
        delivery: true,
        deliveryPrice: marketData.deliveryPrice
      });
    }
  }

  /**
   * Verifica si ha sido seleccionada la opcion delivery
   */
  isDeliverySelected(): boolean{
    return this.formMarket.get('delivery').value;
  }
}
