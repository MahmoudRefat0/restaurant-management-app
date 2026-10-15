import { Component, OnInit, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { User, UserRequest } from '../../models/models';
import { AuthService } from '../../services/auth.service';
import { getErrorMessage } from '../../services/error-message';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './users.component.html'
})
export class UsersComponent implements OnInit {
  private userService = inject(UserService);
  auth = inject(AuthService);

  users: User[] = [];
  form: UserRequest = this.emptyForm();
  error = '';

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.userService.getAll().subscribe(data => this.users = data);
  }

  save(): void {
    this.userService.create(this.form).subscribe({
      next: () => {
        this.form = this.emptyForm();
        this.error = '';
        this.load();
      },
      error: (err: HttpErrorResponse) => this.error = getErrorMessage(err)
    });
  }

  remove(user: User): void {
    if (!confirm(`Delete ${user.fullName}?`)) {
      return;
    }
    this.userService.delete(user.id).subscribe({
      next: () => this.load(),
      error: (err: HttpErrorResponse) => this.error = getErrorMessage(err)
    });
  }

  private emptyForm(): UserRequest {
    return { username: '', password: '', fullName: '', role: 'STAFF' };
  }
}
