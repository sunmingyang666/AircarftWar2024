package com.example.aircraftwar2024.record;

import java.util.List;

public interface RecordDao {

    List<Record> getAllRecords();

    void doAdd(Record record);

    void doDelete(int recordIndex);

    void saveGameHistory();

    void readGameHistory();

    void printSortedRecord();
}
