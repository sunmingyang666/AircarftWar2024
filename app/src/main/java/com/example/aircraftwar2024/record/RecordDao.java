package com.example.aircraftwar2024.record;

import android.content.Context;

import java.io.FileNotFoundException;
import java.util.List;

public interface RecordDao {

    List<Record> getAllRecords();

    void doAdd(Record record);

    void doDelete(int gameType,int recordIndex);

    void saveGameHistory(Context context);

    void readGameHistory(Context context);

    void printSortedRecord();
}
