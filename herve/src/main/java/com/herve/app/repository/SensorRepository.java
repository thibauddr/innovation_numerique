package com.herve.app.repository;

import com.herve.app.domain.Field;
import com.herve.app.domain.Sensor;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the Sensor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long>, JpaSpecificationExecutor<Sensor> {

    @Query("select distinct sensor from Sensor sensor left join fetch sensor.sensorData left join fetch sensor.sensorType where sensor.field.id = :id")
    List<Sensor> findByFieldId(@Param("id") Long id);
}
