package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by ruimadeira on 30/12/15.
 */
public class SpawnDustComp implements Component {
    public float countdown;
    public float maxInterval, minInterval;
    public int minAmount, maxAmount;
    public Entity floor;
    public SpawnDustComp(float minInterval, float maxInterval, int minAmount, int maxAmount){
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }
}
