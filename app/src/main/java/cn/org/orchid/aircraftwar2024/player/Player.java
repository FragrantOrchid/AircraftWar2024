package cn.org.orchid.aircraftwar2024.player;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class Player implements Comparable<Player>, Serializable {
    private Map<String,Object> player;
    public Player(String id, Date date, int score) {
        player.put("id",id);
        player.put("date",date);
        player.put("score",score);
        player.put("uuid", UUID.randomUUID();
    }
    public String getPlayerId() {
        return (String) player.get("id");
    }
    @Override
    public int compareTo(Player o) {
            return Integer.compare(o.getScore(), (int) player.get("score"));
    }
    public Date getDate() {

        return (Date) player.get("date");
    }
    public int getScore() {
        return (int) player.get("score");
    }

    public UUID getUUID(){
        return (UUID)player.get("uuid");
    }
}
