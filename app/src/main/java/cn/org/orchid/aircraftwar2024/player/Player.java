package cn.org.orchid.aircraftwar2024.player;

import java.io.Serializable;
import java.util.Date;

public class Player implements Comparable<Player>, Serializable {
    private String id;
    private Date date;
    private int score;
    public Player(String id, Date date, int score) {
        this.id = id;
        this.date = date;
        this.score = score;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPlayerId() {
        return id;
    }
    @Override
    public int compareTo(Player o) {
        return Integer.compare(o.getScore(), score);

    }
    public Date getTime() {
        return date;
    }
    public int getScore() {
        return score;
    }
}
