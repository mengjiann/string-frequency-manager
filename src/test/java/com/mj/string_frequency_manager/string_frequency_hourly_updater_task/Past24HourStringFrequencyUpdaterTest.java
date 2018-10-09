package com.mj.string_frequency_manager.string_frequency_hourly_updater_task;

import com.mj.string_frequency_manager.string_frequency.HourlyStringFrequencyMapper;
import com.mj.string_frequency_manager.string_frequency.StringFrequencyGetter;
import com.mj.string_frequency_manager.string_frequency.StringFrequencyMapper;
import com.mj.string_frequency_manager.string_frequency.StringFrequencyUpdater;
import com.mj.string_frequency_manager.string_frequency.domain.HourlyStringFrequency;
import com.mj.string_frequency_manager.string_frequency.domain.Past24HourStringFrequency;
import com.mj.string_frequency_manager.string_frequency.domain.StringFrequency;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.profiles.active=test"})
public class Past24HourStringFrequencyUpdaterTest {

    @Autowired
    private StringFrequencyGetter stringFrequencyGetter;

    @Autowired
    private StringFrequencyUpdater stringFrequencyUpdater;

    @Autowired
    private StringFrequencyMapper stringFrequencyMapper;

    @Autowired
    private HourlyStringFrequencyMapper hourlyStringFrequencyMapper;

    private Past24HourStringFrequencyUpdater past24HourStringFrequencyUpdater;

    @Before
    public void setUp() throws Exception {

        past24HourStringFrequencyUpdater = new Past24HourStringFrequencyUpdater(
            stringFrequencyGetter,stringFrequencyUpdater,stringFrequencyMapper
        );
    }

    @Test
    public void removeHourlyStringFrequencyForHour_validRemoval()  {

        LocalDateTime dateTime = LocalDateTime.now();
        dateTime = dateTime.withYear(2018).withMonth(10).withDayOfMonth(7).withHour(22);

        String type = hourlyStringFrequencyMapper.getTypeFromLocalDateTime(dateTime);

        stringFrequencyUpdater.incrementCountForStringId(new HourlyStringFrequency(type,"abc",2));
        stringFrequencyUpdater.incrementCountForStringId(new HourlyStringFrequency(type,"def",1));

        stringFrequencyUpdater.incrementCountForStringId(new Past24HourStringFrequency("abc",3));
        stringFrequencyUpdater.incrementCountForStringId(new Past24HourStringFrequency("def",5));


        past24HourStringFrequencyUpdater.removeHourlyStringFrequencyForHour(dateTime);

        StringFrequency abc = stringFrequencyGetter.getByStringFrequency(new Past24HourStringFrequency("abc")).get();
        StringFrequency def = stringFrequencyGetter.getByStringFrequency(new Past24HourStringFrequency("def")).get();

        assertThat(abc.getCounter(),is(1));
        assertThat(def.getCounter(),is(4));

    }

    @Test
    public void removeHourlyStringFrequencyForHour_emptyRemoval()  {

        LocalDateTime dateTime = LocalDateTime.now();
        dateTime = dateTime.withYear(2018).withMonth(10).withDayOfMonth(7).withHour(23);

        stringFrequencyUpdater.incrementCountForStringId(
                new Past24HourStringFrequency("xyz",3));
        stringFrequencyUpdater.incrementCountForStringId(
                new Past24HourStringFrequency("opq",5));

        past24HourStringFrequencyUpdater.removeHourlyStringFrequencyForHour(dateTime);

        StringFrequency xyz = stringFrequencyGetter.getByStringFrequency(
                new Past24HourStringFrequency("xyz")).get();
        StringFrequency opq = stringFrequencyGetter.getByStringFrequency(
                new Past24HourStringFrequency("opq")).get();

        assertThat(xyz.getCounter(),is(3));
        assertThat(opq.getCounter(),is(5));

    }



}