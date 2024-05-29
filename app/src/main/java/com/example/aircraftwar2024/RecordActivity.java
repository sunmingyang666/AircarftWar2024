package com.example.aircraftwar2024;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.aircraftwar2024.record.Record;
import com.example.aircraftwar2024.record.RecordDao;
import com.example.aircraftwar2024.record.RecordDaoImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //获得Layout里面的ListView
        ListView list = (ListView) findViewById(R.id.ListView);

        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(
                this,
                getData(),
                R.layout.activity_item,
                new String[]{"rank", "user", "score", "time"},
                new int[]{R.id.rank, R.id.user, R.id.score, R.id.time});

        //添加并且显示
        list.setAdapter(listItemAdapter);
    }

    private List<Map<String, Object>> getData() {
        ArrayList<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        RecordDao recordDao = new RecordDaoImpl();
        List<Record> records = recordDao.getAllRecords();
        int rowCount = records.size();
        for (int i = 0; i < rowCount; i++) {
            Record record = records.get(i);
            map = new HashMap<String, Object>();
            map.put("rank", String.valueOf(i + 1));
            map.put("user", record.getRecordName());
            map.put("score", String.valueOf(record.getRecordScore()));
            map.put("time", record.getRecordTime());
            listitem.add(map);
        }
        return listitem;
    }

}
