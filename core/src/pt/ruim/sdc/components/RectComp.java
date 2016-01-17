package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by ruimadeira on 30/12/15.
 */
public class RectComp implements Component{
    public Rectangle rectangle;

    public RectComp(final Rectangle r){
        rectangle = new Rectangle(r);
    }

    public RectComp(float x, float y, float w, float h){
        rectangle = new Rectangle(x, y, w, h);
    }

}
