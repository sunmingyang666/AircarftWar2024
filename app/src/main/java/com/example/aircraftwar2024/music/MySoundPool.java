package com.example.aircraftwar2024.music;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aircraftwar2024.R;

import java.util.HashMap;

public class MySoundPool{
    private SoundPool mysp;
    private HashMap<Integer, Integer> soundPoolMap;
    private boolean isMusicOn = false;

    public MySoundPool(){

    }
    public void putMusic(Context context){
        AudioAttributes audioAttributes = null;
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mysp = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(1, mysp.load(context, R.raw.bomb_explosion, 1));
        soundPoolMap.put(2, mysp.load(context, R.raw.bullet_hit, 1));
        soundPoolMap.put(3, mysp.load(context, R.raw.game_over, 1));
        soundPoolMap.put(4, mysp.load(context, R.raw.get_supply, 1));
    }

    public void setMusicOn(boolean musicOn) {
        isMusicOn = musicOn;
    }

    public void playMusic(int order){
        if(isMusicOn){
            mysp.play(soundPoolMap.get(order),1,1,0,0,1);
            System.out.println("播放音乐"+order);
        }
    }
}
