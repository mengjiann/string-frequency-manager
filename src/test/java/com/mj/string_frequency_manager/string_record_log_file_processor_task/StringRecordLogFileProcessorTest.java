package com.mj.string_frequency_manager.string_record_log_file_processor_task;

import com.mj.string_frequency_manager.string_frequency.HourlyStringFrequencyMapper;
import com.mj.string_frequency_manager.string_frequency.Past24HourStringFrequencyMapper;
import com.mj.string_frequency_manager.string_frequency.StringFrequencyGetter;
import com.mj.string_frequency_manager.string_frequency.StringFrequencyUpdater;
import com.mj.string_frequency_manager.string_frequency.domain.HourlyStringFrequency;
import com.mj.string_frequency_manager.string_frequency.domain.StringFrequency;
import com.mj.string_frequency_manager.string_record.StringRecordMapper;
import com.mj.string_frequency_manager.string_record_log_file_processor_task.exception.StringRecordLogFileProcessException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisServer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.profiles.active=test"})
public class StringRecordLogFileProcessorTest {

    @Autowired
    private StringFrequencyGetter stringFrequencyGetter;

    @Autowired
    private StringFrequencyUpdater stringFrequencyUpdater;

    @Autowired
    private Past24HourStringFrequencyMapper past24HourStringFrequencyMapper;

    @Autowired
    private StringRecordMapper stringRecordMapper;

    @Autowired
    private HourlyStringFrequencyMapper hourlyStringFrequencyMapper;

    private StringRecordLogFileProcessor stringRecordLogFileProcessor;

    private RedisServer redisServer;

    @Before
    public void setUp() throws Exception {

        stringRecordLogFileProcessor = new StringRecordLogFileProcessor(
                stringRecordMapper,hourlyStringFrequencyMapper,past24HourStringFrequencyMapper,stringFrequencyUpdater
        );
    }

    @Test
    public void processLogFileForHour_validLogFile() throws IOException, StringRecordLogFileProcessException {

        LocalDateTime dateTime = LocalDateTime.now();
        dateTime = dateTime.withYear(2018).withMonth(10).withDayOfMonth(01).withHour(14).withMinute(0);

        File file = new ClassPathResource(
                "test/string_record_log_file/string-generation-2018100114.log").getFile();

        stringRecordLogFileProcessor.processLogFileForHour(file,dateTime);

        StringFrequency stringFrequency = stringFrequencyGetter.getByStringFrequency(
                new HourlyStringFrequency(hourlyStringFrequencyMapper.getTypeFromLocalDateTime(dateTime.minusHours(1)),
                        "9156cef74194b365808f58c134fe548a"))
                .get();

        assertThat(stringFrequency.getCounter(),is(2));
    }

    @Test
    public void processLogFileForHour_logFileContainingWrongStringRecord() throws IOException, StringRecordLogFileProcessException {

        LocalDateTime dateTime = LocalDateTime.now();
        dateTime = dateTime.withYear(2018).withMonth(10).withDayOfMonth(01).withHour(15).withMinute(0);

        File file = new ClassPathResource(
                "test/string_record_log_file/string-generation-2018100115.log").getFile();

        stringRecordLogFileProcessor.processLogFileForHour(file,dateTime);

        Optional<StringFrequency> optionalStringFrequency = stringFrequencyGetter.getByStringFrequency(
                new HourlyStringFrequency(hourlyStringFrequencyMapper.getTypeFromLocalDateTime(dateTime.minusHours(1)),
                        "9156cef74194b365808f58c134fe548a1"));

        assertThat(optionalStringFrequency.isPresent(),is(false));
    }

}