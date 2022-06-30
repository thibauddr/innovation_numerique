package com.herve.app.repository;

import com.herve.app.domain.Sensor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Sensor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long>, JpaSpecificationExecutor<Sensor> {}
