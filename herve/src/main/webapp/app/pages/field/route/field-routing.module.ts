import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FieldComponent } from '../list/field.component';
import { FieldDetailComponent } from '../detail/field-detail.component';
import { FieldUpdateComponent } from '../update/field-update.component';
import { FieldRoutingResolveService } from './field-routing-resolve.service';

const fieldRoute: Routes = [
  {
    path: '',
    component: FieldComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FieldDetailComponent,
    resolve: {
      field: FieldRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FieldUpdateComponent,
    resolve: {
      field: FieldRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FieldUpdateComponent,
    resolve: {
      field: FieldRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fieldRoute)],
  exports: [RouterModule],
})
export class FieldRoutingModule {}
