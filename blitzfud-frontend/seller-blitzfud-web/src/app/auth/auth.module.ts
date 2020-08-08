import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

// Components
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { RegisterMarketComponent } from './register-market/register-market.component';
import { HeroComponent } from './hero/hero.component';

// Modules
import { SharedModule } from './../shared/shared.module';
import { NgZorroModule } from './../ng-zorro/ng-zorro.module';
import { AgmCoreModule } from '@agm/core';


@NgModule({
    declarations: [
        LoginComponent,
        RegisterComponent,
        RegisterMarketComponent,
        HeroComponent
    ],
    exports: [
        LoginComponent,
        RegisterComponent,
        RegisterMarketComponent,
        HeroComponent
    ],
    imports: [
        SharedModule,
        NgZorroModule,
        FormsModule,
        ReactiveFormsModule,
        CommonModule,
        AgmCoreModule,
        RouterModule
    ]
})
export class AuthModule{}
