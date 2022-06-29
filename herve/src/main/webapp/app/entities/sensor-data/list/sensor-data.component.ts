import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISensorData } from '../sensor-data.model';
import { SensorDataService } from '../service/sensor-data.service';
import { SensorDataDeleteDialogComponent } from '../delete/sensor-data-delete-dialog.component';

@Component({
  selector: 'jhi-sensor-data',
  templateUrl: './sensor-data.component.html',
})
export class SensorDataComponent implements OnInit {
  sensorData?: ISensorData[];
  isLoading = false;

  constructor(protected sensorDataService: SensorDataService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.sensorDataService.query().subscribe({
      next: (res: HttpResponse<ISensorData[]>) => {
        this.isLoading = false;
        this.sensorData = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISensorData): number {
    return item.id!;
  }

  delete(sensorData: ISensorData): void {
    const modalRef = this.modalService.open(SensorDataDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sensorData = sensorData;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
