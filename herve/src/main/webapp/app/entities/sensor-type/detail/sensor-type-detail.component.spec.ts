import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SensorTypeDetailComponent } from './sensor-type-detail.component';

describe('SensorType Management Detail Component', () => {
  let comp: SensorTypeDetailComponent;
  let fixture: ComponentFixture<SensorTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SensorTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sensorType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SensorTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SensorTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sensorType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sensorType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
