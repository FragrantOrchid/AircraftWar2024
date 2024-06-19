package cn.org.orchid.aircraftwar2024.player;

import android.content.Context;
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
    Context context;
    String fileName;
    public PlayerDaoImpl(Context context, int gameType) {
        this.gameType = gameType;
        this.context = context;
        //1:easy;2:medium;3:hard
        switch (gameType) {
            case 1:
                fileName = "easyPlayers.txt";
                break;
            case 2:
                fileName = "mediumPlayers.txt";
                break;
            case 3:
                fileName = "hardPlayers.txt";
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
        Log.v("Dao","saveall");
        File file = new File(context.getFilesDir(),fileName);
        if(!file.exists()){
            file.createNewFile();
        }
        //创建文件输出流，用构建时得到的context
        FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
        //创建对象输出流
        ObjectOutputStream oos =new ObjectOutputStream(fos);
        //保存对象
        oos.writeObject(players);


    }
    public void loadAll() throws IOException, ClassNotFoundException {
        Log.v("Dao","loadall");
        File file = new File(context.getFilesDir(),fileName);
        if(!file.exists()){
            file.createNewFile();
            Log.v("Dao","no data");
            players = new ArrayList<Player>();
        } else {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            players = (ArrayList<Player>) ois.readObject();
        }
    }
    public void Order() {
        Collections.sort(players);
    }

}
