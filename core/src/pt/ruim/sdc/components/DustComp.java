package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by ruimadeira on 30/12/15.
 */
public class DustComp implements Component {
    public float counter;
    public float counterAdd;

    public DustComp(){
        counter = MathUtils.random(0f, 100f);
        counterAdd = MathUtils.random(2f, 4f);
    }
}
