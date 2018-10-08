package com.mj.string_frequency_manager.string_frequency;

import com.mj.string_frequency_manager.string_frequency.domain.HourlyStringFrequency;
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
public class HourlyStringFrequencyMapperTest {

    private HourlyStringFrequencyMapper hourlyStringFrequencyMapper;

    @Before
    public void setUp() throws Exception {
        hourlyStringFrequencyMapper = new HourlyStringFrequencyMapper();
    }

    @Test
    public void map_validStringRecord_validOutput() {
        LocalDateTime now = LocalDateTime.now();
        StringRecord stringRecord = new StringRecord(Timestamp.valueOf(now),"abc");

        HourlyStringFrequency hourlyStringFrequency = hourlyStringFrequencyMapper.map(stringRecord);

        assertThat(hourlyStringFrequency.getStringId(),is("abc"));
        assertThat(hourlyStringFrequency.getType(),is(hourlyStringFrequencyMapper.getTypeFromLocalDateTime(now)));

    }

    @Test(expected = IllegalArgumentException.class)
    public void map_validInput_invalidOutput() {
        hourlyStringFrequencyMapper.map(null);
    }

    @Test
    public void getTypeFromTimestamp_validInput_validOutput() {
        LocalDateTime now = LocalDateTime.now();

        now = now.withYear(2018).withMonth(10).withDayOfMonth(7).withHour(22);

        String type = hourlyStringFrequencyMapper.getTypeFromTimestamp(Timestamp.valueOf(now));

        assertThat(type,is("2018100722"));

    }

    @Test
    public void getTypeFromLocalDateTime_validInput_validOutput() {
        LocalDateTime now = LocalDateTime.now();

        now = now.withYear(2018).withMonth(10).withDayOfMonth(7).withHour(22);

        String type = hourlyStringFrequencyMapper.getTypeFromLocalDateTime(now);

        assertThat(type,is("2018100722"));

    }
}