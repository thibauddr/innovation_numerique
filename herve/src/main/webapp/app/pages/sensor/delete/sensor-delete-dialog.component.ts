import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISensor } from '../../../entities/sensor/sensor.model';
import { SensorService } from '../../../entities/sensor/service/sensor.service';

@Component({
  templateUrl: './sensor-delete-dialog.component.html',
})
export class SensorDeleteDialogComponent {
  sensor?: ISensor;

  constructor(protected sensorService: SensorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sensorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
