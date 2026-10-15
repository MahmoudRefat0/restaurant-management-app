import { Component, OnInit, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Category, CategoryRequest } from '../../models/models';
import { CategoryService } from '../../services/category.service';
import { getErrorMessage } from '../../services/error-message';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './categories.component.html'
})
export class CategoriesComponent implements OnInit {
  private categoryService = inject(CategoryService);

  categories: Category[] = [];
  form: CategoryRequest = { name: '', description: '' };
  editingId: number | null = null;
  error = '';

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.categoryService.getAll().subscribe(data => this.categories = data);
  }

  save(): void {
    const request = this.editingId
      ? this.categoryService.update(this.editingId, this.form)
      : this.categoryService.create(this.form);

    request.subscribe({
      next: () => {
        this.reset();
        this.load();
      },
      error: (err: HttpErrorResponse) => this.error = getErrorMessage(err)
    });
  }

  edit(category: Category): void {
    this.editingId = category.id;
    this.form = { name: category.name, description: category.description };
  }

  remove(category: Category): void {
    if (!confirm(`Delete ${category.name}?`)) {
      return;
    }
    this.categoryService.delete(category.id).subscribe({
      next: () => this.load(),
      error: (err: HttpErrorResponse) => this.error = getErrorMessage(err)
    });
  }

  reset(): void {
    this.editingId = null;
    this.form = { name: '', description: '' };
    this.error = '';
  }
}
