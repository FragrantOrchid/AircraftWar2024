package cn.org.orchid.aircraftwar2024.factory.enemy_factory;


import cn.org.orchid.aircraftwar2024.ImageManager;
import cn.org.orchid.aircraftwar2024.activity.GameActivity;
import cn.org.orchid.aircraftwar2024.aircraft.AbstractEnemyAircraft;
import cn.org.orchid.aircraftwar2024.aircraft.BossEnemy;
import cn.org.orchid.aircraftwar2024.shoot.DisperseShoot;

/**
 *
 *
 *  @author hitsz
 */
public class BossFactory implements EnemyFactory {

    private int bossHp = 500;
    @Override
    public AbstractEnemyAircraft createEnemyAircraft(double bosslevel) {
        BossEnemy boss = new BossEnemy(
                (GameActivity.screenWidth - ImageManager.BOSS_ENEMY_IMAGE.getWidth()) / 2,
                (ImageManager.BOSS_ENEMY_IMAGE.getHeight())/2,
                5,
                0,
                (int)(bossHp * bosslevel));
        boss.setShootStrategy(new DisperseShoot());
        return boss;

    }
}
