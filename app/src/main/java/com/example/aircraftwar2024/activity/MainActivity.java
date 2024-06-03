package com.example.aircraftwar2024.activity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.aircraftwar2024.R;

public class MainActivity extends AppCompatActivity {
    private RadioButton radioButton1,radioButton2;
    private Button startBotton;
    //test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//new11
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        startBotton = findViewById(R.id.startButton);

        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioButton2.isChecked()){
                    radioButton2.setChecked(false);
                }
            }
        });

        startBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OfflineActivity.class);
                boolean isMusicOn = radioButton1.isChecked();
                intent.putExtra("music",isMusicOn);
                startActivity(intent);
            }
        });
    }
}