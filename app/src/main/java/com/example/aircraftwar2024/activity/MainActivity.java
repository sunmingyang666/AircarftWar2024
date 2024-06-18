package com.example.aircraftwar2024.activity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

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

public class MainActivity extends AppCompatActivity {
    private RadioButton radioButton1,radioButton2;
    private Button startBotton;
    private Button onlineButton;
    public static Socket socket;
    public PrintWriter writer;
    public BufferedReader reader;
    public Handler handler;
    private EditText txt;
    public boolean isMusicOn;
    public boolean isOnline;
    private static  final String TAG = "MainActivity";
    //test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        startBotton = findViewById(R.id.startButton);
        onlineButton = findViewById(R.id.onlineButton);
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
                isMusicOn = radioButton1.isChecked();
                intent.putExtra("music",isMusicOn);
                startActivity(intent);
            }
        });
        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("匹配中，请等待......")
                        .setTitle("");
                AlertDialog dialog = builder.create();
                dialog.show();
                handler = new Handler(getMainLooper()){
                    //当数据处理子线程更新数据后发送消息给UI线程，UI线程更新UI
                    @Override
                    public void handleMessage(Message msg){
                        if(msg.what == 1 && msg.obj.equals("start")){
                            dialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, OnlineActivity.class);
                            intent.putExtra("gameType",2);
                            intent.putExtra("music",isMusicOn);
                            startActivity(intent);
                            try{
                                socket.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                };        
                new NetConn(handler).start();
            }
        });
    }
    private class NetConn extends Thread{
        private Handler handler;
        public NetConn(Handler handler){
            this.handler = handler;
        }
        @Override
        public void run(){
            try{
                socket = new Socket();
                socket.connect(new InetSocketAddress
                        ("10.0.2.2",9999),5000);
                reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(),"utf-8"));
                writer = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream(),"utf-8")),true);
                Log.i(TAG,"connect to server");

                //接收服务器返回的数据
                Thread receiveServerMsg =  new Thread(){
                    @Override
                    public void run(){
                        String msgFromserver;
                        try{
                            while((msgFromserver = reader.readLine())!=null)
                            {
                                //发送消息给UI线程
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = msgFromserver;
                                handler.sendMessage(msg);
                                Log.i(TAG,msgFromserver);
                            }
                        }catch (IOException ex){
                            ex.printStackTrace();
                        }
                    }
                };
                receiveServerMsg.start();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}