package cn.org.orchid.aircraftwar2024.music;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.util.HashMap;

import cn.org.orchid.aircraftwar2024.R;

public class GameSoundPool {
    //1:bomb_explosion，炸弹爆炸
    //2:bullet_hit，子弹击中
    //3:game_over，游戏结束
    //4:get_supply，补血
    SoundPool mysp;
    HashMap<Integer,Integer> soundPoolMap;

    boolean sound;
    Context context;

    private  static GameSoundPool gameSoundPool;

    public static void InitGameSoundPool(Context context,boolean sound) {
        gameSoundPool = new GameSoundPool(context,sound);
    }
    public static GameSoundPool getGameSoundPool(){
        return gameSoundPool;
    }
    private GameSoundPool(Context context,boolean sound) {
        this.sound = sound;
        this.context = context;
        createSoundPool();//创建SoundPool对象
        soundPoolMap = new HashMap<Integer,Integer>();
        if(sound){
            soundPoolMap.put(1,mysp.load(context, R.raw.bomb_explosion,1));
            soundPoolMap.put(2,mysp.load(context, R.raw.bullet_hit,1));
            soundPoolMap.put(3,mysp.load(context, R.raw.game_over,1));
            soundPoolMap.put(4,mysp.load(context, R.raw.get_supply,1));
        }
    }
    private void createSoundPool() {
        if (mysp == null) {
            // Android 5.0 及 之后版本
            AudioAttributes audioAttributes = null;
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            mysp = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
    }
    public void playBombExplosionSound(){
        if(soundPoolMap != null && soundPoolMap.containsKey(1)) {
            mysp.play(soundPoolMap.get(1),1,1,0,0,1);
        }
    }
    public void playBulletHitSound(){
        if(soundPoolMap != null && soundPoolMap.containsKey(2)) {
            Log.v("Pool","contain2");
            mysp.play(soundPoolMap.get(2),1,1,0,0,1);
        }
    }
    public void playGameOverSound(){
        if(soundPoolMap != null && soundPoolMap.containsKey(3)) {
            mysp.play(soundPoolMap.get(3),1,1,0,0,1);
        }
    }
    public void playGetSupplySound(){
        if(soundPoolMap != null && soundPoolMap.containsKey(4)) {
            mysp.play(soundPoolMap.get(4),1,1,0,0,1);
        }
    }
    public void vanish(){
        mysp = null;
        soundPoolMap = null;
        context = null;
        sound = false;
    }
}
