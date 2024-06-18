package cn.org.orchid.aircraftwar2024.player;

import java.util.List;
import java.util.Map;

public interface PlayerDao {
    public void findById(String playerid);
    public List<Map<String, Object>> getAllPlayers();
    public void doAdd(Map<String,Object> map);
    public void doDelete(String playerid);
}
