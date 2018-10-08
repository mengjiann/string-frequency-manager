package com.mj.string_frequency_manager.string_frequency.domain;


import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class HourlyStringFrequency extends StringFrequency {

    public HourlyStringFrequency(String type, String stringId) {
        super(type, stringId);
    }

    public HourlyStringFrequency(String type, String stringId, int counter) {
        super(type, stringId, counter);
    }
}
