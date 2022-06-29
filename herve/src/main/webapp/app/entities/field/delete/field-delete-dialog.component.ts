import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IField } from '../field.model';
import { FieldService } from '../service/field.service';

@Component({
  templateUrl: './field-delete-dialog.component.html',
})
export class FieldDeleteDialogComponent {
  field?: IField;

  constructor(protected fieldService: FieldService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fieldService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
