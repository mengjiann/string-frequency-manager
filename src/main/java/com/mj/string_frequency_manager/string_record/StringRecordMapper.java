package com.mj.string_frequency_manager.string_record;

import com.google.common.base.Preconditions;
import com.mj.string_frequency_manager.string_record.domain.StringRecord;
import com.mj.string_frequency_manager.string_record_log_file_processor_task.exception.InvalidRawLogRecordException;
import com.mj.string_frequency_manager.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Service
public class StringRecordMapper {

    public StringRecord mapRawLogRecord(String rawLogRecord) throws InvalidRawLogRecordException {
        Preconditions.checkArgument(!InputValidator.isInvalid(rawLogRecord),"Invalid raw log record");

        String[] rawLogRecords = rawLogRecord.split(",");
        if(rawLogRecords == null || rawLogRecords.length != 2){
            throw new InvalidRawLogRecordException("Unable to create string record from raw log record: "+rawLogRecord);
        }

        String timestampString = rawLogRecords[0].trim();
        String stringRecordText = rawLogRecords[1].trim();

        Timestamp stringRecordTimestamp = null;
        try{
            stringRecordTimestamp = new Timestamp(Long.valueOf(timestampString));
        }catch (Exception e){
            throw new InvalidRawLogRecordException("Invalid timestamp string from raw log record: "+rawLogRecord);
        }

        return new StringRecord(stringRecordTimestamp, stringRecordText);
    }

    public boolean isRawLogRecordForPreviousHour(StringRecord stringRecord, LocalDateTime previousHour){
        Preconditions.checkArgument(!InputValidator.isInvalid(stringRecord),"Invalid string record");
        Preconditions.checkArgument(!InputValidator.isInvalid(previousHour),"Invalid previous hour");

        LocalDateTime recordDateTime = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(stringRecord.getTimestamp().getTime()),
                        TimeZone.getDefault().toZoneId());

        return recordDateTime.getHour() == previousHour.getHour()
                && recordDateTime.getDayOfYear() == previousHour.getDayOfYear()
                && recordDateTime.getMonth() == previousHour.getMonth()
                && recordDateTime.getYear() == previousHour.getYear();
    }


}
