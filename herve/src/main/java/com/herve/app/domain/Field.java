package com.herve.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Field.
 */
@Entity
@Table(name = "field")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Field implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "position")
    private String position;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "field")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "field", "sensorType", "sensorData" }, allowSetters = true)
    private Set<Sensor> sensors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Field id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Field name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Field description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosition() {
        return this.position;
    }

    public Field position(String position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Field user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Sensor> getSensors() {
        return this.sensors;
    }

    public void setSensors(Set<Sensor> sensors) {
        if (this.sensors != null) {
            this.sensors.forEach(i -> i.setField(null));
        }
        if (sensors != null) {
            sensors.forEach(i -> i.setField(this));
        }
        this.sensors = sensors;
    }

    public Field sensors(Set<Sensor> sensors) {
        this.setSensors(sensors);
        return this;
    }

    public Field addSensor(Sensor sensor) {
        this.sensors.add(sensor);
        sensor.setField(this);
        return this;
    }

    public Field removeSensor(Sensor sensor) {
        this.sensors.remove(sensor);
        sensor.setField(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Field)) {
            return false;
        }
        return id != null && id.equals(((Field) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Field{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", position='" + getPosition() + "'" +
            "}";
    }
}
