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
import java.util.Objects;
import java.util.UUID;

public class PlayerDaoImpl implements PlayerDao{
    private List<Player> players = null;
    int gameType;
    File file;
    public PlayerDaoImpl(int gameType) {
        this.gameType = gameType;
        //1:easy;2:medium;3:hard
        switch (gameType) {
            case 1:
                file = new File("easyPlayers.txt");
                break;
            case 2:
                file = new File("mediumPlayers.txt");
                break;
            case 3:
                file = new File("hardPlayers.txt");
                break;
            default:
                break;
        }
    }
    @Override
    public Player findByUUId(UUID uuid) {
        for(Player player : players){
            if(player.getUUID() == uuid){
                return player;
            }
        }
        return null;
    }

    @Override
    public List<Player> getAllPlayers() {
        return players;
    }

    @Override
    public void doAdd(Player player) {
        players.add(player);

    }

    @Override
    public void doDelete(UUID uuid) {
        Player player = findByUUId(uuid);
        players.remove(player);
    }
    public void saveAll() throws IOException {
        //创建文件输出流
        FileOutputStream fos = new FileOutputStream(file);
        //创建对象输出流
        ObjectOutputStream oos =new ObjectOutputStream(fos);
        //保存对象
        oos.writeObject(players);
    }
    public void loadAll() throws IOException, ClassNotFoundException {
        if(file.length() > 0) {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            players = (List<Player>) ois.readObject();
        } else {
            Log.v("Dao","no data");
        }
    }
    public void Order() {
        //TODO 完善order代码





    }

}
