package com.herve.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.herve.app.IntegrationTest;
import com.herve.app.domain.Field;
import com.herve.app.domain.Sensor;
import com.herve.app.domain.SensorData;
import com.herve.app.domain.SensorType;
import com.herve.app.repository.SensorRepository;
import com.herve.app.service.criteria.SensorCriteria;
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
 * Integration tests for the {@link SensorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SensorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_POSITION_X = 1;
    private static final Integer UPDATED_POSITION_X = 2;
    private static final Integer SMALLER_POSITION_X = 1 - 1;

    private static final Integer DEFAULT_POSITION_Y = 1;
    private static final Integer UPDATED_POSITION_Y = 2;
    private static final Integer SMALLER_POSITION_Y = 1 - 1;

    private static final String ENTITY_API_URL = "/api/sensors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSensorMockMvc;

    private Sensor sensor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sensor createEntity(EntityManager em) {
        Sensor sensor = new Sensor()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .position_x(DEFAULT_POSITION_X)
            .position_y(DEFAULT_POSITION_Y);
        // Add required entity
        SensorType sensorType;
        if (TestUtil.findAll(em, SensorType.class).isEmpty()) {
            sensorType = SensorTypeResourceIT.createEntity(em);
            em.persist(sensorType);
            em.flush();
        } else {
            sensorType = TestUtil.findAll(em, SensorType.class).get(0);
        }
        sensor.setSensorType(sensorType);
        return sensor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sensor createUpdatedEntity(EntityManager em) {
        Sensor sensor = new Sensor()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .position_x(UPDATED_POSITION_X)
            .position_y(UPDATED_POSITION_Y);
        // Add required entity
        SensorType sensorType;
        if (TestUtil.findAll(em, SensorType.class).isEmpty()) {
            sensorType = SensorTypeResourceIT.createUpdatedEntity(em);
            em.persist(sensorType);
            em.flush();
        } else {
            sensorType = TestUtil.findAll(em, SensorType.class).get(0);
        }
        sensor.setSensorType(sensorType);
        return sensor;
    }

    @BeforeEach
    public void initTest() {
        sensor = createEntity(em);
    }

    @Test
    @Transactional
    void createSensor() throws Exception {
        int databaseSizeBeforeCreate = sensorRepository.findAll().size();
        // Create the Sensor
        restSensorMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sensor))
            )
            .andExpect(status().isCreated());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate + 1);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSensor.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSensor.getPosition_x()).isEqualTo(DEFAULT_POSITION_X);
        assertThat(testSensor.getPosition_y()).isEqualTo(DEFAULT_POSITION_Y);
    }

    @Test
    @Transactional
    void createSensorWithExistingId() throws Exception {
        // Create the Sensor with an existing ID
        sensor.setId(1L);

        int databaseSizeBeforeCreate = sensorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensorMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sensor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSensors() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList
        restSensorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].position_x").value(hasItem(DEFAULT_POSITION_X)))
            .andExpect(jsonPath("$.[*].position_y").value(hasItem(DEFAULT_POSITION_Y)));
    }

    @Test
    @Transactional
    void getSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get the sensor
        restSensorMockMvc
            .perform(get(ENTITY_API_URL_ID, sensor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sensor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.position_x").value(DEFAULT_POSITION_X))
            .andExpect(jsonPath("$.position_y").value(DEFAULT_POSITION_Y));
    }

    @Test
    @Transactional
    void getSensorsByIdFiltering() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        Long id = sensor.getId();

        defaultSensorShouldBeFound("id.equals=" + id);
        defaultSensorShouldNotBeFound("id.notEquals=" + id);

        defaultSensorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSensorShouldNotBeFound("id.greaterThan=" + id);

        defaultSensorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSensorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSensorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where name equals to DEFAULT_NAME
        defaultSensorShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the sensorList where name equals to UPDATED_NAME
        defaultSensorShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSensorsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where name not equals to DEFAULT_NAME
        defaultSensorShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the sensorList where name not equals to UPDATED_NAME
        defaultSensorShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSensorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSensorShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the sensorList where name equals to UPDATED_NAME
        defaultSensorShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSensorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where name is not null
        defaultSensorShouldBeFound("name.specified=true");

        // Get all the sensorList where name is null
        defaultSensorShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSensorsByNameContainsSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where name contains DEFAULT_NAME
        defaultSensorShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the sensorList where name contains UPDATED_NAME
        defaultSensorShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSensorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where name does not contain DEFAULT_NAME
        defaultSensorShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the sensorList where name does not contain UPDATED_NAME
        defaultSensorShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSensorsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where description equals to DEFAULT_DESCRIPTION
        defaultSensorShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the sensorList where description equals to UPDATED_DESCRIPTION
        defaultSensorShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSensorsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where description not equals to DEFAULT_DESCRIPTION
        defaultSensorShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the sensorList where description not equals to UPDATED_DESCRIPTION
        defaultSensorShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSensorsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSensorShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the sensorList where description equals to UPDATED_DESCRIPTION
        defaultSensorShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSensorsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where description is not null
        defaultSensorShouldBeFound("description.specified=true");

        // Get all the sensorList where description is null
        defaultSensorShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSensorsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where description contains DEFAULT_DESCRIPTION
        defaultSensorShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the sensorList where description contains UPDATED_DESCRIPTION
        defaultSensorShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSensorsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where description does not contain DEFAULT_DESCRIPTION
        defaultSensorShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the sensorList where description does not contain UPDATED_DESCRIPTION
        defaultSensorShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_xIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_x equals to DEFAULT_POSITION_X
        defaultSensorShouldBeFound("position_x.equals=" + DEFAULT_POSITION_X);

        // Get all the sensorList where position_x equals to UPDATED_POSITION_X
        defaultSensorShouldNotBeFound("position_x.equals=" + UPDATED_POSITION_X);
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_xIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_x not equals to DEFAULT_POSITION_X
        defaultSensorShouldNotBeFound("position_x.notEquals=" + DEFAULT_POSITION_X);

        // Get all the sensorList where position_x not equals to UPDATED_POSITION_X
        defaultSensorShouldBeFound("position_x.notEquals=" + UPDATED_POSITION_X);
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_xIsInShouldWork() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_x in DEFAULT_POSITION_X or UPDATED_POSITION_X
        defaultSensorShouldBeFound("position_x.in=" + DEFAULT_POSITION_X + "," + UPDATED_POSITION_X);

        // Get all the sensorList where position_x equals to UPDATED_POSITION_X
        defaultSensorShouldNotBeFound("position_x.in=" + UPDATED_POSITION_X);
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_xIsNullOrNotNull() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_x is not null
        defaultSensorShouldBeFound("position_x.specified=true");

        // Get all the sensorList where position_x is null
        defaultSensorShouldNotBeFound("position_x.specified=false");
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_xIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_x is greater than or equal to DEFAULT_POSITION_X
        defaultSensorShouldBeFound("position_x.greaterThanOrEqual=" + DEFAULT_POSITION_X);

        // Get all the sensorList where position_x is greater than or equal to UPDATED_POSITION_X
        defaultSensorShouldNotBeFound("position_x.greaterThanOrEqual=" + UPDATED_POSITION_X);
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_xIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_x is less than or equal to DEFAULT_POSITION_X
        defaultSensorShouldBeFound("position_x.lessThanOrEqual=" + DEFAULT_POSITION_X);

        // Get all the sensorList where position_x is less than or equal to SMALLER_POSITION_X
        defaultSensorShouldNotBeFound("position_x.lessThanOrEqual=" + SMALLER_POSITION_X);
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_xIsLessThanSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_x is less than DEFAULT_POSITION_X
        defaultSensorShouldNotBeFound("position_x.lessThan=" + DEFAULT_POSITION_X);

        // Get all the sensorList where position_x is less than UPDATED_POSITION_X
        defaultSensorShouldBeFound("position_x.lessThan=" + UPDATED_POSITION_X);
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_xIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_x is greater than DEFAULT_POSITION_X
        defaultSensorShouldNotBeFound("position_x.greaterThan=" + DEFAULT_POSITION_X);

        // Get all the sensorList where position_x is greater than SMALLER_POSITION_X
        defaultSensorShouldBeFound("position_x.greaterThan=" + SMALLER_POSITION_X);
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_yIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_y equals to DEFAULT_POSITION_Y
        defaultSensorShouldBeFound("position_y.equals=" + DEFAULT_POSITION_Y);

        // Get all the sensorList where position_y equals to UPDATED_POSITION_Y
        defaultSensorShouldNotBeFound("position_y.equals=" + UPDATED_POSITION_Y);
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_yIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_y not equals to DEFAULT_POSITION_Y
        defaultSensorShouldNotBeFound("position_y.notEquals=" + DEFAULT_POSITION_Y);

        // Get all the sensorList where position_y not equals to UPDATED_POSITION_Y
        defaultSensorShouldBeFound("position_y.notEquals=" + UPDATED_POSITION_Y);
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_yIsInShouldWork() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_y in DEFAULT_POSITION_Y or UPDATED_POSITION_Y
        defaultSensorShouldBeFound("position_y.in=" + DEFAULT_POSITION_Y + "," + UPDATED_POSITION_Y);

        // Get all the sensorList where position_y equals to UPDATED_POSITION_Y
        defaultSensorShouldNotBeFound("position_y.in=" + UPDATED_POSITION_Y);
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_yIsNullOrNotNull() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_y is not null
        defaultSensorShouldBeFound("position_y.specified=true");

        // Get all the sensorList where position_y is null
        defaultSensorShouldNotBeFound("position_y.specified=false");
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_yIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_y is greater than or equal to DEFAULT_POSITION_Y
        defaultSensorShouldBeFound("position_y.greaterThanOrEqual=" + DEFAULT_POSITION_Y);

        // Get all the sensorList where position_y is greater than or equal to UPDATED_POSITION_Y
        defaultSensorShouldNotBeFound("position_y.greaterThanOrEqual=" + UPDATED_POSITION_Y);
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_yIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_y is less than or equal to DEFAULT_POSITION_Y
        defaultSensorShouldBeFound("position_y.lessThanOrEqual=" + DEFAULT_POSITION_Y);

        // Get all the sensorList where position_y is less than or equal to SMALLER_POSITION_Y
        defaultSensorShouldNotBeFound("position_y.lessThanOrEqual=" + SMALLER_POSITION_Y);
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_yIsLessThanSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_y is less than DEFAULT_POSITION_Y
        defaultSensorShouldNotBeFound("position_y.lessThan=" + DEFAULT_POSITION_Y);

        // Get all the sensorList where position_y is less than UPDATED_POSITION_Y
        defaultSensorShouldBeFound("position_y.lessThan=" + UPDATED_POSITION_Y);
    }

    @Test
    @Transactional
    void getAllSensorsByPosition_yIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where position_y is greater than DEFAULT_POSITION_Y
        defaultSensorShouldNotBeFound("position_y.greaterThan=" + DEFAULT_POSITION_Y);

        // Get all the sensorList where position_y is greater than SMALLER_POSITION_Y
        defaultSensorShouldBeFound("position_y.greaterThan=" + SMALLER_POSITION_Y);
    }

    @Test
    @Transactional
    void getAllSensorsByFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);
        Field field;
        if (TestUtil.findAll(em, Field.class).isEmpty()) {
            field = FieldResourceIT.createEntity(em);
            em.persist(field);
            em.flush();
        } else {
            field = TestUtil.findAll(em, Field.class).get(0);
        }
        em.persist(field);
        em.flush();
        sensor.setField(field);
        sensorRepository.saveAndFlush(sensor);
        Long fieldId = field.getId();

        // Get all the sensorList where field equals to fieldId
        defaultSensorShouldBeFound("fieldId.equals=" + fieldId);

        // Get all the sensorList where field equals to (fieldId + 1)
        defaultSensorShouldNotBeFound("fieldId.equals=" + (fieldId + 1));
    }

    @Test
    @Transactional
    void getAllSensorsBySensorTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);
        SensorType sensorType;
        if (TestUtil.findAll(em, SensorType.class).isEmpty()) {
            sensorType = SensorTypeResourceIT.createEntity(em);
            em.persist(sensorType);
            em.flush();
        } else {
            sensorType = TestUtil.findAll(em, SensorType.class).get(0);
        }
        em.persist(sensorType);
        em.flush();
        sensor.setSensorType(sensorType);
        sensorRepository.saveAndFlush(sensor);
        Long sensorTypeId = sensorType.getId();

        // Get all the sensorList where sensorType equals to sensorTypeId
        defaultSensorShouldBeFound("sensorTypeId.equals=" + sensorTypeId);

        // Get all the sensorList where sensorType equals to (sensorTypeId + 1)
        defaultSensorShouldNotBeFound("sensorTypeId.equals=" + (sensorTypeId + 1));
    }

    @Test
    @Transactional
    void getAllSensorsBySensorDataIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);
        SensorData sensorData;
        if (TestUtil.findAll(em, SensorData.class).isEmpty()) {
            sensorData = SensorDataResourceIT.createEntity(em);
            em.persist(sensorData);
            em.flush();
        } else {
            sensorData = TestUtil.findAll(em, SensorData.class).get(0);
        }
        em.persist(sensorData);
        em.flush();
        sensor.addSensorData(sensorData);
        sensorRepository.saveAndFlush(sensor);
        Long sensorDataId = sensorData.getId();

        // Get all the sensorList where sensorData equals to sensorDataId
        defaultSensorShouldBeFound("sensorDataId.equals=" + sensorDataId);

        // Get all the sensorList where sensorData equals to (sensorDataId + 1)
        defaultSensorShouldNotBeFound("sensorDataId.equals=" + (sensorDataId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSensorShouldBeFound(String filter) throws Exception {
        restSensorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].position_x").value(hasItem(DEFAULT_POSITION_X)))
            .andExpect(jsonPath("$.[*].position_y").value(hasItem(DEFAULT_POSITION_Y)));

        // Check, that the count call also returns 1
        restSensorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSensorShouldNotBeFound(String filter) throws Exception {
        restSensorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSensorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSensor() throws Exception {
        // Get the sensor
        restSensorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Update the sensor
        Sensor updatedSensor = sensorRepository.findById(sensor.getId()).get();
        // Disconnect from session so that the updates on updatedSensor are not directly saved in db
        em.detach(updatedSensor);
        updatedSensor.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).position_x(UPDATED_POSITION_X).position_y(UPDATED_POSITION_Y);

        restSensorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSensor.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSensor))
            )
            .andExpect(status().isOk());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSensor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSensor.getPosition_x()).isEqualTo(UPDATED_POSITION_X);
        assertThat(testSensor.getPosition_y()).isEqualTo(UPDATED_POSITION_Y);
    }

    @Test
    @Transactional
    void putNonExistingSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();
        sensor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sensor.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();
        sensor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();
        sensor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sensor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSensorWithPatch() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Update the sensor using partial update
        Sensor partialUpdatedSensor = new Sensor();
        partialUpdatedSensor.setId(sensor.getId());

        partialUpdatedSensor.description(UPDATED_DESCRIPTION).position_x(UPDATED_POSITION_X);

        restSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensor.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSensor))
            )
            .andExpect(status().isOk());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSensor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSensor.getPosition_x()).isEqualTo(UPDATED_POSITION_X);
        assertThat(testSensor.getPosition_y()).isEqualTo(DEFAULT_POSITION_Y);
    }

    @Test
    @Transactional
    void fullUpdateSensorWithPatch() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Update the sensor using partial update
        Sensor partialUpdatedSensor = new Sensor();
        partialUpdatedSensor.setId(sensor.getId());

        partialUpdatedSensor
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .position_x(UPDATED_POSITION_X)
            .position_y(UPDATED_POSITION_Y);

        restSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensor.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSensor))
            )
            .andExpect(status().isOk());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSensor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSensor.getPosition_x()).isEqualTo(UPDATED_POSITION_X);
        assertThat(testSensor.getPosition_y()).isEqualTo(UPDATED_POSITION_Y);
    }

    @Test
    @Transactional
    void patchNonExistingSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();
        sensor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sensor.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();
        sensor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();
        sensor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        int databaseSizeBeforeDelete = sensorRepository.findAll().size();

        // Delete the sensor
        restSensorMockMvc
            .perform(delete(ENTITY_API_URL_ID, sensor.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
