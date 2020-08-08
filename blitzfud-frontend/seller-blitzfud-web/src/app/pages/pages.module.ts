/*===============> IMPORTACIONES <===============*/
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

/*===============> COMPONENTES <===============*/
import { ProductsComponent } from './products/products.component';
import { PagesComponent } from './pages.component';
import { EditComponent } from './products/edit/edit.component';
import { HomeComponent } from './home/home.component';
import { PendingOrdersComponent } from './orders/pending-orders/pending-orders.component';
import { DistributorsComponent } from './distributors/distributors.component';
import { OrderHistoryComponent } from './orders/order-history/order-history.component';
import { MyDistributorsComponent } from './distributors/my-distributors/my-distributors.component';
import { SearchDistributorsComponent } from './distributors/search-distributors/search-distributors.component';
import { EditProfileComponent } from './edit-profile/edit-profile.component';
import { ModalInfoDistributorComponent } from './distributors/modal-info-distributor/modal-info-distributor.component';
import { ModalContractDistributorComponent } from './distributors/modal-contract-distributor/modal-contract-distributor.component';
import { ModalAttendOrderComponent } from './orders/pending-orders/modal-attend-order/modal-attend-order.component';
import { ModalRejectOrderComponent } from './orders/pending-orders/modal-reject-order/modal-reject-order.component';

/*===============> MODULOS <===============*/
import { SharedModule } from './../shared/shared.module';
import { NgZorroModule } from './../ng-zorro/ng-zorro.module';

@NgModule({
    declarations: [
        PagesComponent,
        ProductsComponent,
        EditComponent,
        HomeComponent,
        DistributorsComponent,
        MyDistributorsComponent,
        ModalInfoDistributorComponent,
        SearchDistributorsComponent,
        ModalContractDistributorComponent,
        PendingOrdersComponent,
        ModalRejectOrderComponent,
        OrderHistoryComponent,
        EditProfileComponent,
        ModalAttendOrderComponent
    ],
    exports: [
        PagesComponent,
        ProductsComponent,
        EditComponent,
        HomeComponent,
        DistributorsComponent,
        MyDistributorsComponent,
        SearchDistributorsComponent,
        PendingOrdersComponent,
        OrderHistoryComponent,
        EditProfileComponent
    ],
    imports: [
        SharedModule,
        NgZorroModule,
        FormsModule,
        ReactiveFormsModule,
        CommonModule,
        RouterModule
    ]
})
export class PagesModule{}