package com.example.aircraftwar2024.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aircraftwar2024.game.BaseGame;
import com.example.aircraftwar2024.game.HardGame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class OnlineActivity extends AppCompatActivity {
    private static final String TAG = "OnlineActivity";
    private Socket socket;
    private PrintWriter writer;

    private BufferedReader reader;
    private static int opponentScore=0;
    private BaseGame game;
    private Handler handler;
    private boolean gameOverFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isMusicOn = getIntent().getBooleanExtra("music",false);

        game = new HardGame(OnlineActivity.this);
        game.setMusic(isMusicOn);
        setContentView(game);
        game.setOnline();

        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                new Thread(() ->{
                    while (!gameOverFlag){
                        writer.println(game.getScore());
                        Log.i(TAG,"send score to server:"+game.getScore());
                        try {
                            Thread.sleep(5000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    writer.println("gameover");
                    Log.i(TAG,"send gameover to server");
                    if(msg.what == 1 && msg.obj.equals("end")) {
                        Intent intent = new Intent(OnlineActivity.this, OverActivity.class);
                        intent.putExtra("myScore", game.getScore());
                        intent.putExtra("opponentScore",opponentScore);
                        startActivity(intent);
                        onDestroy();
                    }
                    else if (msg.obj.toString().startsWith("score:")) {
                        opponentScore = Integer.parseInt(msg.toString().split(":")[1]);
                    }
                }).start();

            }
        };
        new Thread(new NetConn(handler)).start();

    }

    private class NetConn implements Runnable {
        private Handler toClienthandler;

        public NetConn(Handler handler) {
            this.toClienthandler = handler;
        }

        @Override
        public void run() {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress
                        ("10.0.2.2",9999),5000);
                reader = new BufferedReader(new InputStreamReader(
                        socket.getInputStream(),"utf-8"));
                writer = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream(),"utf-8")),true);
                // 接收服务端信息
                new Thread(() ->{
                    String msgFromServer;
                    try{
                        while ((msgFromServer = reader.readLine()) != null){
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = msgFromServer;
                            toClienthandler.sendMessage(msg);
                        }
                        } catch(IOException e){
                            e.printStackTrace();
                        }
                    }).start();

                }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public static int getOpponentScore() {
        return opponentScore;
    }

    public void setGameOverFlag(boolean gameOverFlag) {
        this.gameOverFlag = gameOverFlag;
    }
    protected void onDestroy(){
        super.onDestroy();
        disconnectFromServer();
    }
    private void disconnectFromServer(){
        try {
            if(writer!=null){
                writer.close();
            }
            if(reader!=null){
                reader.close();
            }
            if(socket!=null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
