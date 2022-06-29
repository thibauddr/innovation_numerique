package com.herve.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SensorData.
 */
@Entity
@Table(name = "sensor_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SensorData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "unit", nullable = false)
    private String unit;

    @NotNull
    @Column(name = "value", nullable = false)
    private Float value;

    @Column(name = "datetime")
    private LocalDate datetime;

    @ManyToOne
    @JsonIgnoreProperties(value = { "field", "sensorType", "sensorData" }, allowSetters = true)
    private Sensor sensor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SensorData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnit() {
        return this.unit;
    }

    public SensorData unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Float getValue() {
        return this.value;
    }

    public SensorData value(Float value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public LocalDate getDatetime() {
        return this.datetime;
    }

    public SensorData datetime(LocalDate datetime) {
        this.setDatetime(datetime);
        return this;
    }

    public void setDatetime(LocalDate datetime) {
        this.datetime = datetime;
    }

    public Sensor getSensor() {
        return this.sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public SensorData sensor(Sensor sensor) {
        this.setSensor(sensor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SensorData)) {
            return false;
        }
        return id != null && id.equals(((SensorData) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SensorData{" +
            "id=" + getId() +
            ", unit='" + getUnit() + "'" +
            ", value=" + getValue() +
            ", datetime='" + getDatetime() + "'" +
            "}";
    }
}
