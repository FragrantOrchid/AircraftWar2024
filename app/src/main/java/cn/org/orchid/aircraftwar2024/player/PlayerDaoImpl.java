package cn.org.orchid.aircraftwar2024.player;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PlayerDaoImpl implements PlayerDao{
    private List<Map<String, Object>> players = null;
    public PlayerDaoImpl() {
        players = new ArrayList<Map<String, Object>>();
    }
    public int getPlayersNums() {
        return players.size();
    }
    @Override
    public void findById(String playerid) {
        for(Map<String,Object> player : players) {
            if(player.get("id").toString().equals(playerid)) {
                Log.v("Dao","EQUAL");
            }
        }
    }

    @Override
    public List<Map<String, Object>> getAllPlayers() {
        return players;
    }

    @Override
    public void doAdd(Map<String,Object> map) {
        players.add(map);

    }

    @Override
    public void doDelete(String playerid) {
        for(Map<String,Object> player : players) {
            if(player.get("id").toString().equals(playerid)) {
                players.remove(player);
            }
        }
    }
    public void saveAll() throws IOException {
        File file = new File("Players.txt");
        //创建文件输出流
        FileOutputStream fos = new FileOutputStream(file);
        //创建对象输出流
        ObjectOutputStream oos =new ObjectOutputStream(fos);
        //保存对象
        oos.writeObject(players);
    }
    public void loadAll() throws IOException, ClassNotFoundException {
        File file = new File("Players.txt");
        if(file.length() > 0) {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            players = (List<Map<String, Object>>)ois.readObject();
        } else {
            Log.v("Dao","no data");
        }
    }
    public void Order() {
        Collections.sort(players, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> stringObjectMap, Map<String, Object> t1) {
                int score1 = (int)stringObjectMap.get("score");
                int score2 = (int)t1.get("score");
                return Integer.compare(score2,score1);
            }
        });
    }

}
