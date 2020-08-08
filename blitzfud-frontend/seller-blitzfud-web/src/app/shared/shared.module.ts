/*=============== IMPORTACIONES ===============*/
import { NgModule } from '@angular/core';
import { NgZorroModule } from './../ng-zorro/ng-zorro.module';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

/*=============== COMPONENTES ===============*/
import { NotpagefoundComponent } from './notpagefound/notpagefound.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { HeaderComponent } from './header/header.component';
import { ModalAddProductComponent } from './modal-add-product/modal-add-product.component';
import { ModalUpdatedProductComponent } from './modal-updated-product/modal-updated-product.component';
import { ModalDeleteProductComponent } from './modal-delete-product/modal-delete-product.component';
import { ModalConfirmationDeleteProductComponent } from './modal-confirmation-delete-product/modal-confirmation-delete-product.component';
import { LoaderComponent } from './loader/loader.component';

@NgModule({
  declarations: [
    HeaderComponent,
    SidebarComponent,
    NotpagefoundComponent,
    ModalAddProductComponent,
    ModalUpdatedProductComponent,
    ModalDeleteProductComponent,
    ModalConfirmationDeleteProductComponent,
    LoaderComponent,
  ],
  exports: [
    HeaderComponent,
    SidebarComponent,
    NotpagefoundComponent,
    ModalAddProductComponent,
    ModalUpdatedProductComponent,
    ModalDeleteProductComponent,
    ModalConfirmationDeleteProductComponent,
    LoaderComponent
  ],
  imports: [NgZorroModule, RouterModule, CommonModule],
})
export class SharedModule {}
