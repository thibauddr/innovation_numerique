package com.herve.app.web.rest;

import com.herve.app.domain.SensorData;
import com.herve.app.repository.SensorDataRepository;
import com.herve.app.service.SensorDataQueryService;
import com.herve.app.service.SensorDataService;
import com.herve.app.service.criteria.SensorDataCriteria;
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
 * REST controller for managing {@link com.herve.app.domain.SensorData}.
 */
@RestController
@RequestMapping("/api")
public class SensorDataResource {

    private final Logger log = LoggerFactory.getLogger(SensorDataResource.class);

    private static final String ENTITY_NAME = "sensorData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SensorDataService sensorDataService;

    private final SensorDataRepository sensorDataRepository;

    private final SensorDataQueryService sensorDataQueryService;

    public SensorDataResource(
        SensorDataService sensorDataService,
        SensorDataRepository sensorDataRepository,
        SensorDataQueryService sensorDataQueryService
    ) {
        this.sensorDataService = sensorDataService;
        this.sensorDataRepository = sensorDataRepository;
        this.sensorDataQueryService = sensorDataQueryService;
    }

    /**
     * {@code POST  /sensor-data} : Create a new sensorData.
     *
     * @param sensorData the sensorData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sensorData, or with status {@code 400 (Bad Request)} if the sensorData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sensor-data")
    public ResponseEntity<SensorData> createSensorData(@Valid @RequestBody SensorData sensorData) throws URISyntaxException {
        log.debug("REST request to save SensorData : {}", sensorData);
        if (sensorData.getId() != null) {
            throw new BadRequestAlertException("A new sensorData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SensorData result = sensorDataService.save(sensorData);
        return ResponseEntity
            .created(new URI("/api/sensor-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sensor-data/:id} : Updates an existing sensorData.
     *
     * @param id the id of the sensorData to save.
     * @param sensorData the sensorData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensorData,
     * or with status {@code 400 (Bad Request)} if the sensorData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sensorData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sensor-data/{id}")
    public ResponseEntity<SensorData> updateSensorData(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SensorData sensorData
    ) throws URISyntaxException {
        log.debug("REST request to update SensorData : {}, {}", id, sensorData);
        if (sensorData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensorData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensorDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SensorData result = sensorDataService.update(sensorData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensorData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sensor-data/:id} : Partial updates given fields of an existing sensorData, field will ignore if it is null
     *
     * @param id the id of the sensorData to save.
     * @param sensorData the sensorData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensorData,
     * or with status {@code 400 (Bad Request)} if the sensorData is not valid,
     * or with status {@code 404 (Not Found)} if the sensorData is not found,
     * or with status {@code 500 (Internal Server Error)} if the sensorData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sensor-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SensorData> partialUpdateSensorData(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SensorData sensorData
    ) throws URISyntaxException {
        log.debug("REST request to partial update SensorData partially : {}, {}", id, sensorData);
        if (sensorData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensorData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensorDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SensorData> result = sensorDataService.partialUpdate(sensorData);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensorData.getId().toString())
        );
    }

    /**
     * {@code GET  /sensor-data} : get all the sensorData.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sensorData in body.
     */
    @GetMapping("/sensor-data")
    public ResponseEntity<List<SensorData>> getAllSensorData(SensorDataCriteria criteria) {
        log.debug("REST request to get SensorData by criteria: {}", criteria);
        List<SensorData> entityList = sensorDataQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /sensor-data} : get sensorData where datetime = now.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sensorData in body.
     */
    @GetMapping("/sensor-data/now")
    public ResponseEntity<List<SensorData>> getNowSensorData(SensorDataCriteria criteria) {
        log.debug("REST request to get SensorData by criteria: {}", criteria);
        List<SensorData> entityList = sensorDataQueryService.findNow(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /sensor-data/count} : count all the sensorData.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sensor-data/count")
    public ResponseEntity<Long> countSensorData(SensorDataCriteria criteria) {
        log.debug("REST request to count SensorData by criteria: {}", criteria);
        return ResponseEntity.ok().body(sensorDataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sensor-data/:id} : get the "id" sensorData.
     *
     * @param id the id of the sensorData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sensorData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sensor-data/{id}")
    public ResponseEntity<SensorData> getSensorData(@PathVariable Long id) {
        log.debug("REST request to get SensorData : {}", id);
        Optional<SensorData> sensorData = sensorDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sensorData);
    }

    /**
     * {@code DELETE  /sensor-data/:id} : delete the "id" sensorData.
     *
     * @param id the id of the sensorData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sensor-data/{id}")
    public ResponseEntity<Void> deleteSensorData(@PathVariable Long id) {
        log.debug("REST request to delete SensorData : {}", id);
        sensorDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
