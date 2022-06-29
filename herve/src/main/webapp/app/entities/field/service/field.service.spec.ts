import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IField, Field } from '../field.model';

import { FieldService } from './field.service';

describe('Field Service', () => {
  let service: FieldService;
  let httpMock: HttpTestingController;
  let elemDefault: IField;
  let expectedResult: IField | IField[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FieldService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
      position: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Field', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Field()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Field', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          position: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Field', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
          position: 'BBBBBB',
        },
        new Field()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Field', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          position: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Field', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFieldToCollectionIfMissing', () => {
      it('should add a Field to an empty array', () => {
        const field: IField = { id: 123 };
        expectedResult = service.addFieldToCollectionIfMissing([], field);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(field);
      });

      it('should not add a Field to an array that contains it', () => {
        const field: IField = { id: 123 };
        const fieldCollection: IField[] = [
          {
            ...field,
          },
          { id: 456 },
        ];
        expectedResult = service.addFieldToCollectionIfMissing(fieldCollection, field);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Field to an array that doesn't contain it", () => {
        const field: IField = { id: 123 };
        const fieldCollection: IField[] = [{ id: 456 }];
        expectedResult = service.addFieldToCollectionIfMissing(fieldCollection, field);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(field);
      });

      it('should add only unique Field to an array', () => {
        const fieldArray: IField[] = [{ id: 123 }, { id: 456 }, { id: 58809 }];
        const fieldCollection: IField[] = [{ id: 123 }];
        expectedResult = service.addFieldToCollectionIfMissing(fieldCollection, ...fieldArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const field: IField = { id: 123 };
        const field2: IField = { id: 456 };
        expectedResult = service.addFieldToCollectionIfMissing([], field, field2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(field);
        expect(expectedResult).toContain(field2);
      });

      it('should accept null and undefined values', () => {
        const field: IField = { id: 123 };
        expectedResult = service.addFieldToCollectionIfMissing([], null, field, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(field);
      });

      it('should return initial array if no Field is added', () => {
        const fieldCollection: IField[] = [{ id: 123 }];
        expectedResult = service.addFieldToCollectionIfMissing(fieldCollection, undefined, null);
        expect(expectedResult).toEqual(fieldCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
