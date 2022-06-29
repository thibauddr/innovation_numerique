import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SensorDataDetailComponent } from './sensor-data-detail.component';

describe('SensorData Management Detail Component', () => {
  let comp: SensorDataDetailComponent;
  let fixture: ComponentFixture<SensorDataDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SensorDataDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sensorData: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SensorDataDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SensorDataDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sensorData on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sensorData).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
