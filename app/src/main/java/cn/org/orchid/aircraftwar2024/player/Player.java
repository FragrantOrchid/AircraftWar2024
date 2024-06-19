package cn.org.orchid.aircraftwar2024.player;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Player implements Comparable<Player>, Serializable {
    private Map<String,Object> player;
    public Player(String id, Date date, int score) {
        player = new HashMap<String, Object>();
        player.put("id",id);
        player.put("date",date);
        player.put("score",score);
        player.put("uuid", UUID.randomUUID());
    }

    @Override
    public int compareTo(Player o) {
        return Integer.compare(o.getScore(),this.getScore());
    }
    public String getPlayerId() {

        return (String) player.get("id");
    }
    public Date getDate() {

        return (Date) player.get("date");
    }
    public int getScore() {
        if(player.containsKey("score")){
            return (int) player.get("score");
        } else {
            return -1;
        }
    }

    public UUID getUUID(){

        return (UUID)player.get("uuid");
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(getUUID(),player.getUUID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUUID());
    }
}
