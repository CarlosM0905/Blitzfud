import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

// Services
import { UserService } from './service.index';
import { MarketService } from './market/market.service';
import { ProductService } from './product/product.service';
import { LoaderService } from './loader/loader.service';
import { NotificationService } from './notification/notification.service';

// Guards
import { LoginGuard } from './guards/login.guard';
import { LoggedInGuard } from './guards/logged-in.guard';
import { HaveMarketGuard } from './guards/have-market.guard';
import { NotHaveMarketGuard } from './guards/not-have-market.guard';

@NgModule({
  declarations: [],
  imports: [CommonModule, HttpClientModule],
  providers: [
    UserService,
    MarketService,
    LoginGuard,
    LoggedInGuard,
    HaveMarketGuard,
    NotHaveMarketGuard,
    ProductService,
    LoaderService,
    NotificationService,
  ],
})
export class ServiceModule {}
