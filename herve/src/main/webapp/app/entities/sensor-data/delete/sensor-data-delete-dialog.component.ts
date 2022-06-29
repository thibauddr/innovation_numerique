import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISensorData } from '../sensor-data.model';
import { SensorDataService } from '../service/sensor-data.service';

@Component({
  templateUrl: './sensor-data-delete-dialog.component.html',
})
export class SensorDataDeleteDialogComponent {
  sensorData?: ISensorData;

  constructor(protected sensorDataService: SensorDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sensorDataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
