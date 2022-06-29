package com.herve.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.herve.app.IntegrationTest;
import com.herve.app.domain.Sensor;
import com.herve.app.domain.SensorData;
import com.herve.app.repository.SensorDataRepository;
import com.herve.app.service.criteria.SensorDataCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SensorDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SensorDataResourceIT {

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final Float DEFAULT_VALUE = 1F;
    private static final Float UPDATED_VALUE = 2F;
    private static final Float SMALLER_VALUE = 1F - 1F;

    private static final LocalDate DEFAULT_DATETIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATETIME = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATETIME = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/sensor-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSensorDataMockMvc;

    private SensorData sensorData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SensorData createEntity(EntityManager em) {
        SensorData sensorData = new SensorData().unit(DEFAULT_UNIT).value(DEFAULT_VALUE).datetime(DEFAULT_DATETIME);
        return sensorData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SensorData createUpdatedEntity(EntityManager em) {
        SensorData sensorData = new SensorData().unit(UPDATED_UNIT).value(UPDATED_VALUE).datetime(UPDATED_DATETIME);
        return sensorData;
    }

    @BeforeEach
    public void initTest() {
        sensorData = createEntity(em);
    }

    @Test
    @Transactional
    void createSensorData() throws Exception {
        int databaseSizeBeforeCreate = sensorDataRepository.findAll().size();
        // Create the SensorData
        restSensorDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensorData))
            )
            .andExpect(status().isCreated());

        // Validate the SensorData in the database
        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        assertThat(sensorDataList).hasSize(databaseSizeBeforeCreate + 1);
        SensorData testSensorData = sensorDataList.get(sensorDataList.size() - 1);
        assertThat(testSensorData.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testSensorData.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testSensorData.getDatetime()).isEqualTo(DEFAULT_DATETIME);
    }

    @Test
    @Transactional
    void createSensorDataWithExistingId() throws Exception {
        // Create the SensorData with an existing ID
        sensorData.setId(1L);

        int databaseSizeBeforeCreate = sensorDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensorDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensorData))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensorData in the database
        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        assertThat(sensorDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = sensorDataRepository.findAll().size();
        // set the field null
        sensorData.setUnit(null);

        // Create the SensorData, which fails.

        restSensorDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensorData))
            )
            .andExpect(status().isBadRequest());

        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        assertThat(sensorDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = sensorDataRepository.findAll().size();
        // set the field null
        sensorData.setValue(null);

        // Create the SensorData, which fails.

        restSensorDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensorData))
            )
            .andExpect(status().isBadRequest());

        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        assertThat(sensorDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSensorData() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList
        restSensorDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensorData.getId().intValue())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(DEFAULT_DATETIME.toString())));
    }

    @Test
    @Transactional
    void getSensorData() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get the sensorData
        restSensorDataMockMvc
            .perform(get(ENTITY_API_URL_ID, sensorData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sensorData.getId().intValue()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.datetime").value(DEFAULT_DATETIME.toString()));
    }

    @Test
    @Transactional
    void getSensorDataByIdFiltering() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        Long id = sensorData.getId();

        defaultSensorDataShouldBeFound("id.equals=" + id);
        defaultSensorDataShouldNotBeFound("id.notEquals=" + id);

        defaultSensorDataShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSensorDataShouldNotBeFound("id.greaterThan=" + id);

        defaultSensorDataShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSensorDataShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSensorDataByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where unit equals to DEFAULT_UNIT
        defaultSensorDataShouldBeFound("unit.equals=" + DEFAULT_UNIT);

        // Get all the sensorDataList where unit equals to UPDATED_UNIT
        defaultSensorDataShouldNotBeFound("unit.equals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllSensorDataByUnitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where unit not equals to DEFAULT_UNIT
        defaultSensorDataShouldNotBeFound("unit.notEquals=" + DEFAULT_UNIT);

        // Get all the sensorDataList where unit not equals to UPDATED_UNIT
        defaultSensorDataShouldBeFound("unit.notEquals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllSensorDataByUnitIsInShouldWork() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where unit in DEFAULT_UNIT or UPDATED_UNIT
        defaultSensorDataShouldBeFound("unit.in=" + DEFAULT_UNIT + "," + UPDATED_UNIT);

        // Get all the sensorDataList where unit equals to UPDATED_UNIT
        defaultSensorDataShouldNotBeFound("unit.in=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllSensorDataByUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where unit is not null
        defaultSensorDataShouldBeFound("unit.specified=true");

        // Get all the sensorDataList where unit is null
        defaultSensorDataShouldNotBeFound("unit.specified=false");
    }

    @Test
    @Transactional
    void getAllSensorDataByUnitContainsSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where unit contains DEFAULT_UNIT
        defaultSensorDataShouldBeFound("unit.contains=" + DEFAULT_UNIT);

        // Get all the sensorDataList where unit contains UPDATED_UNIT
        defaultSensorDataShouldNotBeFound("unit.contains=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllSensorDataByUnitNotContainsSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where unit does not contain DEFAULT_UNIT
        defaultSensorDataShouldNotBeFound("unit.doesNotContain=" + DEFAULT_UNIT);

        // Get all the sensorDataList where unit does not contain UPDATED_UNIT
        defaultSensorDataShouldBeFound("unit.doesNotContain=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllSensorDataByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where value equals to DEFAULT_VALUE
        defaultSensorDataShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the sensorDataList where value equals to UPDATED_VALUE
        defaultSensorDataShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSensorDataByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where value not equals to DEFAULT_VALUE
        defaultSensorDataShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the sensorDataList where value not equals to UPDATED_VALUE
        defaultSensorDataShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSensorDataByValueIsInShouldWork() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultSensorDataShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the sensorDataList where value equals to UPDATED_VALUE
        defaultSensorDataShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSensorDataByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where value is not null
        defaultSensorDataShouldBeFound("value.specified=true");

        // Get all the sensorDataList where value is null
        defaultSensorDataShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllSensorDataByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where value is greater than or equal to DEFAULT_VALUE
        defaultSensorDataShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the sensorDataList where value is greater than or equal to UPDATED_VALUE
        defaultSensorDataShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSensorDataByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where value is less than or equal to DEFAULT_VALUE
        defaultSensorDataShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the sensorDataList where value is less than or equal to SMALLER_VALUE
        defaultSensorDataShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllSensorDataByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where value is less than DEFAULT_VALUE
        defaultSensorDataShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the sensorDataList where value is less than UPDATED_VALUE
        defaultSensorDataShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSensorDataByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where value is greater than DEFAULT_VALUE
        defaultSensorDataShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the sensorDataList where value is greater than SMALLER_VALUE
        defaultSensorDataShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllSensorDataByDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where datetime equals to DEFAULT_DATETIME
        defaultSensorDataShouldBeFound("datetime.equals=" + DEFAULT_DATETIME);

        // Get all the sensorDataList where datetime equals to UPDATED_DATETIME
        defaultSensorDataShouldNotBeFound("datetime.equals=" + UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void getAllSensorDataByDatetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where datetime not equals to DEFAULT_DATETIME
        defaultSensorDataShouldNotBeFound("datetime.notEquals=" + DEFAULT_DATETIME);

        // Get all the sensorDataList where datetime not equals to UPDATED_DATETIME
        defaultSensorDataShouldBeFound("datetime.notEquals=" + UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void getAllSensorDataByDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where datetime in DEFAULT_DATETIME or UPDATED_DATETIME
        defaultSensorDataShouldBeFound("datetime.in=" + DEFAULT_DATETIME + "," + UPDATED_DATETIME);

        // Get all the sensorDataList where datetime equals to UPDATED_DATETIME
        defaultSensorDataShouldNotBeFound("datetime.in=" + UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void getAllSensorDataByDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where datetime is not null
        defaultSensorDataShouldBeFound("datetime.specified=true");

        // Get all the sensorDataList where datetime is null
        defaultSensorDataShouldNotBeFound("datetime.specified=false");
    }

    @Test
    @Transactional
    void getAllSensorDataByDatetimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where datetime is greater than or equal to DEFAULT_DATETIME
        defaultSensorDataShouldBeFound("datetime.greaterThanOrEqual=" + DEFAULT_DATETIME);

        // Get all the sensorDataList where datetime is greater than or equal to UPDATED_DATETIME
        defaultSensorDataShouldNotBeFound("datetime.greaterThanOrEqual=" + UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void getAllSensorDataByDatetimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where datetime is less than or equal to DEFAULT_DATETIME
        defaultSensorDataShouldBeFound("datetime.lessThanOrEqual=" + DEFAULT_DATETIME);

        // Get all the sensorDataList where datetime is less than or equal to SMALLER_DATETIME
        defaultSensorDataShouldNotBeFound("datetime.lessThanOrEqual=" + SMALLER_DATETIME);
    }

    @Test
    @Transactional
    void getAllSensorDataByDatetimeIsLessThanSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where datetime is less than DEFAULT_DATETIME
        defaultSensorDataShouldNotBeFound("datetime.lessThan=" + DEFAULT_DATETIME);

        // Get all the sensorDataList where datetime is less than UPDATED_DATETIME
        defaultSensorDataShouldBeFound("datetime.lessThan=" + UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void getAllSensorDataByDatetimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        // Get all the sensorDataList where datetime is greater than DEFAULT_DATETIME
        defaultSensorDataShouldNotBeFound("datetime.greaterThan=" + DEFAULT_DATETIME);

        // Get all the sensorDataList where datetime is greater than SMALLER_DATETIME
        defaultSensorDataShouldBeFound("datetime.greaterThan=" + SMALLER_DATETIME);
    }

    @Test
    @Transactional
    void getAllSensorDataBySensorIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);
        Sensor sensor;
        if (TestUtil.findAll(em, Sensor.class).isEmpty()) {
            sensor = SensorResourceIT.createEntity(em);
            em.persist(sensor);
            em.flush();
        } else {
            sensor = TestUtil.findAll(em, Sensor.class).get(0);
        }
        em.persist(sensor);
        em.flush();
        sensorData.setSensor(sensor);
        sensorDataRepository.saveAndFlush(sensorData);
        Long sensorId = sensor.getId();

        // Get all the sensorDataList where sensor equals to sensorId
        defaultSensorDataShouldBeFound("sensorId.equals=" + sensorId);

        // Get all the sensorDataList where sensor equals to (sensorId + 1)
        defaultSensorDataShouldNotBeFound("sensorId.equals=" + (sensorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSensorDataShouldBeFound(String filter) throws Exception {
        restSensorDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensorData.getId().intValue())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(DEFAULT_DATETIME.toString())));

        // Check, that the count call also returns 1
        restSensorDataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSensorDataShouldNotBeFound(String filter) throws Exception {
        restSensorDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSensorDataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSensorData() throws Exception {
        // Get the sensorData
        restSensorDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSensorData() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        int databaseSizeBeforeUpdate = sensorDataRepository.findAll().size();

        // Update the sensorData
        SensorData updatedSensorData = sensorDataRepository.findById(sensorData.getId()).get();
        // Disconnect from session so that the updates on updatedSensorData are not directly saved in db
        em.detach(updatedSensorData);
        updatedSensorData.unit(UPDATED_UNIT).value(UPDATED_VALUE).datetime(UPDATED_DATETIME);

        restSensorDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSensorData.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSensorData))
            )
            .andExpect(status().isOk());

        // Validate the SensorData in the database
        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        assertThat(sensorDataList).hasSize(databaseSizeBeforeUpdate);
        SensorData testSensorData = sensorDataList.get(sensorDataList.size() - 1);
        assertThat(testSensorData.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testSensorData.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testSensorData.getDatetime()).isEqualTo(UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void putNonExistingSensorData() throws Exception {
        int databaseSizeBeforeUpdate = sensorDataRepository.findAll().size();
        sensorData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensorDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sensorData.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensorData))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensorData in the database
        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        assertThat(sensorDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSensorData() throws Exception {
        int databaseSizeBeforeUpdate = sensorDataRepository.findAll().size();
        sensorData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensorData))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensorData in the database
        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        assertThat(sensorDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSensorData() throws Exception {
        int databaseSizeBeforeUpdate = sensorDataRepository.findAll().size();
        sensorData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorDataMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensorData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SensorData in the database
        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        assertThat(sensorDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSensorDataWithPatch() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        int databaseSizeBeforeUpdate = sensorDataRepository.findAll().size();

        // Update the sensorData using partial update
        SensorData partialUpdatedSensorData = new SensorData();
        partialUpdatedSensorData.setId(sensorData.getId());

        partialUpdatedSensorData.datetime(UPDATED_DATETIME);

        restSensorDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensorData.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSensorData))
            )
            .andExpect(status().isOk());

        // Validate the SensorData in the database
        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        assertThat(sensorDataList).hasSize(databaseSizeBeforeUpdate);
        SensorData testSensorData = sensorDataList.get(sensorDataList.size() - 1);
        assertThat(testSensorData.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testSensorData.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testSensorData.getDatetime()).isEqualTo(UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void fullUpdateSensorDataWithPatch() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        int databaseSizeBeforeUpdate = sensorDataRepository.findAll().size();

        // Update the sensorData using partial update
        SensorData partialUpdatedSensorData = new SensorData();
        partialUpdatedSensorData.setId(sensorData.getId());

        partialUpdatedSensorData.unit(UPDATED_UNIT).value(UPDATED_VALUE).datetime(UPDATED_DATETIME);

        restSensorDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensorData.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSensorData))
            )
            .andExpect(status().isOk());

        // Validate the SensorData in the database
        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        assertThat(sensorDataList).hasSize(databaseSizeBeforeUpdate);
        SensorData testSensorData = sensorDataList.get(sensorDataList.size() - 1);
        assertThat(testSensorData.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testSensorData.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testSensorData.getDatetime()).isEqualTo(UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void patchNonExistingSensorData() throws Exception {
        int databaseSizeBeforeUpdate = sensorDataRepository.findAll().size();
        sensorData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensorDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sensorData.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensorData))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensorData in the database
        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        assertThat(sensorDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSensorData() throws Exception {
        int databaseSizeBeforeUpdate = sensorDataRepository.findAll().size();
        sensorData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensorData))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensorData in the database
        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        assertThat(sensorDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSensorData() throws Exception {
        int databaseSizeBeforeUpdate = sensorDataRepository.findAll().size();
        sensorData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorDataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensorData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SensorData in the database
        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        assertThat(sensorDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSensorData() throws Exception {
        // Initialize the database
        sensorDataRepository.saveAndFlush(sensorData);

        int databaseSizeBeforeDelete = sensorDataRepository.findAll().size();

        // Delete the sensorData
        restSensorDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, sensorData.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        assertThat(sensorDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
