import { HttpErrorResponse } from '@angular/common/http';

export function getErrorMessage(err: HttpErrorResponse): string {
  return err.error?.message ?? 'Something went wrong, please try again';
}
