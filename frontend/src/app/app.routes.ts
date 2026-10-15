import { Routes } from '@angular/router';
import { adminGuard, authGuard } from './guards/auth.guard';
import { CategoriesComponent } from './pages/categories/categories.component';
import { LoginComponent } from './pages/login/login.component';
import { MenuItemsComponent } from './pages/menu-items/menu-items.component';
import { OrdersComponent } from './pages/orders/orders.component';
import { TablesComponent } from './pages/tables/tables.component';
import { UsersComponent } from './pages/users/users.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'orders', component: OrdersComponent, canActivate: [authGuard] },
  { path: 'menu', component: MenuItemsComponent, canActivate: [authGuard] },
  { path: 'tables', component: TablesComponent, canActivate: [authGuard] },
  { path: 'categories', component: CategoriesComponent, canActivate: [authGuard, adminGuard] },
  { path: 'users', component: UsersComponent, canActivate: [authGuard, adminGuard] },
  { path: '', redirectTo: 'orders', pathMatch: 'full' },
  { path: '**', redirectTo: 'orders' }
];
