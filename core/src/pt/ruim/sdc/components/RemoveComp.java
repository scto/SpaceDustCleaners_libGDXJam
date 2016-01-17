package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by ruimadeira on 30/12/15.
 */
public class RemoveComp implements Component{
    public boolean remove;
    public boolean removeWithCountdown;
    public float countdown;
}
