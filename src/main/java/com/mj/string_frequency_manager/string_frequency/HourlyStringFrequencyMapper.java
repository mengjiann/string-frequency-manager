package com.mj.string_frequency_manager.string_frequency;

import com.google.common.base.Preconditions;
import com.mj.string_frequency_manager.config.constant.AppConstant;
import com.mj.string_frequency_manager.string_frequency.domain.HourlyStringFrequency;
import com.mj.string_frequency_manager.string_record.domain.StringRecord;
import com.mj.string_frequency_manager.util.InputValidator;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Service
public class HourlyStringFrequencyMapper {

    public HourlyStringFrequency map(StringRecord stringRecord){
        Preconditions.checkArgument(!InputValidator.isInvalid(stringRecord),"Invalid string record for mapper");

        return new HourlyStringFrequency(this.getTypeFromTimestamp(stringRecord.getTimestamp()),stringRecord.getString());
    }

    public String getTypeFromTimestamp(Timestamp recordTimestamp){
        Preconditions.checkArgument(!InputValidator.isInvalid(recordTimestamp),"Invalid recordTimestamp");

        LocalDateTime recordDateTime = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(recordTimestamp.getTime()),
                        TimeZone.getDefault().toZoneId());

        return recordDateTime.format(DateTimeFormatter.ofPattern(AppConstant.STRING_FREQUENCY_TYPE_HOURLY_DATETIME_FORMAT));
    }

    public String getTypeFromLocalDateTime(LocalDateTime recordDateTime){
        Preconditions.checkArgument(!InputValidator.isInvalid(recordDateTime),"Invalid recordDateTime");

        return recordDateTime.format(DateTimeFormatter.ofPattern(AppConstant.STRING_FREQUENCY_TYPE_HOURLY_DATETIME_FORMAT));
    }
}
