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
    private Handler handler;
    private BufferedReader reader;
    private static int opponentScore=0;
    private BaseGame game;

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
                if (true) {
                //if (msg.what == 1 && !msg.obj.equals("bothGameover")) {
                    new Thread(() -> {
                        while (!game.isGameOverFlag()) {
                            //每隔五秒，发送自己的分数到服务器
                            writer.println(game.getScore());
                            Log.i(TAG,"分数"+game.getScore());
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        gameOverFlag= game.isGameOverFlag();
                        writer.println("gameover");
                        Log.i(TAG,"游戏结束");
                    }
                    ).start();
                }
                else if (msg.what == 1 && msg.obj.equals("end")) {
                    Intent intent = new Intent(OnlineActivity.this, OverActivity.class);
                    intent.putExtra("myScore", game.getScore());
                    intent.putExtra("opponentScore",opponentScore);

                    startActivity(intent);
                }
                else if (msg.obj.toString().startsWith("score:")) {
                    opponentScore = Integer.parseInt(msg.toString().split(":")[1]);
                }
            }
        };

        new Thread(new NetConn(handler)).start();

    }

    private class NetConn extends Thread {
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
            Thread receiveServerMsg = new Thread(){
                @Override
                    public void run(){
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
                }
            };
            receiveServerMsg.start();
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


}
