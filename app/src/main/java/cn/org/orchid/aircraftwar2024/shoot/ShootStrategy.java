package cn.org.orchid.aircraftwar2024.shoot;

import cn.org.orchid.aircraftwar2024.aircraft.AbstractAircraft;
import cn.org.orchid.aircraftwar2024.bullet.AbstractBullet;

import java.util.List;

/**
 * 攻击方式接口
 * 【策略模式】
 *
 * @author hitsz
 */
public interface ShootStrategy {

    /**
     * 通过射击产生子弹
     *
     * @param abstractAircraft 子弹发射源
     * @return 射击出的子弹List
     */
    public abstract List<AbstractBullet> shootWithStrategy(AbstractAircraft abstractAircraft);


}