package com.example.aircraftwar2024.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aircraftwar2024.game.BaseGame;
import com.example.aircraftwar2024.game.EasyGame;
import com.example.aircraftwar2024.game.HardGame;
import com.example.aircraftwar2024.game.MediumGame;

import java.util.logging.LogRecord;


public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";
    public Handler mhandler;
    private int gameType=0;
    public static int screenWidth,screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getScreenHW();

        mhandler = new Mhandler();

        if(getIntent() != null){
            gameType = getIntent().getIntExtra("gameType",1);
        }
        BaseGame baseGameView = null;
        /*TODO:根据用户选择的难度加载相应的游戏界面*/
        if(gameType==1){
            baseGameView = new EasyGame(this);
        }
        else if(gameType==2){
            baseGameView = new MediumGame(this);

        }
        else if(gameType==3){
            baseGameView = new HardGame(this);
        }

        setContentView(baseGameView);
    }

    public void getScreenHW(){
        //定义DisplayMetrics 对象
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getDisplay().getRealMetrics(dm);

        //窗口的宽度
        screenWidth= dm.widthPixels;
        //窗口高度
        screenHeight = dm.heightPixels;

        Log.i(TAG, "screenWidth : " + screenWidth + " screenHeight : " + screenHeight);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    class Mhandler extends Handler {
        // 通过复写handlerMessage() 从而确定更新UI的操作
        @Override
        public void handleMessage(Message msg) {
            // 根据不同线程发送过来的消息，执行不同的UI操作
            // 根据 Message对象的what属性 标识不同的消息
            if(msg.what == 1){
            }
        }
    }
}