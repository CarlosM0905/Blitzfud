/*===============> IMPORTACIONES <===============*/
import { Component, OnInit } from '@angular/core';

/*===============> INTERFACES <===============*/
import { Product } from './../../interfaces/Product';

/*===============> SERVICIOS <===============*/
import { ProductService } from './../../services/product/product.service';
import { NotificationService } from './../../services/notification/notification.service';

declare var $: any;

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css'],
})
export class ProductsComponent implements OnInit {

  // VARIABLES
  count: number;
  products: any;
  showAddProducts = false;
  productToEdit: Product;
  checked = false;
  indeterminate = false;
  loadingProducts = false;
  listOfCurrentPageData: Product[] = [];
  listOfProducts: Product[] = [];
  setOfCheckedId = new Set<string>();
  visible = false;
  type = 'edit';


  constructor(private productService: ProductService, private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.getAllProducts();
  }

  /***
 *  Se obtiene todos los productos del servicio correspondiente
   * Y estos se añaden a la lista de productos
   */
  getAllProducts(): void {
    // Para que el Skeleton se visualice
    this.loadingProducts = true;
    this.productService
      .getProducts()
      .toPromise()
      .then((response: any) => {
        this.count = response.count;
        this.products = response.products;
        this.listOfProducts = this.products;
        if (response.products.length === 0){
          this.showAddProducts = true;
        }
        else{
          this.showAddProducts = false;
        }
        this.loadingProducts = false;
      })
      .catch(err => {
        console.log(err);
      });
  }

  /**
   * Actualiza el lista del productos
   * Cuando se hace alguna operación de modificación (CRUD)
   * @param event Indica si hubo una modificación
   */
  reloadProducts(event: boolean): void {
    if (event) {
      this.getAllProducts();
    }
  }

  /**
   * Actualiza el conjunto de productos seleccionados
   * @param id      id del producto
   * @param checked Si fue seleccionado
   */
  updateCheckedSet(id: string, checked: boolean): void {
    if (checked) {
      this.setOfCheckedId.add(id);
    } else {
      this.setOfCheckedId.delete(id);
    }
  }

  /**
   * Método llamado al seleccionar un producto
   * @param id
   * @param checked
   */
  onItemChecked(id: string, checked: boolean): void {
    this.updateCheckedSet(id, checked);
    this.refreshCheckedStatus();
  }

  /**
   * Método llamado para seleccionar todos los productos
   * @param value
   */
  onAllChecked(value: boolean): void {
    this.listOfCurrentPageData.forEach((item) =>
      this.updateCheckedSet(item._id, value)
    );
    this.refreshCheckedStatus();
  }

  /***
   * Para paginar la tabla
   */
  onCurrentPageDataChange($event: Product[]): void {
    console.log($event);
    this.listOfCurrentPageData = $event;
    this.refreshCheckedStatus();
  }

  /***
   * Actualiza el estado de los elementos seleccionados a nivel visual
   */
  refreshCheckedStatus(): void {
    this.checked = this.listOfCurrentPageData.every((item) =>
      this.setOfCheckedId.has(item._id)
    );
    this.indeterminate =
      this.listOfCurrentPageData.some((item) =>
        this.setOfCheckedId.has(item._id)
      ) && !this.checked;
  }

  /**
   * Edita un producto
   * @param product Objeto de Product
   */
  editProduct(product: Product): void {
    this.type = 'edit';
    this.visible = true;
    this.productToEdit = product;
  }

  /**
   * Agrega un producto
   */
  addProduct(): void {
    this.type = 'add';
    this.visible = true;
    this.productToEdit = null;
  }

  /**
   * Modal para confirmar la eliminación de un producto
   */
  verifyDelete(): void{
    $('#confirmationdelete').modal('show');
  }

  /**
   * Actualiza el estado del stocl de los productos seleccionados
   * Con stock / Sin Stock
   */
  async updateStatus(): Promise<any>{

    const updatePromises: any = [];

    for ( const idProduct of this.setOfCheckedId ){
      const product: Product = this.products.filter((val: Product) => val._id === idProduct)[0];
      if (product.status === 'available'){
        updatePromises.push(this.productService.updateProduct([{propName: 'status', value: 'out-of-stock'}], idProduct).toPromise());
      }
      else{
        updatePromises.push(this.productService.updateProduct([{propName: 'status', value: 'available'}], idProduct).toPromise());
      }
    }

    await Promise.all(updatePromises).then(
      response => {
        this.notificationService.createIconNotification('success', '¡Bien hecho!', 'Se actualizó el stock correctamente', 'bottomRight');
      }
    )
    .catch(err => {
      console.log(err);
    });

    this.reloadProducts(true);
    this.setOfCheckedId.clear();
  }

  /**
   * Elima uno o variaos productos seleccionados
   */
  async deleteProducts(): Promise<any> {

    const deletePromises: any = [];

    for (const idProduct of this.setOfCheckedId){
      deletePromises.push(this.productService.deleteProduct(idProduct).toPromise());
    }

    await Promise.all(deletePromises).then(
      response => {
        $('#delete').modal('show');
      }
    )
    .catch(err => {
      console.log(err);
    });

    this.reloadProducts(true);
    this.setOfCheckedId.clear();
  }

  /**
   * Para cerrar el modal
   */
  close(): void {
    this.visible = false;
  }

  confirm(): void {}
}
