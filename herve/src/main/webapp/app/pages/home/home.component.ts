import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { map, takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { FieldService } from 'app/entities/field/service/field.service';
import { IField } from 'app/entities/field/field.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  fields: IField[] = [];

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router, private fieldService: FieldService) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    this.fieldService
      .query()
      .pipe(
        map((res: HttpResponse<IField[]>) => {
          this.fields = res.body ?? [];
        })
      )
      .subscribe();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
