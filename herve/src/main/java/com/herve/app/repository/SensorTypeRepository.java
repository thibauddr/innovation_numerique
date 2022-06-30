package com.herve.app.repository;

import com.herve.app.domain.SensorType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SensorType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorTypeRepository extends JpaRepository<SensorType, Long>, JpaSpecificationExecutor<SensorType> {}
