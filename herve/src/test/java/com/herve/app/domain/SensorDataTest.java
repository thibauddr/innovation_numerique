package com.herve.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.herve.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SensorDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SensorData.class);
        SensorData sensorData1 = new SensorData();
        sensorData1.setId(1L);
        SensorData sensorData2 = new SensorData();
        sensorData2.setId(sensorData1.getId());
        assertThat(sensorData1).isEqualTo(sensorData2);
        sensorData2.setId(2L);
        assertThat(sensorData1).isNotEqualTo(sensorData2);
        sensorData1.setId(null);
        assertThat(sensorData1).isNotEqualTo(sensorData2);
    }
}
