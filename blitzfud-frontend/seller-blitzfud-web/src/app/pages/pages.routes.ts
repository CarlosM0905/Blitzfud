/*===============> IMPORTACIONES <===============*/
import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

/*===============> COMPONENTES <===============*/
import { PagesComponent } from './pages.component';
import { ProductsComponent } from './products/products.component';
import { HomeComponent } from './home/home.component';
import { OrderHistoryComponent } from './orders/order-history/order-history.component';
import { PendingOrdersComponent } from './orders/pending-orders/pending-orders.component';
import { EditProfileComponent } from './edit-profile/edit-profile.component';
import { DistributorsComponent } from './distributors/distributors.component';

/*===============> GUARDS <===============*/
import { LoginGuard } from './../services/guards/login.guard';
import { HaveMarketGuard } from './../services/guards/have-market.guard';

const pagesRoutes: Routes = [
    {
        path: 'dashboard',
        canActivate: [LoginGuard, HaveMarketGuard],
        component: PagesComponent,
        children: [
            {
                path: 'distributors', component: DistributorsComponent,
            },
            {
                path: 'products', component: ProductsComponent,
            },
            {
                path: 'home', component: HomeComponent,
            },
            {
                path: 'orders-pending', component: PendingOrdersComponent,
            },
            {
                path: 'history',  component: OrderHistoryComponent,
            },
            {
                path: 'edit-profile',  component: EditProfileComponent,
            },
            {
                path: '', component: HomeComponent,
            },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(pagesRoutes)],
    exports: [RouterModule]
})
export class PagesRoutingModule{}