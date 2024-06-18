package com.example.aircraftwar2024.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aircraftwar2024.R;

public class OverActivity extends AppCompatActivity {
    private Button returnButton;
    private int myScore;
    private int opponentScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over);
        returnButton = findViewById(R.id.returnButton);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OverActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        myScore = getIntent().getIntExtra("myScore", 1);
        opponentScore = getIntent().getIntExtra("opponentScore", 1);
        TextView myScoreText = findViewById(R.id.myScoreText);
        TextView opponentScoreText = findViewById(R.id.opponentScoreText);
        myScoreText.setText(String.valueOf(myScore));
        opponentScoreText.setText(String.valueOf(opponentScore));
    }

}
