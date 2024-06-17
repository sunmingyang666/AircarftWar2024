package com.example.aircraftwar2024.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aircraftwar2024.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class OnlineActivity extends AppCompatActivity {

    private Socket socket;
    private PrintWriter writer;
    private Handler handler;
    private BufferedReader reader;
    private static int opponentScore=0;
    private GameView view;

    private static boolean gameOverFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new HardGame(OnlineActivity.this);
        view.setPlayMusic(MainActivity.myBinder != null);
        setContentView(view);
        Handler handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1 && msg.obj.equals("start")) {
                    String serverMsg = (String) msg.obj;
                    new Thread(() -> {
                        while (!view.isGameOverFlag()) {
                            //每隔五秒，发送自己的分数到服务器
                            MainActivity.writer.println(view.getScore());
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        gameOverFlag = true; // 设置自己游戏结束标志
                        MainActivity.writer.println("gameover");
                    }
                    ).start();
                }
                else if (msg.what == 1 && msg.obj.equals("bothGameover")) {
                    Intent intent = new Intent(OnlineActivity.this, OverActivity.class);
                    intent.putExtra("myScore",Game.score);
                    intent.putExtra("opponentScore",opponentScore);
                    intent.putExtra("myName",myName);
                    intent.putExtra("opName",opName);
                    startActivity(intent);
                }
                else if (msg.obj.startsWith("score:")) {
                    opponentScore = Integer.parseInt(serverMsg.split(":")[1]);
                }
            }
        };

        new Thread(new NetConn(handler)).start();

    }

    private class NetConn extends Thread {
        private Handler handler;

        public NetConn(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
                // 接收服务端信息
            Thread receiveServerMsg = new Thread(){
                @Override
                    public void run(){
                    String msg;
                    try{
                        while ((msg = MainActivity.reader.readLine()) != null){
                            Message msgFromServer = new Message();
                            msgFromServer.what = 1;
                            msgFromServer.obj = msg;
                            handler.sendMessage(msgFromServer);
                        }
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                }
            };
            receiveServerMsg.start();
        }
    }

    public static int getOpponentScore() {
        return opponentScore;
    }

    public void setGameOverFlag(boolean gameOverFlag) {
        OnlineActivity.gameOverFlag = gameOverFlag;
    }



}
