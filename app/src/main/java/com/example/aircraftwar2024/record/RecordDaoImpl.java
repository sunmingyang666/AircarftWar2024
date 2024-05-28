package com.example.aircraftwar2024.record;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.io.FileWriter;
import java.io.IOException;

import static java.lang.String.valueOf;

public class RecordDaoImpl implements RecordDao {
    private ArrayList<Record> records;

    public RecordDaoImpl() {
        records = new ArrayList<Record>();
    }


    @Override
    public List<Record> getAllRecords() {
        return records;
    }

    @Override
    public void doAdd(Record record) {
        records.add(record);
        System.out.println("Add new Record: UserName [" + record.getRecordName() + "], Score [" + record.getRecordScore() + "]");

    }

    @Override
    public void doDelete(int recordIndex) {
        records.remove(recordIndex);
    }

    @Override
    public void saveGameHistory() {

        String filePath = "game_history.txt";
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Record record : records) {
                String line = String.join(",", valueOf(record.getRecordScore()), record.getRecordName(), record.getRecordTime()) + "\n"; // 使用逗号分隔每个字段，并添加换行符
                writer.write(line);
            }
            System.out.println("游戏历史数据已保存到文件：" + filePath);
        } catch (IOException e) {
            System.out.println("保存游戏历史数据时出错：" + e.getMessage());
            e.printStackTrace();

        }
    }

    @Override
    public void readGameHistory() {
        String filePath = "game_history.txt";
        try {
            // 创建一个文件读取器
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);

            // 逐行读取数据并输出
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Record record = new Record(Integer.parseInt(data[0]), data[1], data[2]);
                records.add(record);// 使用逗号分隔数据
            }

            // 关闭读取器
            br.close();
            fr.close();
        } catch (IOException e) {
            System.out.println("读取游戏数据时出错：" + e.getMessage());
        }
    }

    public void printSortedRecord() {
        Collections.sort(records);
        System.out.println("****************************************");
        System.out.println("                得分排行榜");
        System.out.println("****************************************");
        int i = 0;
        for (Record record : records) {
            i++;
            System.out.println("第" + i + "名:" + record.getRecordName() + "," + record.getRecordScore() + "," + record.getRecordTime());
        }


    }
}
