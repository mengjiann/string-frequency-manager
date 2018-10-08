package com.mj.string_frequency_manager.string_frequency;

import com.google.common.base.Preconditions;
import com.mj.string_frequency_manager.string_frequency.domain.Past24HourStringFrequency;
import com.mj.string_frequency_manager.string_record.domain.StringRecord;
import com.mj.string_frequency_manager.util.InputValidator;
import org.springframework.stereotype.Service;

@Service
public class Past24HourStringFrequencyMapper {

    public Past24HourStringFrequency map(StringRecord stringRecord){
        Preconditions.checkArgument(!InputValidator.isInvalid(stringRecord),"Invalid string record for mapper");

        return new Past24HourStringFrequency(stringRecord.getString());
    }
}
