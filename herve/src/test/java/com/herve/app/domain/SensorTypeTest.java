package com.herve.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.herve.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SensorTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SensorType.class);
        SensorType sensorType1 = new SensorType();
        sensorType1.setId(1L);
        SensorType sensorType2 = new SensorType();
        sensorType2.setId(sensorType1.getId());
        assertThat(sensorType1).isEqualTo(sensorType2);
        sensorType2.setId(2L);
        assertThat(sensorType1).isNotEqualTo(sensorType2);
        sensorType1.setId(null);
        assertThat(sensorType1).isNotEqualTo(sensorType2);
    }
}
