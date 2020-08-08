import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

// Components
import { NotpagefoundComponent } from './shared/notpagefound/notpagefound.component';

// Modules
import { PagesRoutingModule } from './pages/pages.routes';
import { AuthRoutingModule } from './auth/auth.routing';

const AppRoutes: Routes = [
    {
        path: '', redirectTo: '/welcome', pathMatch: 'full'
    },
    {
        path: '**', component: NotpagefoundComponent
    }
];

@NgModule({
    imports: [
        RouterModule.forRoot(AppRoutes),
        AuthRoutingModule,
        PagesRoutingModule
    ],
    exports: [RouterModule]
})
export class AppRoutingModule{}


