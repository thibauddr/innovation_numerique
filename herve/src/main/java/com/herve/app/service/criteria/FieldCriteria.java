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
 * Criteria class for the {@link com.herve.app.domain.Field} entity. This class is used
 * in {@link com.herve.app.web.rest.FieldResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fields?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class FieldCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter position;

    private LongFilter userId;

    private LongFilter sensorId;

    private Boolean distinct;

    public FieldCriteria() {}

    public FieldCriteria(FieldCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.position = other.position == null ? null : other.position.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.sensorId = other.sensorId == null ? null : other.sensorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FieldCriteria copy() {
        return new FieldCriteria(this);
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

    public StringFilter getPosition() {
        return position;
    }

    public StringFilter position() {
        if (position == null) {
            position = new StringFilter();
        }
        return position;
    }

    public void setPosition(StringFilter position) {
        this.position = position;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final FieldCriteria that = (FieldCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(position, that.position) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(sensorId, that.sensorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, position, userId, sensorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FieldCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (position != null ? "position=" + position + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (sensorId != null ? "sensorId=" + sensorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
