import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

import { CarouselModule } from 'primeng/carousel';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { TimelineModule } from 'primeng/timeline';
import { SliderModule } from 'primeng/slider';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { TableModule } from 'primeng/table';
import { ChipModule } from 'primeng/chip';

@NgModule({
  imports: [
    SharedModule,
    CarouselModule,
    ButtonModule,
    ToastModule,
    ButtonModule,
    TimelineModule,
    SliderModule,
    MessageModule,
    MessagesModule,
    TableModule,
    ChipModule,
    RouterModule.forChild([HOME_ROUTE]),
  ],
  declarations: [HomeComponent],
})
export class HomeModule {}
