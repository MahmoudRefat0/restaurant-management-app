import { Component, OnInit, inject } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MenuItem, Order, OrderRequest, OrderStatus, RestaurantTable } from '../../models/models';
import { AuthService } from '../../services/auth.service';
import { getErrorMessage } from '../../services/error-message';
import { MenuItemService } from '../../services/menu-item.service';
import { OrderService } from '../../services/order.service';
import { TableService } from '../../services/table.service';

interface CartItem {
  menuItem: MenuItem;
  quantity: number;
}

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [FormsModule, DecimalPipe, DatePipe],
  templateUrl: './orders.component.html'
})
export class OrdersComponent implements OnInit {
  private orderService = inject(OrderService);
  private tableService = inject(TableService);
  private menuItemService = inject(MenuItemService);
  auth = inject(AuthService);

  orders: Order[] = [];
  tables: RestaurantTable[] = [];
  menuItems: MenuItem[] = [];
  statuses: OrderStatus[] = ['PENDING', 'PREPARING', 'SERVED', 'PAID', 'CANCELLED'];
  statusFilter: OrderStatus | '' = '';

  selectedTableId = 0;
  selectedMenuItemId = 0;
  quantity = 1;
  cart: CartItem[] = [];
  error = '';

  private readonly flow: OrderStatus[] = ['PENDING', 'PREPARING', 'SERVED', 'PAID'];

  ngOnInit(): void {
    this.loadOrders();
    this.loadFormData();
  }

  loadOrders(): void {
    this.orderService.getAll(this.statusFilter || undefined).subscribe(data => this.orders = data);
  }

  loadFormData(): void {
    this.tableService.getAll().subscribe(data => this.tables = data.filter(t => t.status !== 'OCCUPIED'));
    this.menuItemService.getAll().subscribe(data => this.menuItems = data.filter(m => m.available));
  }

  addToCart(): void {
    const menuItem = this.menuItems.find(m => m.id === this.selectedMenuItemId);
    if (!menuItem || this.quantity < 1) {
      return;
    }

    const existing = this.cart.find(c => c.menuItem.id === menuItem.id);
    if (existing) {
      existing.quantity += this.quantity;
    } else {
      this.cart.push({ menuItem, quantity: this.quantity });
    }
    this.selectedMenuItemId = 0;
    this.quantity = 1;
  }

  removeFromCart(item: CartItem): void {
    this.cart = this.cart.filter(c => c !== item);
  }

  get cartTotal(): number {
    return this.cart.reduce((sum, c) => sum + c.menuItem.price * c.quantity, 0);
  }

  createOrder(): void {
    const request: OrderRequest = {
      tableId: this.selectedTableId,
      items: this.cart.map(c => ({ menuItemId: c.menuItem.id, quantity: c.quantity }))
    };

    this.orderService.create(request).subscribe({
      next: () => {
        this.cart = [];
        this.selectedTableId = 0;
        this.error = '';
        this.loadOrders();
        this.loadFormData();
      },
      error: (err: HttpErrorResponse) => this.error = getErrorMessage(err)
    });
  }

  nextStatus(status: OrderStatus): OrderStatus | null {
    const index = this.flow.indexOf(status);
    return index >= 0 && index < this.flow.length - 1 ? this.flow[index + 1] : null;
  }

  isClosed(status: OrderStatus): boolean {
    return status === 'PAID' || status === 'CANCELLED';
  }

  changeStatus(order: Order, status: OrderStatus): void {
    this.orderService.updateStatus(order.id, status).subscribe({
      next: () => {
        this.loadOrders();
        this.loadFormData();
      },
      error: (err: HttpErrorResponse) => this.error = getErrorMessage(err)
    });
  }

  remove(order: Order): void {
    if (!confirm(`Delete order #${order.id}?`)) {
      return;
    }
    this.orderService.delete(order.id).subscribe({
      next: () => {
        this.loadOrders();
        this.loadFormData();
      },
      error: (err: HttpErrorResponse) => this.error = getErrorMessage(err)
    });
  }
}
