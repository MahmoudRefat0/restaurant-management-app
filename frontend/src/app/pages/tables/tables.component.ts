import { Component, OnInit, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { RestaurantTable, TableRequest, TableStatus } from '../../models/models';
import { AuthService } from '../../services/auth.service';
import { getErrorMessage } from '../../services/error-message';
import { TableService } from '../../services/table.service';

@Component({
  selector: 'app-tables',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './tables.component.html'
})
export class TablesComponent implements OnInit {
  private tableService = inject(TableService);
  auth = inject(AuthService);

  tables: RestaurantTable[] = [];
  statuses: TableStatus[] = ['AVAILABLE', 'OCCUPIED', 'RESERVED'];
  form: TableRequest = { number: 1, capacity: 4 };
  editingId: number | null = null;
  error = '';

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.tableService.getAll().subscribe(data => this.tables = data);
  }

  save(): void {
    const request = this.editingId
      ? this.tableService.update(this.editingId, this.form)
      : this.tableService.create(this.form);

    request.subscribe({
      next: () => {
        this.reset();
        this.load();
      },
      error: (err: HttpErrorResponse) => this.error = getErrorMessage(err)
    });
  }

  changeStatus(table: RestaurantTable, status: TableStatus): void {
    this.tableService.updateStatus(table.id, status).subscribe({
      next: () => this.load(),
      error: (err: HttpErrorResponse) => this.error = getErrorMessage(err)
    });
  }

  edit(table: RestaurantTable): void {
    this.editingId = table.id;
    this.form = { number: table.number, capacity: table.capacity };
  }

  remove(table: RestaurantTable): void {
    if (!confirm(`Delete table ${table.number}?`)) {
      return;
    }
    this.tableService.delete(table.id).subscribe({
      next: () => this.load(),
      error: (err: HttpErrorResponse) => this.error = getErrorMessage(err)
    });
  }

  reset(): void {
    this.editingId = null;
    this.form = { number: 1, capacity: 4 };
    this.error = '';
  }
}
