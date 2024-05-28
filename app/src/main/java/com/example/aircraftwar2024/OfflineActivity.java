package com.example.aircraftwar2024;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.activity.GameActivity;

public class OfflineActivity extends AppCompatActivity {
    private Button easyButton,normalButton,difficultButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        Intent intent = getIntent();
        boolean isMusicOn = getIntent().getBooleanExtra("music",false);
        easyButton=findViewById(R.id.easyButton);
        normalButton=findViewById(R.id.normalButton);
        difficultButton=findViewById(R.id.difficultButton);

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OfflineActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OfflineActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        difficultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OfflineActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

    }

}