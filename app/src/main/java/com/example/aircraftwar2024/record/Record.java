package com.example.aircraftwar2024.record;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Record implements Comparable {
    private int recordScore;
    private String recordName;
    private String time;


    public Record(int recordScore, String recordName, String time) {
        this.recordScore = recordScore;
        this.recordName = recordName;
        this.time = time;
    }

    public int getRecordScore() {
        return recordScore;
    }

    public void setRecordScore(int recordScore) {
        this.recordScore = recordScore;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getRecordTime() {
        return time;
    }


    @Override
    public int compareTo(Object o) {
        Record record = (Record) o;
        return record.recordScore - this.recordScore;
    }

}
