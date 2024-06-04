package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.record.Record;
import com.example.aircraftwar2024.record.RecordDao;
import com.example.aircraftwar2024.record.RecordDaoImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordActivity extends AppCompatActivity {
    private Button returnButton;
    private TextView ModeText;
    private int gameType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record);
        returnButton = findViewById(R.id.returnButton);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ModeText = findViewById(R.id.Mode);
        if (getIntent() != null) {
            gameType = getIntent().getIntExtra("gameType", 1);
        }

        if (gameType == 1) {
            ModeText.setText("简单模式");
        } else if (gameType == 2) {
            ModeText.setText("普通模式");
        } else if (gameType == 3) {
            ModeText.setText("困难模式");
        }

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


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //Map<String, Object> clkmap = (Map<String, Object>) arg0.getItemAtPosition(arg2);
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
                builder.setMessage("确定删除第" + (arg2 + 1) + "条数据？")
                        .setTitle("提示");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RecordDao recordDao = new RecordDaoImpl();
                        recordDao.readGameHistory(RecordActivity.this);
                        System.out.println(arg2);
                        recordDao.doDelete(gameType, arg2+1);
                        recordDao.saveGameHistory(RecordActivity.this);
                        Intent intent = new Intent(RecordActivity.this, RecordActivity.class);
                        intent.putExtra("gameType", gameType);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    private List<Map<String, Object>> getData() {
        ArrayList<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        RecordDao recordDao = new RecordDaoImpl();
        recordDao.readGameHistory(this);
        List<Record> records = recordDao.getAllRecords();
        int i = 0;
        for (Record record : records) {
            if (record.getGameType() == gameType) {
                map = new HashMap<String, Object>();
                map.put("rank", i + 1);
                map.put("user", record.getRecordName());
                map.put("score", record.getRecordScore());
                map.put("time", record.getRecordTime());
                i++;
                listitem.add(map);
            }

        }
        return listitem;
    }

}
