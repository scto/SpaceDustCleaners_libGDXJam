package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by ruimadeira on 16/01/16.
 */
public class SpawnMarkComp implements Component{
    public float countdown;
    public float targetWidth, targetHeight;

    public SpawnMarkComp(float countdown){
        this.countdown = countdown;
    }
}
