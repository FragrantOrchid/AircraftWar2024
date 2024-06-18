package cn.org.orchid.aircraftwar2024.player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PlayerDao {
    public Player findByUUId(UUID uuid);
    public List<Player> getAllPlayers();
    public void doAdd(Player player);
    public void doDelete(UUID uuid);
}
