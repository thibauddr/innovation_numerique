package com.herve.app.web.rest;

import com.herve.app.domain.SensorType;
import com.herve.app.repository.SensorTypeRepository;
import com.herve.app.service.SensorTypeQueryService;
import com.herve.app.service.SensorTypeService;
import com.herve.app.service.criteria.SensorTypeCriteria;
import com.herve.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.herve.app.domain.SensorType}.
 */
@RestController
@RequestMapping("/api")
public class SensorTypeResource {

    private final Logger log = LoggerFactory.getLogger(SensorTypeResource.class);

    private static final String ENTITY_NAME = "sensorType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SensorTypeService sensorTypeService;

    private final SensorTypeRepository sensorTypeRepository;

    private final SensorTypeQueryService sensorTypeQueryService;

    public SensorTypeResource(
        SensorTypeService sensorTypeService,
        SensorTypeRepository sensorTypeRepository,
        SensorTypeQueryService sensorTypeQueryService
    ) {
        this.sensorTypeService = sensorTypeService;
        this.sensorTypeRepository = sensorTypeRepository;
        this.sensorTypeQueryService = sensorTypeQueryService;
    }

    /**
     * {@code POST  /sensor-types} : Create a new sensorType.
     *
     * @param sensorType the sensorType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sensorType, or with status {@code 400 (Bad Request)} if the sensorType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sensor-types")
    public ResponseEntity<SensorType> createSensorType(@RequestBody SensorType sensorType) throws URISyntaxException {
        log.debug("REST request to save SensorType : {}", sensorType);
        if (sensorType.getId() != null) {
            throw new BadRequestAlertException("A new sensorType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SensorType result = sensorTypeService.save(sensorType);
        return ResponseEntity
            .created(new URI("/api/sensor-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sensor-types/:id} : Updates an existing sensorType.
     *
     * @param id the id of the sensorType to save.
     * @param sensorType the sensorType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensorType,
     * or with status {@code 400 (Bad Request)} if the sensorType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sensorType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sensor-types/{id}")
    public ResponseEntity<SensorType> updateSensorType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SensorType sensorType
    ) throws URISyntaxException {
        log.debug("REST request to update SensorType : {}, {}", id, sensorType);
        if (sensorType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensorType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensorTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SensorType result = sensorTypeService.update(sensorType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensorType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sensor-types/:id} : Partial updates given fields of an existing sensorType, field will ignore if it is null
     *
     * @param id the id of the sensorType to save.
     * @param sensorType the sensorType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensorType,
     * or with status {@code 400 (Bad Request)} if the sensorType is not valid,
     * or with status {@code 404 (Not Found)} if the sensorType is not found,
     * or with status {@code 500 (Internal Server Error)} if the sensorType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sensor-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SensorType> partialUpdateSensorType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SensorType sensorType
    ) throws URISyntaxException {
        log.debug("REST request to partial update SensorType partially : {}, {}", id, sensorType);
        if (sensorType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensorType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensorTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SensorType> result = sensorTypeService.partialUpdate(sensorType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensorType.getId().toString())
        );
    }

    /**
     * {@code GET  /sensor-types} : get all the sensorTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sensorTypes in body.
     */
    @GetMapping("/sensor-types")
    public ResponseEntity<List<SensorType>> getAllSensorTypes(SensorTypeCriteria criteria) {
        log.debug("REST request to get SensorTypes by criteria: {}", criteria);
        List<SensorType> entityList = sensorTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /sensor-types/count} : count all the sensorTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sensor-types/count")
    public ResponseEntity<Long> countSensorTypes(SensorTypeCriteria criteria) {
        log.debug("REST request to count SensorTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(sensorTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sensor-types/:id} : get the "id" sensorType.
     *
     * @param id the id of the sensorType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sensorType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sensor-types/{id}")
    public ResponseEntity<SensorType> getSensorType(@PathVariable Long id) {
        log.debug("REST request to get SensorType : {}", id);
        Optional<SensorType> sensorType = sensorTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sensorType);
    }

    /**
     * {@code DELETE  /sensor-types/:id} : delete the "id" sensorType.
     *
     * @param id the id of the sensorType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sensor-types/{id}")
    public ResponseEntity<Void> deleteSensorType(@PathVariable Long id) {
        log.debug("REST request to delete SensorType : {}", id);
        sensorTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
