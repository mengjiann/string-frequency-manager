package com.mj.string_frequency_manager.string_record;

import com.mj.string_frequency_manager.string_record_log_file_processor_task.exception.InvalidRawLogRecordException;
import com.mj.string_frequency_manager.string_record.domain.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Timestamp;
import java.util.Calendar;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class StringRecordMapperTest {

    private StringRecordMapper stringRecordMapper;

    @Before
    public void setUp() throws Exception {
        stringRecordMapper = new StringRecordMapper();
    }

    @Test
    public void mapRawLogRecord_validRawLogRecord_validStringRecord() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.set(2018,9,01,13,40,11);
        cal.set(Calendar.MILLISECOND,0);

        String rawLogRecord = "1538372411000, 9156cef74194b365808f58c134fe548a";

        StringRecord stringRecord = stringRecordMapper.mapRawLogRecord(rawLogRecord);

        assertThat(stringRecord.getString(),is("9156cef74194b365808f58c134fe548a"));
        assertThat(stringRecord.getTimestamp(),is(new Timestamp(cal.getTimeInMillis())));
    }

    @Test(expected = InvalidRawLogRecordException.class)
    public void mapRawLogRecord_incompleteRawLogRecordType1_throwStringRecordLogFileProcessException() throws Exception {
        String rawLogRecord = "1538372411000";

        StringRecord stringRecord = stringRecordMapper.mapRawLogRecord(rawLogRecord);
    }

    @Test(expected = InvalidRawLogRecordException.class)
    public void mapRawLogRecord_incompleteRawLogRecordType2_throwStringRecordLogFileProcessException() throws Exception {
        String rawLogRecord = "1538372411000,";

        StringRecord stringRecord = stringRecordMapper.mapRawLogRecord(rawLogRecord);
    }

    @Test(expected = InvalidRawLogRecordException.class)
    public void mapRawLogRecord_incompleteRawLogRecordType3_throwStringRecordLogFileProcessException() throws Exception {
        String rawLogRecord = ", 9156cef74194b365808f58c134fe548a";

        StringRecord stringRecord = stringRecordMapper.mapRawLogRecord(rawLogRecord);
    }






}