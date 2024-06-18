package cn.org.orchid.aircraftwar2024.factory.enemy_factory;

import cn.org.orchid.aircraftwar2024.ImageManager;
import cn.org.orchid.aircraftwar2024.activity.GameActivity;
import cn.org.orchid.aircraftwar2024.aircraft.AbstractEnemyAircraft;
import cn.org.orchid.aircraftwar2024.aircraft.MobEnemy;

public class MobFactory implements EnemyFactory {

    private int mobHp = 30;
    private int speedY = 10;

    @Override
    public AbstractEnemyAircraft createEnemyAircraft(double level) {
        return new MobEnemy(
                (int) ( Math.random() * (GameActivity.screenWidth - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * GameActivity.screenHeight * 0.05),
                0,
                (int) (this.speedY * level),
                (int) (this.mobHp * level));
    }

}
