import { Component, OnInit, inject } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Category, MenuItem, MenuItemRequest } from '../../models/models';
import { AuthService } from '../../services/auth.service';
import { CategoryService } from '../../services/category.service';
import { getErrorMessage } from '../../services/error-message';
import { MenuItemService } from '../../services/menu-item.service';

@Component({
  selector: 'app-menu-items',
  standalone: true,
  imports: [FormsModule, DecimalPipe],
  templateUrl: './menu-items.component.html'
})
export class MenuItemsComponent implements OnInit {
  private menuItemService = inject(MenuItemService);
  private categoryService = inject(CategoryService);
  auth = inject(AuthService);

  menuItems: MenuItem[] = [];
  categories: Category[] = [];
  filterCategoryId = 0;
  form: MenuItemRequest = this.emptyForm();
  editingId: number | null = null;
  error = '';

  ngOnInit(): void {
    this.categoryService.getAll().subscribe(data => this.categories = data);
    this.load();
  }

  load(): void {
    this.menuItemService.getAll(this.filterCategoryId).subscribe(data => this.menuItems = data);
  }

  save(): void {
    const request = this.editingId
      ? this.menuItemService.update(this.editingId, this.form)
      : this.menuItemService.create(this.form);

    request.subscribe({
      next: () => {
        this.reset();
        this.load();
      },
      error: (err: HttpErrorResponse) => this.error = getErrorMessage(err)
    });
  }

  edit(item: MenuItem): void {
    this.editingId = item.id;
    this.form = {
      name: item.name,
      description: item.description,
      price: item.price,
      categoryId: item.categoryId,
      available: item.available
    };
  }

  remove(item: MenuItem): void {
    if (!confirm(`Delete ${item.name}?`)) {
      return;
    }
    this.menuItemService.delete(item.id).subscribe({
      next: () => this.load(),
      error: (err: HttpErrorResponse) => this.error = getErrorMessage(err)
    });
  }

  reset(): void {
    this.editingId = null;
    this.form = this.emptyForm();
    this.error = '';
  }

  private emptyForm(): MenuItemRequest {
    return { name: '', description: '', price: 0, categoryId: 0, available: true };
  }
}
