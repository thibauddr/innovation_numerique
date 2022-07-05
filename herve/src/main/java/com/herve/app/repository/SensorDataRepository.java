package com.herve.app.repository;

import com.herve.app.domain.SensorData;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data SQL repository for the SensorData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long>, JpaSpecificationExecutor<SensorData> {
    List<SensorData> findByDatetimeEquals(LocalDate date);

    @Query("select distinct sensorData from SensorData sensorData left join fetch sensorData.sensor as sensor join fetch sensor.field as field left join fetch sensor.sensorType where field.user.login = ?#{principal.username} and sensorData.datetime = :date")
    List<SensorData> findCurrentUserAlert(@Param("date") LocalDate date);
}
