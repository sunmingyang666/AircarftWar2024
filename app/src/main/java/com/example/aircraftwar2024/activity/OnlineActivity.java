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
    private int opponentScore=10;
    private BaseGame game;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isMusicOn = getIntent().getBooleanExtra("music", false);

        game = new HardGame(OnlineActivity.this);
        game.setMusic(isMusicOn);
        game.setOnline();
        setContentView(game);
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                new Thread(() -> {
                    while (!game.getGameOverFlag()) {
                        for (int i = 0; i < 100; i++) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (game.getGameOverFlag()) {
                                break;
                            }
                        }
                        writer.println(Integer.toString(game.getScore()));
                    }
                    writer.println(Integer.toString(game.getScore()));
                    writer.println("gameover");
                    if (writer != null) {
                        writer.close();
                        Log.i(TAG, "send gameover to server");
                    }
                    while(!game.getOnlineEnd()){
                        try {
                            if(msg.what==3){
                                game.setOnlineEnd();
                                break;
                            }
                            Thread.sleep(50);
                        }
                        catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    Intent intent = new Intent(OnlineActivity.this, OfflineActivity.class);
                    intent.putExtra("myScore", game.getScore());
                    intent.putExtra("opponentScore",opponentScore);
                    startActivity(intent);
                    onDestroy();
                }).start();

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
                new Thread(() ->{
                    String msgFromServer;
                    try{
                        while ((msgFromServer = reader.readLine()) != null){
                            Log.i(TAG,"data back:"+msgFromServer);
                            Message msg = new Message();
                            if(msgFromServer.startsWith("score:")){
                                msg.what = 2;
                                msg.obj = msgFromServer;
                                opponentScore = Integer.parseInt(msgFromServer.split(":")[1]);
                                toClienthandler.sendMessage(msg);
                                game.setOpponentScore(opponentScore);
                            } else if (msgFromServer.equals("end")) {
                                game.setOnlineEnd();
                                Log.i(TAG,"end");
                                msg.what = 3;
                                msg.obj = msgFromServer;
                                toClienthandler.sendMessage(msg);
                            } else {
                                msg.what = 1;
                                msg.obj = msgFromServer;
                                toClienthandler.sendMessage(msg);
                            }
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
