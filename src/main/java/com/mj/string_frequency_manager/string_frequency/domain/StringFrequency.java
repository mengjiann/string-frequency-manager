package com.mj.string_frequency_manager.string_frequency.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StringFrequency {

//    private String root;
    private String type;
    private String stringId;
    private Integer counter;

    public StringFrequency() {
    }

    public StringFrequency(String type, String stringId) {
        this.type = type;
        this.stringId = stringId;
    }

    public StringFrequency(String type, String stringId, Integer counter) {
        this.type = type;
        this.stringId = stringId;
        this.counter = counter;
    }



}
