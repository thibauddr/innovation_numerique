package com.herve.app.repository;

import com.herve.app.domain.SensorData;
import org.springframework.data.jpa.repository.*;
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
}
