package com.herve.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sensor.
 */
@Entity
@Table(name = "sensor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "position_x")
    private Integer position_x;

    @Column(name = "position_y")
    private Integer position_y;

    @Column(name = "threshold")
    private Float threshold;

    @Column(name = "min_threshold")
    private Float minThreshold;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "sensors" }, allowSetters = true)
    private Field field;

    @ManyToOne(optional = false)
    @NotNull
    private SensorType sensorType;

    @OneToMany(mappedBy = "sensor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sensor" }, allowSetters = true)
    private Set<SensorData> sensorData = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sensor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Sensor name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Sensor description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPosition_x() {
        return this.position_x;
    }

    public Sensor position_x(Integer position_x) {
        this.setPosition_x(position_x);
        return this;
    }

    public void setPosition_x(Integer position_x) {
        this.position_x = position_x;
    }

    public Integer getPosition_y() {
        return this.position_y;
    }

    public Sensor position_y(Integer position_y) {
        this.setPosition_y(position_y);
        return this;
    }

    public void setPosition_y(Integer position_y) {
        this.position_y = position_y;
    }

    public Float getThreshold() {
        return this.threshold;
    }

    public Sensor threshold(Float threshold) {
        this.setThreshold(threshold);
        return this;
    }

    public void setThreshold(Float threshold) {
        this.threshold = threshold;
    }

    public Float getMinThreshold() {
        return this.minThreshold;
    }

    public Sensor minThreshold(Float minThreshold) {
        this.setMinThreshold(minThreshold);
        return this;
    }

    public void setMinThreshold(Float minThreshold) {
        this.minThreshold = minThreshold;
    }

    public Field getField() {
        return this.field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Sensor field(Field field) {
        this.setField(field);
        return this;
    }

    public SensorType getSensorType() {
        return this.sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public Sensor sensorType(SensorType sensorType) {
        this.setSensorType(sensorType);
        return this;
    }

    public Set<SensorData> getSensorData() {
        return this.sensorData;
    }

    public void setSensorData(Set<SensorData> sensorData) {
        if (this.sensorData != null) {
            this.sensorData.forEach(i -> i.setSensor(null));
        }
        if (sensorData != null) {
            sensorData.forEach(i -> i.setSensor(this));
        }
        this.sensorData = sensorData;
    }

    public Sensor sensorData(Set<SensorData> sensorData) {
        this.setSensorData(sensorData);
        return this;
    }

    public Sensor addSensorData(SensorData sensorData) {
        this.sensorData.add(sensorData);
        sensorData.setSensor(this);
        return this;
    }

    public Sensor removeSensorData(SensorData sensorData) {
        this.sensorData.remove(sensorData);
        sensorData.setSensor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sensor)) {
            return false;
        }
        return id != null && id.equals(((Sensor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sensor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", position_x=" + getPosition_x() +
            ", position_y=" + getPosition_y() +
            ", threshold=" + getThreshold() +
            ", minThreshold=" + getMinThreshold() +
            "}";
    }
}
