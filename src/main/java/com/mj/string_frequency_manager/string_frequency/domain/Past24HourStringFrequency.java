package com.mj.string_frequency_manager.string_frequency.domain;


import com.mj.string_frequency_manager.config.constant.AppConstant;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class Past24HourStringFrequency extends StringFrequency{

    public Past24HourStringFrequency(String stringId) {
        super(AppConstant.STRING_FREQUENCY_TYPE_PAST24HOUR, stringId);
    }

    public Past24HourStringFrequency(String stringId, int counter) {
        super(AppConstant.STRING_FREQUENCY_TYPE_PAST24HOUR, stringId, counter);
    }
}
