import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISensorType } from '../sensor-type.model';

@Component({
  selector: 'jhi-sensor-type-detail',
  templateUrl: './sensor-type-detail.component.html',
})
export class SensorTypeDetailComponent implements OnInit {
  sensorType: ISensorType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sensorType }) => {
      this.sensorType = sensorType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
