import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISensorType, SensorType } from '../sensor-type.model';
import { SensorTypeService } from '../service/sensor-type.service';

@Component({
  selector: 'jhi-sensor-type-update',
  templateUrl: './sensor-type-update.component.html',
})
export class SensorTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    code: [],
    name: [],
    description: [],
  });

  constructor(protected sensorTypeService: SensorTypeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sensorType }) => {
      this.updateForm(sensorType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sensorType = this.createFromForm();
    if (sensorType.id !== undefined) {
      this.subscribeToSaveResponse(this.sensorTypeService.update(sensorType));
    } else {
      this.subscribeToSaveResponse(this.sensorTypeService.create(sensorType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISensorType>>): void {
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

  protected updateForm(sensorType: ISensorType): void {
    this.editForm.patchValue({
      id: sensorType.id,
      code: sensorType.code,
      name: sensorType.name,
      description: sensorType.description,
    });
  }

  protected createFromForm(): ISensorType {
    return {
      ...new SensorType(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
