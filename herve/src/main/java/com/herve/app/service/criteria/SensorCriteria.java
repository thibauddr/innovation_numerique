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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.herve.app.domain.Sensor} entity. This class is used
 * in {@link com.herve.app.web.rest.SensorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sensors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class SensorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private IntegerFilter position_x;

    private IntegerFilter position_y;

    private FloatFilter threshold;

    private FloatFilter minThreshold;

    private LongFilter fieldId;

    private LongFilter sensorTypeId;

    private LongFilter sensorDataId;

    private Boolean distinct;

    public SensorCriteria() {}

    public SensorCriteria(SensorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.position_x = other.position_x == null ? null : other.position_x.copy();
        this.position_y = other.position_y == null ? null : other.position_y.copy();
        this.threshold = other.threshold == null ? null : other.threshold.copy();
        this.minThreshold = other.minThreshold == null ? null : other.minThreshold.copy();
        this.fieldId = other.fieldId == null ? null : other.fieldId.copy();
        this.sensorTypeId = other.sensorTypeId == null ? null : other.sensorTypeId.copy();
        this.sensorDataId = other.sensorDataId == null ? null : other.sensorDataId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SensorCriteria copy() {
        return new SensorCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getPosition_x() {
        return position_x;
    }

    public IntegerFilter position_x() {
        if (position_x == null) {
            position_x = new IntegerFilter();
        }
        return position_x;
    }

    public void setPosition_x(IntegerFilter position_x) {
        this.position_x = position_x;
    }

    public IntegerFilter getPosition_y() {
        return position_y;
    }

    public IntegerFilter position_y() {
        if (position_y == null) {
            position_y = new IntegerFilter();
        }
        return position_y;
    }

    public void setPosition_y(IntegerFilter position_y) {
        this.position_y = position_y;
    }

    public FloatFilter getThreshold() {
        return threshold;
    }

    public FloatFilter threshold() {
        if (threshold == null) {
            threshold = new FloatFilter();
        }
        return threshold;
    }

    public void setThreshold(FloatFilter threshold) {
        this.threshold = threshold;
    }

    public FloatFilter getMinThreshold() {
        return minThreshold;
    }

    public FloatFilter minThreshold() {
        if (minThreshold == null) {
            minThreshold = new FloatFilter();
        }
        return minThreshold;
    }

    public void setMinThreshold(FloatFilter minThreshold) {
        this.minThreshold = minThreshold;
    }

    public LongFilter getFieldId() {
        return fieldId;
    }

    public LongFilter fieldId() {
        if (fieldId == null) {
            fieldId = new LongFilter();
        }
        return fieldId;
    }

    public void setFieldId(LongFilter fieldId) {
        this.fieldId = fieldId;
    }

    public LongFilter getSensorTypeId() {
        return sensorTypeId;
    }

    public LongFilter sensorTypeId() {
        if (sensorTypeId == null) {
            sensorTypeId = new LongFilter();
        }
        return sensorTypeId;
    }

    public void setSensorTypeId(LongFilter sensorTypeId) {
        this.sensorTypeId = sensorTypeId;
    }

    public LongFilter getSensorDataId() {
        return sensorDataId;
    }

    public LongFilter sensorDataId() {
        if (sensorDataId == null) {
            sensorDataId = new LongFilter();
        }
        return sensorDataId;
    }

    public void setSensorDataId(LongFilter sensorDataId) {
        this.sensorDataId = sensorDataId;
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
        final SensorCriteria that = (SensorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(position_x, that.position_x) &&
            Objects.equals(position_y, that.position_y) &&
            Objects.equals(threshold, that.threshold) &&
            Objects.equals(minThreshold, that.minThreshold) &&
            Objects.equals(fieldId, that.fieldId) &&
            Objects.equals(sensorTypeId, that.sensorTypeId) &&
            Objects.equals(sensorDataId, that.sensorDataId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            position_x,
            position_y,
            threshold,
            minThreshold,
            fieldId,
            sensorTypeId,
            sensorDataId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SensorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (position_x != null ? "position_x=" + position_x + ", " : "") +
            (position_y != null ? "position_y=" + position_y + ", " : "") +
            (threshold != null ? "threshold=" + threshold + ", " : "") +
            (minThreshold != null ? "minThreshold=" + minThreshold + ", " : "") +
            (fieldId != null ? "fieldId=" + fieldId + ", " : "") +
            (sensorTypeId != null ? "sensorTypeId=" + sensorTypeId + ", " : "") +
            (sensorDataId != null ? "sensorDataId=" + sensorDataId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
