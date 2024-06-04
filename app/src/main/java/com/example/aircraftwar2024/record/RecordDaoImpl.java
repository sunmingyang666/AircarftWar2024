package com.example.aircraftwar2024.record;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.io.FileWriter;
import java.io.IOException;

import static java.lang.String.valueOf;

import android.content.Context;

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
    public void doDelete(int gameType, int recordIndex) {
        int i = 1;
        for (Record record : records) {
            if (record.getGameType() == gameType) {
                if (i == recordIndex) {
                    records.remove(record);
                    break;
                } else {
                    i++;
                    System.out.println(i+"read");
                }
            }
        }
    }

    @Override
    public void saveGameHistory(Context context) {


        String filePath = "records.txt";

        try (FileOutputStream fos = context.openFileOutput(filePath, Context.MODE_PRIVATE);
             OutputStreamWriter osw = new OutputStreamWriter(fos);
             BufferedWriter writer = new BufferedWriter(osw)) {

            for (Record record : records) {
                String line = String.join(",",
                        String.valueOf(record.getRecordScore()),
                        record.getRecordName(),
                        record.getRecordTime(),
                        String.valueOf(record.getGameType())) + "\n"; // Use comma to separate each field and add newline
                writer.write(line);
            }

            System.out.println("游戏历史数据已保存到文件：" + filePath);
        } catch (IOException e) {
            System.out.println("保存游戏历史数据时出错：" + e.getMessage());
            e.printStackTrace();
        }


    }

    @Override
    public void readGameHistory(Context context) {
        String filePath = "records.txt";
        File file = new File(context.getFilesDir(), filePath);
        try {
            // 创建一个文件读取器
            FileInputStream fis = context.openFileInput(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            // 逐行读取数据并输出
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Record record = new Record(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]));
                records.add(record);// 使用逗号分隔数据
            }

            // 关闭读取器
            br.close();
            fis.close();
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
