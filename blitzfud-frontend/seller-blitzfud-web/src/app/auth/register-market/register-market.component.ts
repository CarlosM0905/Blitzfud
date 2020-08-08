/*================ IMPORTACIONES  ================*/
import { Component, OnInit, ViewChild, ElementRef, NgZone } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MapsAPILoader, MouseEvent } from '@agm/core';

/*================ INTERFACES ================*/
import { User } from './../../interfaces/User';
import { Market } from './../../interfaces/Market';

/*================ SERVICIOS ================*/
import { UserService } from './../../services/service.index';
import { MarketService } from './../../services/service.index';

@Component({
  selector: 'app-register-market',
  templateUrl: './register-market.component.html',
  styleUrls: ['./register-market.component.css']
})
export class RegisterMarketComponent implements OnInit {

  /*================ VARIABLES ================*/
  @ViewChild('search') public searchElementRef: ElementRef;
  private geoCoder;
  latitude: number;
  longitude: number;
  zoom: number;
  address = '';
  formMarket: FormGroup;
  market: Market;

  /**
   * Define las variables de la clase
   * @param formBuilder Construye los formularios
   * @param marketService Llama a los metodos del servicio de tienda
   * @param userService Llama a los metodos del servicio de usuario
   * @param router Permite navegar entre rutas
   * @param mapsAPILoader Permite cargar y acceder a la API de Maps Google
   * @param ngZone Permite obtener los lugares
   */
  constructor(private formBuilder: FormBuilder,
              private marketService: MarketService,
              private userService: UserService,
              private router: Router,
              private mapsAPILoader: MapsAPILoader,
              private ngZone: NgZone) {
              }

  ngOnInit(): void {
    this.initializeFormMarket();
    this.mapsAPILoader.load().then(() => {
      this.setCurrentLocation();
      this.geoCoder = new google.maps.Geocoder();

      const autocomplete = new google.maps.places.Autocomplete(this.searchElementRef.nativeElement, {types: ['address']});
      autocomplete.addListener('place_changed', () => {
        this.ngZone.run(() => {
          const place: google.maps.places.PlaceResult = autocomplete.getPlace();

          if (place.geometry === undefined || place.geometry === null) {
            return;
          }
          this.latitude = place.geometry.location.lat();
          this.longitude = place.geometry.location.lng();
          this.address  = place.formatted_address;
          this.zoom = 15;
        });
      });
    });
  }

  /**
   * Establece la posicion y el lugar solicitandole acceso
   * al usuario a traves del navegador para acceder a su ubicacion
   */
  private setCurrentLocation(): void {
    this.latitude = -12.04623819806132;
    this.longitude = -77.04381780716703;
    if ('geolocation' in navigator) {
      navigator.geolocation.getCurrentPosition((position) => {
        this.latitude = position.coords.latitude;
        this.longitude = position.coords.longitude;
        this.getAddress(this.latitude, this.longitude);
      });
    }
  }

  /**
   * Actualiza la posicion y el lugar cuando el usuario
   * da click en algun lugar del mapa
   */
  mapUserClick($event: MouseEvent): void{
    this.latitude = $event.coords.lat;
    this.longitude = $event.coords.lng;
    this.getAddress(this.latitude, this.longitude);
  }

  /**
   * Actualiza la posicion y el lugar cuando el usuario
   * arrastra el marcador en el mapa
   */
  markerDragEnd($event: MouseEvent): void{
    this.latitude = $event.coords.lat;
    this.longitude = $event.coords.lng;
    this.getAddress(this.latitude, this.longitude);
  }

  /**
   * Obtiene la ubicacion a partir de la longitud y latitud a 
   * traves de PlacesAPI
   */
  getAddress(latitude: number, longitude: number): void{
    this.geoCoder.geocode({ location: { lat: latitude, lng: longitude } }, (results, status) => {
      if (status === 'OK') {
        if (results[0]) {
          this.zoom = 12;
          this.address = results[0].formatted_address;
        } else {
          window.alert('No se encontro resultados');
        }
      } else {
        window.alert('Fallo el geolocalizador: ' + status);
      }

    });
  }

  /**
   * Guarda la ubicacion actualizando el campo direccion
   * del formulario de la tienda a registrar
   */
  saveLocation(): void{
    this.formMarket.patchValue({
      address : this.address
    });
  }

  /**
   * Inicializa el formulario de la tienda con sus valores por defecto
   * asi como las validaciones basicas (campos obligatorios, tamaño minimo)
   */
  initializeFormMarket(): void{
    this.formMarket = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(5)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      address: ['', [Validators.required, Validators.minLength(10)]],
      pickup: [false],
      delivery: [false],
      deliveryPrice: [0, [Validators.min(0)]]
    });
  }

  /**
   * Crea un variable "market" donde se almacenan los datos
   * del formulario, se llama al metodo para crear una tienda
   * se recibe la respuesta y se llama al metodo para logearse
   * con una tienda registrada
   */
  registerMarket(): void{
    const market: Market = {
      name: this.formMarket.get('name').value,
      description: this.formMarket.get('description').value,
      address: this.formMarket.get('address').value,
      pickup: this.formMarket.get('pickup').value,
      delivery: this.formMarket.get('delivery').value,
      deliveryPrice: this.formMarket.get('deliveryPrice').value,
      latitude: this.latitude,
      longitude: this.longitude,
    };
    this.marketService.createMarket(market).toPromise()
    .then(resp => {
      this.loginWithMarket();
    })
    .catch(err => console.error(err));
  }

  /**
   * Se obtienen los datos de usuario desde el local storage
   * Se vuelve a hacer el login de usuario, para actualizar su token
   * Redirigiendolo a la pantalla correspondiente.
   */
  loginWithMarket(): void{
    const user: User = this.userService.getUserData();
    this.userService.login(user).then(response => {
        if (response.haveMarket){
          this.router.navigate(['/dashboard']);
        }
        else{
          this.router.navigate(['/register-market']);
        }
      });
  }

  /**
   * Cierra la sesión del sistema
   */
  logout(): void{
    this.userService.logout();
  }

  /**
   * Verifica que haya sido seleccionado
   * el metodo de entrega delivery
   */
  deliverySelected(): boolean{
    return this.formMarket.get('delivery').value;
  }

  /**
   * Verifica que se haya seleccionado al menos
   * un metodo de entrega
   */
  atLeastOneDeliveryMethod(): boolean{
    return this.formMarket.get('delivery').value || this.formMarket.get('pickup').value;
  }
}
