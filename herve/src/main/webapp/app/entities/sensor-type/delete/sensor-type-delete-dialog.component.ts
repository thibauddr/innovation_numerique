import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISensorType } from '../sensor-type.model';
import { SensorTypeService } from '../service/sensor-type.service';

@Component({
  templateUrl: './sensor-type-delete-dialog.component.html',
})
export class SensorTypeDeleteDialogComponent {
  sensorType?: ISensorType;

  constructor(protected sensorTypeService: SensorTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sensorTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
