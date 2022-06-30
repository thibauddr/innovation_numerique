package com.herve.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.herve.app.domain.SensorData} entity. This class is used
 * in {@link com.herve.app.web.rest.SensorDataResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sensor-data?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class SensorDataCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter unit;

    private FloatFilter value;

    private LocalDateFilter datetime;

    private LongFilter sensorId;

    private Boolean distinct;

    public SensorDataCriteria() {}

    public SensorDataCriteria(SensorDataCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.unit = other.unit == null ? null : other.unit.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.datetime = other.datetime == null ? null : other.datetime.copy();
        this.sensorId = other.sensorId == null ? null : other.sensorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SensorDataCriteria copy() {
        return new SensorDataCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUnit() {
        return unit;
    }

    public StringFilter unit() {
        if (unit == null) {
            unit = new StringFilter();
        }
        return unit;
    }

    public void setUnit(StringFilter unit) {
        this.unit = unit;
    }

    public FloatFilter getValue() {
        return value;
    }

    public FloatFilter value() {
        if (value == null) {
            value = new FloatFilter();
        }
        return value;
    }

    public void setValue(FloatFilter value) {
        this.value = value;
    }

    public LocalDateFilter getDatetime() {
        return datetime;
    }

    public LocalDateFilter datetime() {
        if (datetime == null) {
            datetime = new LocalDateFilter();
        }
        return datetime;
    }

    public void setDatetime(LocalDateFilter datetime) {
        this.datetime = datetime;
    }

    public LongFilter getSensorId() {
        return sensorId;
    }

    public LongFilter sensorId() {
        if (sensorId == null) {
            sensorId = new LongFilter();
        }
        return sensorId;
    }

    public void setSensorId(LongFilter sensorId) {
        this.sensorId = sensorId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SensorDataCriteria that = (SensorDataCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(unit, that.unit) &&
            Objects.equals(value, that.value) &&
            Objects.equals(datetime, that.datetime) &&
            Objects.equals(sensorId, that.sensorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unit, value, datetime, sensorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SensorDataCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (unit != null ? "unit=" + unit + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (datetime != null ? "datetime=" + datetime + ", " : "") +
            (sensorId != null ? "sensorId=" + sensorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
