package com.mj.string_frequency_manager.string_record_log_file_processor_task;

import com.mj.string_frequency_manager.config.AppConfig;
import com.mj.string_frequency_manager.config.constant.AppConstant;
import com.mj.string_frequency_manager.string_frequency.HourlyStringFrequencyMapper;
import com.mj.string_frequency_manager.string_record.exception.InvalidStringRecordLogFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;

@Slf4j
@Component
public class StringRecordLogFileProcessorTask {

    private AppConfig appConfig;
    private StringRecordLogFileProcessor stringRecordLogFileProcessor;
    private HourlyStringFrequencyMapper hourlyStringFrequencyMapper;

    public StringRecordLogFileProcessorTask(AppConfig appConfig,
                                            StringRecordLogFileProcessor stringRecordLogFileProcessor,
                                            HourlyStringFrequencyMapper hourlyStringFrequencyMapper) {
        this.appConfig = appConfig;
        this.stringRecordLogFileProcessor = stringRecordLogFileProcessor;
        this.hourlyStringFrequencyMapper = hourlyStringFrequencyMapper;
    }

    /**
     * Assuming that :-
     * Upon the zero-th minute of every hour, a new log file that contains the data in the last hour
     * will be generated and be placed into the same folder.
     *
     * log file for the previous hour will be available.
     */
    @Scheduled(cron = "1 * * * * ?")
    public void execute(){

        LocalDateTime now = LocalDateTime.now();

        String stringRecordLogFilePath = new StringBuilder()
                .append(appConfig.getAppLogPath())
                .append(AppConstant.STRING_RECORD_LOG_FILE_PREFIX)
                .append(hourlyStringFrequencyMapper.getTypeFromLocalDateTime(now))
                .append(".log")
                .toString();
        try{
            log.info("StringRecordLogFileProcessorTask starts at: {} for file: {}",LocalDateTime.now(),stringRecordLogFilePath);

            File stringRecordLogFile = new File(stringRecordLogFilePath);
            if(!stringRecordLogFile.exists()){
                throw new InvalidStringRecordLogFileException("Unable to read file at: "+stringRecordLogFilePath);
            }

            stringRecordLogFileProcessor.processLogFileForHour(stringRecordLogFile,now);

            log.info("StringRecordLogFileProcessorTask ends at: {} for file: {}",LocalDateTime.now(),stringRecordLogFilePath);

        }catch (Exception e){
            log.error("Error processing stringRecordLogFile: {} with error: {}",stringRecordLogFilePath,e.getMessage(),e);
        }



    }



}
