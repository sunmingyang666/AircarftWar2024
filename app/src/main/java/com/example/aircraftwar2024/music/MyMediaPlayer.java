package com.example.aircraftwar2024.music;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.aircraftwar2024.R;


public class MyMediaPlayer {

    private boolean isMusicOn = false;

    private boolean isBossBGMOn =false;
    private MediaPlayer bgmMP;
    private MediaPlayer bossMP=null;
    private int position;

    public MyMediaPlayer() {

    }

    public void setMusicOn(boolean musicOn) {
        isMusicOn = musicOn;
    }

    public void playBGM(Context context) {
        if (isMusicOn) {
            bgmMP = MediaPlayer.create(context, R.raw.bgm);
            bgmMP.start();
            bgmMP.setLooping(true);
        }

    }

    public void playBossBGM(Context context) {
        if (isMusicOn&!isBossBGMOn) {
            bgmMP.pause();
            position=bgmMP.getCurrentPosition();
            bossMP = MediaPlayer.create(context, R.raw.bgm_boss);
            bossMP.start();
            bossMP.setLooping(true);
            this.isBossBGMOn=true;
        }
    }

    public void endBossBGM(Context context){
        if(isMusicOn&isBossBGMOn){
            bgmMP.seekTo(position);
            bgmMP.start();
            this.isBossBGMOn=false;
            if(bossMP!=null){
                bossMP.stop();
                bossMP.release();
                bossMP=null;
            }
        }
    }


    public void endGame() {
        if (isMusicOn) {
            bgmMP.stop();
            if(bossMP!=null){
                bossMP.stop();
                bossMP.release();
                bossMP=null;
            }
        }

    }
}
