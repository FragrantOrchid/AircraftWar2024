package cn.org.orchid.aircraftwar2024.factory.supply_factory;

import cn.org.orchid.aircraftwar2024.supply.AbstractFlyingSupply;
import cn.org.orchid.aircraftwar2024.supply.HpSupply;

/**
 * 加血道具工厂
 *
 * @author hitsz
 */
public class HpSupplyFactory implements SupplyFactory {

    @Override
    public AbstractFlyingSupply createFlyingSupply(int x, int y) {
        return new HpSupply(x,y,0,2);
    }
}
