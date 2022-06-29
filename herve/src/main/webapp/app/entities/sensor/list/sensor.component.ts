import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISensor } from '../sensor.model';
import { SensorService } from '../service/sensor.service';
import { SensorDeleteDialogComponent } from '../delete/sensor-delete-dialog.component';

@Component({
  selector: 'jhi-sensor',
  templateUrl: './sensor.component.html',
})
export class SensorComponent implements OnInit {
  sensors?: ISensor[];
  isLoading = false;

  constructor(protected sensorService: SensorService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.sensorService.query().subscribe({
      next: (res: HttpResponse<ISensor[]>) => {
        this.isLoading = false;
        this.sensors = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISensor): number {
    return item.id!;
  }

  delete(sensor: ISensor): void {
    const modalRef = this.modalService.open(SensorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sensor = sensor;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
