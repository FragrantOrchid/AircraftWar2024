package cn.org.orchid.aircraftwar2024.music;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

import cn.org.orchid.aircraftwar2024.R;

public class GameMediaPlayer {
    MediaPlayer bgm = null;
    MediaPlayer bgm_boss = null;
    Context context;
    public GameMediaPlayer(Context context) {
        this.context = context;
        //InitMediaPlayer();
    }
    public void InitMediaPlayer() {
        bgm = MediaPlayer.create(context, R.raw.bgm);
        bgm_boss = MediaPlayer.create(context,R.raw.bgm_boss);
    }
    public void beginBgm(){
        if(bgm != null) {
            bgm.start();
            bgm.setLooping(true);
        }

    }
    public void pauseBgm() {
        if(bgm != null){
            bgm.pause();
        }

    }
    public void continueBgm() {
        if(bgm != null) {
            int position = bgm.getCurrentPosition();
            bgm.seekTo(position);
            bgm.start();
        }

    }
    public void stopBgm() {
        if(bgm != null) {
            bgm.stop();
            bgm.release();
            bgm = null;
        }

    }
    public void beginBossBgm() {
        if(bgm_boss != null) {
            bgm_boss.start();
            bgm_boss.setLooping(true);
        }

    }
    public void stopBossBgm(){
        if(bgm_boss != null){
            bgm_boss.pause();
            bgm_boss.seekTo(0);
            //bgm_boss = null;
        }
    }
    public void stopAndReleaseAll() {
        if(bgm != null) {
            bgm.stop();
            bgm.release();
            bgm = null;
        }
        if(bgm_boss != null){
            bgm_boss.stop();
            bgm_boss.release();
            bgm_boss = null;
        }
    }

}
