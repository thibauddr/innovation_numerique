import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IField } from '../field.model';
import { FieldService } from '../service/field.service';
import { FieldDeleteDialogComponent } from '../delete/field-delete-dialog.component';

@Component({
  selector: 'jhi-field',
  templateUrl: './field.component.html',
})
export class FieldComponent implements OnInit {
  fields?: IField[];
  isLoading = false;

  constructor(protected fieldService: FieldService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.fieldService.query().subscribe({
      next: (res: HttpResponse<IField[]>) => {
        this.isLoading = false;
        this.fields = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IField): number {
    return item.id!;
  }

  delete(field: IField): void {
    const modalRef = this.modalService.open(FieldDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.field = field;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
