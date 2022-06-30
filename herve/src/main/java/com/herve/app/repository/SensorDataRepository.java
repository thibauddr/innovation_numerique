package com.herve.app.repository;

import com.herve.app.domain.SensorData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SensorData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long>, JpaSpecificationExecutor<SensorData> {}
