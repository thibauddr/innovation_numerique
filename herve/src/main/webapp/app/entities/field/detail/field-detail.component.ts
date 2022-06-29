import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IField } from '../field.model';

@Component({
  selector: 'jhi-field-detail',
  templateUrl: './field-detail.component.html',
})
export class FieldDetailComponent implements OnInit {
  field: IField | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ field }) => {
      this.field = field;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
