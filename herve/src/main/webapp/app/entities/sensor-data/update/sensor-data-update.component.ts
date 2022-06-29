import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISensorData, SensorData } from '../sensor-data.model';
import { SensorDataService } from '../service/sensor-data.service';
import { ISensor } from 'app/entities/sensor/sensor.model';
import { SensorService } from 'app/entities/sensor/service/sensor.service';

@Component({
  selector: 'jhi-sensor-data-update',
  templateUrl: './sensor-data-update.component.html',
})
export class SensorDataUpdateComponent implements OnInit {
  isSaving = false;

  sensorsSharedCollection: ISensor[] = [];

  editForm = this.fb.group({
    id: [],
    unit: [null, [Validators.required]],
    value: [null, [Validators.required]],
    datetime: [],
    sensor: [],
  });

  constructor(
    protected sensorDataService: SensorDataService,
    protected sensorService: SensorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sensorData }) => {
      this.updateForm(sensorData);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sensorData = this.createFromForm();
    if (sensorData.id !== undefined) {
      this.subscribeToSaveResponse(this.sensorDataService.update(sensorData));
    } else {
      this.subscribeToSaveResponse(this.sensorDataService.create(sensorData));
    }
  }

  trackSensorById(_index: number, item: ISensor): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISensorData>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(sensorData: ISensorData): void {
    this.editForm.patchValue({
      id: sensorData.id,
      unit: sensorData.unit,
      value: sensorData.value,
      datetime: sensorData.datetime,
      sensor: sensorData.sensor,
    });

    this.sensorsSharedCollection = this.sensorService.addSensorToCollectionIfMissing(this.sensorsSharedCollection, sensorData.sensor);
  }

  protected loadRelationshipsOptions(): void {
    this.sensorService
      .query()
      .pipe(map((res: HttpResponse<ISensor[]>) => res.body ?? []))
      .pipe(map((sensors: ISensor[]) => this.sensorService.addSensorToCollectionIfMissing(sensors, this.editForm.get('sensor')!.value)))
      .subscribe((sensors: ISensor[]) => (this.sensorsSharedCollection = sensors));
  }

  protected createFromForm(): ISensorData {
    return {
      ...new SensorData(),
      id: this.editForm.get(['id'])!.value,
      unit: this.editForm.get(['unit'])!.value,
      value: this.editForm.get(['value'])!.value,
      datetime: this.editForm.get(['datetime'])!.value,
      sensor: this.editForm.get(['sensor'])!.value,
    };
  }
}
