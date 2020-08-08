/*===============> IMPORTACIONES <===============*/
import {
  Component,
  OnInit,
  Input,
  Output,
  EventEmitter,
  OnChanges,
  SimpleChanges,
} from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

/*===============> INTERFACES <===============*/
import { Product } from './../../../interfaces/Product';
import { Category } from './../../../interfaces/Category';

/*===============> SERVICIOS <===============*/
import { ProductService } from './../../../services/product/product.service';
import { NotificationService } from './../../../services/notification/notification.service';

declare var $: any;

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css'],
})

export class EditComponent implements OnInit, OnChanges {

  /*===============> INPUTS <===============*/
  @Input() type: any;
  @Input() productToEdit: Product;

  /*===============> OUTPUTS <===============*/
  @Output() productAdded: EventEmitter<any> = new EventEmitter<any>();

  /*===============> VARIABLES <===============*/
  form: FormGroup;
  categories: Category[] = [];
  units: string[] = ['mg', 'g', 'kg', 'ml', 'l', 'un'];
  highlights: string[] = ['Sí', 'No'];

  constructor(
    private formBuilder: FormBuilder,
    private productService: ProductService,
    private notificationService: NotificationService
  ) {
    this.createForm();
    this.productService
      .getCategories()
      .toPromise()
      .then((response) => {
        this.categories = response.categories;
      })
      .catch((err) => console.log(err));
  }

  ngOnInit(): void {}

  ngOnChanges(changes: SimpleChanges): void {
    this.verifyChanges(changes);
  }

  /**
   * Verifica los cambios en las variables de la vista
   * @param changes Cambios en la vista
   */
  verifyChanges(changes: SimpleChanges): void {
    if (changes.productToEdit && changes.productToEdit.currentValue) {
      const product = changes.productToEdit.currentValue;
      this.form.setValue({
        name: product.name,
        maxQuantity: product.maxQuantityPerOrder,
        category: product.category._id,
        price: product.price,
        highlight: product.highlight ? 'Sí' : 'No',
        contentOfProduct: product.content,
        unitOfMeasurement: product.unitOfMeasurement,
        description: '',
      });
    } else {
      this.form.reset();
    }
  }

  /*===============> GETTERS <===============*/
  get nameInvalid(): boolean {
    return this.form.get('name').invalid;
  }

  get maxQuantityInvalid(): boolean {
    return this.form.get('maxQuantity').invalid;
  }

  get categoryInvalid(): boolean {
    return this.form.get('category').invalid;
  }

  get priceInvalid(): boolean {
    return this.form.get('price').invalid;
  }

  get highlightInvalid(): boolean {
    return this.form.get('highlight').invalid;
  }

  get contentOfProductInvalid(): boolean {
    return this.form.get('contentOfProduct').invalid;
  }

  get unitOfMeasuremenInvalid(): boolean {
    return this.form.get('unitOfMeasurement').invalid;
  }

  get descriptionInvalid(): boolean {
    return this.form.get('description').invalid;
  }

  /**
   * Asigna valores iniciales a los campos del form y aplica validaciones
   */
  createForm(): void {
    this.form = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(5)]],
      maxQuantity: ['', [Validators.required, Validators.max(100), Validators.min(0)]],
      category: ['', [Validators.required]],
      price: ['', [Validators.required]],
      highlight: ['', [Validators.required]],
      contentOfProduct: ['', [Validators.required]],
      unitOfMeasurement: ['', [Validators.required]],
      description: [''],
    });
  }

  /**
   * Asigna valores iniciales al formularia
   */
  chargeData(): void {
    this.form.setValue({
      name: '',
      maxQuantity: '',
      category: '',
      price: '',
      highlight: '',
      contentOfProduct: '',
      unitOfMeasurement: '',
      description: '',
    });
  }

  /**
   * Evalua si se agregará o editará el producto
   * @param type Tipo de operacion
   */
  takeData(type: string): void {
    if (type === 'add') {
      this.saveProduct();
    } else {
      this.editProduct();
    }
  }

  /**
   * Se crea un objeto producto, guarda la información recbida y
   * se llama al servicio para guardar en la base de datos
   */
  saveProduct(): void {
    if (this.form.invalid) {
    } else {
      const product: Product = {
        name: this.form.value.name,
        maxQuantity: this.form.value.maxQuantity,
        category: this.form.value.category,
        price: this.form.value.price,
        highlight: this.form.value.highlight === 'Sí' ? true : false,
        contentOfProduct: this.form.value.contentOfProduct,
        unitOfMeasurement: this.form.value.unitOfMeasurement,
      };
      this.productService
        .createProduct(product)
        .toPromise()
        .then((response) => {
          console.log(response);
          this.productAdded.emit(true);
          this.form.reset();
          this.notificationService.createIconNotification('success', '¡Felicitaciones!', response.message , 'bottomRight');
        })
        .catch(err => {
          console.log(err);
        });
    }
  }

  /**
   * Edita un producto y se llama al servicio para su guardado en la base de datos
   */
  editProduct(): void {
    const body = [
      { propName: 'name', value: this.form.value.name },
      { propName: 'content', value: this.form.value.contentOfProduct},
      { propName: 'unitOfMeasurement', value: this.form.value.unitOfMeasurement},
      { propName: 'maxQuantityPerOrder', value: this.form.value.maxQuantity },
      { propName: 'price', value: this.form.value.price },
      { propName: 'highlight', value: this.form.value.highlight === 'Sí' ? true : false},
      { propName: 'category', value: this.form.value.category }
    ];
    this.productService.updateProduct(body, this.productToEdit._id).toPromise()
    .then(response => {
      this.productAdded.emit(true);
      $('#Updated').modal('show');
    })
    .catch(err => {
      console.log(err);
    });
  }
}
