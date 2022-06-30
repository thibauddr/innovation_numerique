package com.herve.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.herve.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FieldTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Field.class);
        Field field1 = new Field();
        field1.setId(1L);
        Field field2 = new Field();
        field2.setId(field1.getId());
        assertThat(field1).isEqualTo(field2);
        field2.setId(2L);
        assertThat(field1).isNotEqualTo(field2);
        field1.setId(null);
        assertThat(field1).isNotEqualTo(field2);
    }
}
