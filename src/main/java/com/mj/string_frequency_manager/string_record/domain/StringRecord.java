package com.mj.string_frequency_manager.string_record.domain;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class StringRecord {

    private Timestamp timestamp;
    private String string;

    public StringRecord(Timestamp timestamp, String string) {
        this.timestamp = timestamp;
        this.string = string;
    }
}
