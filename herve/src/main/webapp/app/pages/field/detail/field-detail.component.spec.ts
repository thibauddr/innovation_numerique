import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FieldDetailComponent } from './field-detail.component';

describe('Field Management Detail Component', () => {
  let comp: FieldDetailComponent;
  let fixture: ComponentFixture<FieldDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FieldDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ field: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FieldDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FieldDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load field on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.field).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
