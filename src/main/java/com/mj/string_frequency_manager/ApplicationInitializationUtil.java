package com.mj.string_frequency_manager;

import com.mj.string_frequency_manager.config.AppConfig;
import com.mj.string_frequency_manager.config.constant.AppConstant;
import com.mj.string_frequency_manager.string_frequency.HourlyStringFrequencyMapper;
import com.mj.string_frequency_manager.string_record.exception.InvalidStringRecordLogFileException;
import com.mj.string_frequency_manager.string_record_log_file_processor_task.StringRecordLogFileProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class ApplicationInitializationUtil {

    private AppConfig appConfig;

    private StringRecordLogFileProcessor stringRecordLogFileProcessor;

    private HourlyStringFrequencyMapper hourlyStringFrequencyMapper;

    public ApplicationInitializationUtil(AppConfig appConfig, StringRecordLogFileProcessor stringRecordLogFileProcessor, HourlyStringFrequencyMapper hourlyStringFrequencyMapper) {
        this.appConfig = appConfig;
        this.stringRecordLogFileProcessor = stringRecordLogFileProcessor;
        this.hourlyStringFrequencyMapper = hourlyStringFrequencyMapper;
    }

    public void generateTestData(){
        if(!appConfig.getGenerateInitData()){
            log.info("Skip generating test data.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 2; i++) {
            LocalDateTime toProcessPastHour = now.minusHours(i);

            String stringRecordLogFilePath = new StringBuilder()
                    .append(appConfig.getAppLogPath())
                    .append(AppConstant.STRING_RECORD_LOG_FILE_PREFIX)
                    .append(hourlyStringFrequencyMapper.getTypeFromLocalDateTime(toProcessPastHour))
                    .append(".log")
                    .toString();
            try{
                log.info("Create test data for file: {}",stringRecordLogFilePath);

                File stringRecordLogFile = new File(stringRecordLogFilePath);
                if(!stringRecordLogFile.exists()){
                    stringRecordLogFile.createNewFile();
                }else {
                    continue;
                }

                for (int j = 0; j < 5; j++) {
                    try {
                        String value = new StringBuilder()
                                .append(Timestamp.valueOf(toProcessPastHour.minusHours(1)).getTime())
                                .append(", ")
                                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMSS")))
                                .append("\n")
                                .toString();

                        Thread.sleep(10);
                        Files.write(stringRecordLogFile.toPath(),value.getBytes(),StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        log.error(e.getMessage(),e);
                    }
                }
                log.info("Past24hour log process for file: {}",stringRecordLogFilePath);
            }catch (Exception e){
                log.error("Error processing stringRecordLogFile: {} with error: {}",stringRecordLogFilePath,e.getMessage());
            }
        }
    }

    public void loadInitData(){
        if(!appConfig.getLoadInitData()){
            log.info("Skip loading init data.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 24; i++) {
            LocalDateTime toProcessPastHour = now.minusHours(i);

            String stringRecordLogFilePath = new StringBuilder()
                    .append(appConfig.getAppLogPath())
                    .append(AppConstant.STRING_RECORD_LOG_FILE_PREFIX)
                    .append(hourlyStringFrequencyMapper.getTypeFromLocalDateTime(toProcessPastHour))
                    .append(".log")
                    .toString();
            try{
                log.info("To process past24hour log for file: {}",stringRecordLogFilePath);

                File stringRecordLogFile = new File(stringRecordLogFilePath);
                if(!stringRecordLogFile.exists()){
                    throw new InvalidStringRecordLogFileException("Unable to read file at: "+stringRecordLogFilePath);
                }

                stringRecordLogFileProcessor.processLogFileForHour(stringRecordLogFile,toProcessPastHour);

                log.info("Past24hour log process for file: {}",stringRecordLogFilePath);

            }catch (Exception e){
                log.error("Error processing stringRecordLogFile: {} with error: {}",stringRecordLogFilePath,e.getMessage());
            }
        }
    }


}
