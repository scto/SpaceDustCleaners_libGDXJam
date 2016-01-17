package pt.ruim.sdc.components.monsters;


import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by ruimadeira on 02/01/16.
 */
public class PlatformMonsterComp implements Component{
    public float minX, maxX;
    public float standCountdown, standInterval;

    public PlatformMonsterComp(float minX, float maxX){
        this.minX = minX;
        this.maxX = maxX;
        standInterval = 1;
    }
}
