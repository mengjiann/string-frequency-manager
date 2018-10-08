package com.mj.string_frequency_manager.string_frequency;

import com.mj.string_frequency_manager.config.constant.AppConstant;
import com.mj.string_frequency_manager.string_frequency.domain.Past24HourStringFrequency;
import com.mj.string_frequency_manager.string_record.domain.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class Past24HourStringFrequencyMapperTest {

    private Past24HourStringFrequencyMapper past24HourStringFrequencyMapper;

    @Before
    public void setUp() throws Exception {
        past24HourStringFrequencyMapper = new Past24HourStringFrequencyMapper();
    }

    @Test
    public void map_validInput_validOutput() {

        LocalDateTime now = LocalDateTime.now();
        StringRecord stringRecord = new StringRecord(Timestamp.valueOf(now),"abc");

        Past24HourStringFrequency stringFrequency = past24HourStringFrequencyMapper.map(stringRecord);

        assertThat(stringFrequency.getType(),is(AppConstant.STRING_FREQUENCY_TYPE_PAST24HOUR));
        assertThat(stringFrequency.getStringId(),is("abc"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void map_validInput_invalidOutput() {
        past24HourStringFrequencyMapper.map(null);
    }
}