package com.mj.string_frequency_manager.string_record_log_file_processor_task;

import com.mj.string_frequency_manager.string_frequency.HourlyStringFrequencyMapper;
import com.mj.string_frequency_manager.string_frequency.Past24HourStringFrequencyMapper;
import com.mj.string_frequency_manager.string_frequency.StringFrequencyUpdater;
import com.mj.string_frequency_manager.string_record.StringRecordMapper;
import com.mj.string_frequency_manager.string_record.domain.StringRecord;
import com.mj.string_frequency_manager.string_record_log_file_processor_task.exception.InvalidRawLogRecordException;
import com.mj.string_frequency_manager.string_record_log_file_processor_task.exception.StringRecordLogFileProcessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.TimeZone;

@Slf4j
@Service
public class StringRecordLogFileProcessor {

    private StringRecordMapper stringRecordMapper;

    private HourlyStringFrequencyMapper hourlyStringFrequencyMapper;

    private Past24HourStringFrequencyMapper past24HourStringFrequencyMapper;

    private StringFrequencyUpdater stringFrequencyUpdater;

    public StringRecordLogFileProcessor(StringRecordMapper stringRecordMapper,
                                        HourlyStringFrequencyMapper hourlyStringFrequencyMapper,
                                        Past24HourStringFrequencyMapper past24HourStringFrequencyMapper,
                                        StringFrequencyUpdater stringFrequencyUpdater) {
        this.stringRecordMapper = stringRecordMapper;
        this.hourlyStringFrequencyMapper = hourlyStringFrequencyMapper;
        this.past24HourStringFrequencyMapper = past24HourStringFrequencyMapper;
        this.stringFrequencyUpdater = stringFrequencyUpdater;
    }


    /**
     * The new log file that contains the data in the last hour will be generated
     * and be placed into the same folder, the file name format is string-generation-{yyyymmddhh}.log​.
     * E.g. the file with name ​string-generation-2018093016.log is a file generated at 1600 hour on 30 Sep, 2018.
     *
     * Assume that the log file with ​string-generation-2018093016.log name will contain string records
     * with timestamp within 2018-09-30 15:00. Anything happened
     *
     * @param file - File to be processed
     * @param fileReceivedHour - File received hour
     * @throws StringRecordLogFileProcessException
     */
    public void processLogFileForHour(File file, LocalDateTime fileReceivedHour) throws StringRecordLogFileProcessException {
        try{
            FileInputStream inputStream = null;
            Scanner sc = null;
            try {
                inputStream = new FileInputStream(file);
                sc = new Scanner(inputStream, "UTF-8");
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    log.info("Read debug: {}",line);
                    try{
                        StringRecord stringRecord = stringRecordMapper.mapRawLogRecord(line);

                        if(!stringRecordMapper
                                .isRawLogRecordForPreviousHour(stringRecord,fileReceivedHour.minusHours(1L))){
                            throw new InvalidRawLogRecordException("Raw log record is not for previous fileReceivedHour: " + line+" for stringRecord: "+stringRecord);
                        }

                        stringFrequencyUpdater.incrementCountForStringId(
                                hourlyStringFrequencyMapper.map(stringRecord));
                        stringFrequencyUpdater.incrementCountForStringId(
                                past24HourStringFrequencyMapper.map(stringRecord));
                    }catch (InvalidRawLogRecordException e){
                        log.error("Unable to process raw log record: {} from file: {} with error: {}",line,file.getAbsolutePath(),e.getMessage());
                    }
                }

                if (sc.ioException() != null) {
                    throw sc.ioException();
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (sc != null) {
                    sc.close();
                }
            }
        } catch (IOException e) {
            throw new StringRecordLogFileProcessException("Unable to process string record from file: " + file.getPath()+" with error: "+e.getMessage());
        }
    }





}
