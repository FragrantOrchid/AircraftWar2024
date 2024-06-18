package cn.org.orchid.aircraftwar2024.factory.supply_factory;


import cn.org.orchid.aircraftwar2024.supply.AbstractFlyingSupply;
import cn.org.orchid.aircraftwar2024.supply.BombSupply;

/**
 * 炸弹道具工厂
 *
 * @author hitsz
 */
public class BombSupplyFactory implements SupplyFactory {

    @Override
    public AbstractFlyingSupply createFlyingSupply(int x, int y) {
        return new BombSupply(x, y,0,2);
    }

}
