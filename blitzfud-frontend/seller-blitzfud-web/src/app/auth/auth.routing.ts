import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

// Components
import { HeroComponent } from './hero/hero.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { RegisterMarketComponent } from './register-market/register-market.component';

// Guards
import { LoggedInGuard, NotHaveMarketGuard, LoginGuard } from '../services/service.index';

const authRoutes: Routes = [
    {
        path: 'welcome', component: HeroComponent,
        canActivate: [LoggedInGuard]
    },
    {
        path: 'login', component: LoginComponent,
        canActivate: [LoggedInGuard]
    },
    {
        path: 'register', component: RegisterComponent,
        canActivate: [LoggedInGuard]
    },
    {
        path: 'register-market', component: RegisterMarketComponent,
        canActivate: [NotHaveMarketGuard, LoginGuard]
    },
];

@NgModule({
    imports: [RouterModule.forChild(authRoutes)],
    exports: [RouterModule]
})
export class AuthRoutingModule{}
