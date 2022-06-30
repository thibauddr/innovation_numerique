import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IField } from 'app/entities/field/field.model';
import { FieldService } from 'app/entities/field/service/field.service';
import { ISensorType } from 'app/entities/sensor-type/sensor-type.model';
import { SensorTypeService } from 'app/entities/sensor-type/service/sensor-type.service';
import { ISensor, Sensor } from 'app/entities/sensor/sensor.model';
import { SensorService } from 'app/entities/sensor/service/sensor.service';

@Component({
  selector: 'jhi-sensor-update',
  templateUrl: './sensor-update.component.html',
})
export class SensorUpdateComponent implements OnInit {
  isSaving = false;

  fieldsSharedCollection: IField[] = [];
  sensorTypesSharedCollection: ISensorType[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    position_x: [],
    position_y: [],
    field: [],
    sensorType: [null, Validators.required],
  });

  constructor(
    protected sensorService: SensorService,
    protected fieldService: FieldService,
    protected sensorTypeService: SensorTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sensor }) => {
      this.updateForm(sensor);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sensor = this.createFromForm();
    if (sensor.id !== undefined) {
      this.subscribeToSaveResponse(this.sensorService.update(sensor));
    } else {
      this.subscribeToSaveResponse(this.sensorService.create(sensor));
    }
  }

  trackFieldById(_index: number, item: IField): number {
    return item.id!;
  }

  trackSensorTypeById(_index: number, item: ISensorType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISensor>>): void {
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

  protected updateForm(sensor: ISensor): void {
    this.editForm.patchValue({
      id: sensor.id,
      name: sensor.name,
      description: sensor.description,
      position_x: sensor.position_x,
      position_y: sensor.position_y,
      field: sensor.field,
      sensorType: sensor.sensorType,
    });

    this.fieldsSharedCollection = this.fieldService.addFieldToCollectionIfMissing(this.fieldsSharedCollection, sensor.field);
    this.sensorTypesSharedCollection = this.sensorTypeService.addSensorTypeToCollectionIfMissing(
      this.sensorTypesSharedCollection,
      sensor.sensorType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fieldService
      .query()
      .pipe(map((res: HttpResponse<IField[]>) => res.body ?? []))
      .pipe(map((fields: IField[]) => this.fieldService.addFieldToCollectionIfMissing(fields, this.editForm.get('field')!.value)))
      .subscribe((fields: IField[]) => (this.fieldsSharedCollection = fields));

    this.sensorTypeService
      .query()
      .pipe(map((res: HttpResponse<ISensorType[]>) => res.body ?? []))
      .pipe(
        map((sensorTypes: ISensorType[]) =>
          this.sensorTypeService.addSensorTypeToCollectionIfMissing(sensorTypes, this.editForm.get('sensorType')!.value)
        )
      )
      .subscribe((sensorTypes: ISensorType[]) => (this.sensorTypesSharedCollection = sensorTypes));
  }

  protected createFromForm(): ISensor {
    return {
      ...new Sensor(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      position_x: this.editForm.get(['position_x'])!.value,
      position_y: this.editForm.get(['position_y'])!.value,
      field: this.editForm.get(['field'])!.value,
      sensorType: this.editForm.get(['sensorType'])!.value,
    };
  }
}
