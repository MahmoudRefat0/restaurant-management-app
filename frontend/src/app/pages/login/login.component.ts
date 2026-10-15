import { Component, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { getErrorMessage } from '../../services/error-message';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  private auth = inject(AuthService);
  private router = inject(Router);

  username = '';
  password = '';
  error = '';
  loading = false;

  login(): void {
    this.loading = true;
    this.error = '';

    this.auth.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/orders']),
      error: (err: HttpErrorResponse) => {
        this.error = getErrorMessage(err);
        this.loading = false;
      }
    });
  }
}
