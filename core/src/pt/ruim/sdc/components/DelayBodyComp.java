package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by ruimadeira on 16/01/16.
 */
public class DelayBodyComp implements Component {
    public float countdown;

    public DelayBodyComp(float countdown){
        this.countdown = countdown;
    }
}
