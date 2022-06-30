package com.herve.app.web.rest;

import com.herve.app.domain.Sensor;
import com.herve.app.repository.SensorRepository;
import com.herve.app.service.SensorQueryService;
import com.herve.app.service.SensorService;
import com.herve.app.service.criteria.SensorCriteria;
import com.herve.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.herve.app.domain.Sensor}.
 */
@RestController
@RequestMapping("/api")
public class SensorResource {

    private final Logger log = LoggerFactory.getLogger(SensorResource.class);

    private static final String ENTITY_NAME = "sensor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SensorService sensorService;

    private final SensorRepository sensorRepository;

    private final SensorQueryService sensorQueryService;

    public SensorResource(SensorService sensorService, SensorRepository sensorRepository, SensorQueryService sensorQueryService) {
        this.sensorService = sensorService;
        this.sensorRepository = sensorRepository;
        this.sensorQueryService = sensorQueryService;
    }

    /**
     * {@code POST  /sensors} : Create a new sensor.
     *
     * @param sensor the sensor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sensor, or with status {@code 400 (Bad Request)} if the sensor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sensors")
    public ResponseEntity<Sensor> createSensor(@Valid @RequestBody Sensor sensor) throws URISyntaxException {
        log.debug("REST request to save Sensor : {}", sensor);
        if (sensor.getId() != null) {
            throw new BadRequestAlertException("A new sensor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sensor result = sensorService.save(sensor);
        return ResponseEntity
            .created(new URI("/api/sensors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sensors/:id} : Updates an existing sensor.
     *
     * @param id the id of the sensor to save.
     * @param sensor the sensor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensor,
     * or with status {@code 400 (Bad Request)} if the sensor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sensor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sensors/{id}")
    public ResponseEntity<Sensor> updateSensor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Sensor sensor
    ) throws URISyntaxException {
        log.debug("REST request to update Sensor : {}, {}", id, sensor);
        if (sensor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sensor result = sensorService.update(sensor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sensors/:id} : Partial updates given fields of an existing sensor, field will ignore if it is null
     *
     * @param id the id of the sensor to save.
     * @param sensor the sensor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensor,
     * or with status {@code 400 (Bad Request)} if the sensor is not valid,
     * or with status {@code 404 (Not Found)} if the sensor is not found,
     * or with status {@code 500 (Internal Server Error)} if the sensor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sensors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Sensor> partialUpdateSensor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Sensor sensor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sensor partially : {}, {}", id, sensor);
        if (sensor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sensor> result = sensorService.partialUpdate(sensor);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensor.getId().toString())
        );
    }

    /**
     * {@code GET  /sensors} : get all the sensors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sensors in body.
     */
    @GetMapping("/sensors")
    public ResponseEntity<List<Sensor>> getAllSensors(SensorCriteria criteria) {
        log.debug("REST request to get Sensors by criteria: {}", criteria);
        List<Sensor> entityList = sensorQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /sensors/count} : count all the sensors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sensors/count")
    public ResponseEntity<Long> countSensors(SensorCriteria criteria) {
        log.debug("REST request to count Sensors by criteria: {}", criteria);
        return ResponseEntity.ok().body(sensorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sensors/:id} : get the "id" sensor.
     *
     * @param id the id of the sensor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sensor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sensors/{id}")
    public ResponseEntity<Sensor> getSensor(@PathVariable Long id) {
        log.debug("REST request to get Sensor : {}", id);
        Optional<Sensor> sensor = sensorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sensor);
    }

    /**
     * {@code DELETE  /sensors/:id} : delete the "id" sensor.
     *
     * @param id the id of the sensor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sensors/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        log.debug("REST request to delete Sensor : {}", id);
        sensorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
