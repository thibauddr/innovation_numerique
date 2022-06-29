import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISensorType } from '../sensor-type.model';
import { SensorTypeService } from '../service/sensor-type.service';
import { SensorTypeDeleteDialogComponent } from '../delete/sensor-type-delete-dialog.component';

@Component({
  selector: 'jhi-sensor-type',
  templateUrl: './sensor-type.component.html',
})
export class SensorTypeComponent implements OnInit {
  sensorTypes?: ISensorType[];
  isLoading = false;

  constructor(protected sensorTypeService: SensorTypeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.sensorTypeService.query().subscribe({
      next: (res: HttpResponse<ISensorType[]>) => {
        this.isLoading = false;
        this.sensorTypes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISensorType): number {
    return item.id!;
  }

  delete(sensorType: ISensorType): void {
    const modalRef = this.modalService.open(SensorTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sensorType = sensorType;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
