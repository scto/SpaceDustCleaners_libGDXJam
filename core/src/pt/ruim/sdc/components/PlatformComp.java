package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by ruimadeira on 31/12/15.
 */
public class PlatformComp implements Component {
    public int floorIndex;

    public PlatformComp(int floorIndex){
        this.floorIndex = floorIndex;
    }
}
