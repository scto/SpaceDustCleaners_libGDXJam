package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import pt.ruim.sdc.UserData;


/**
 * Created by ruimadeira on 29/12/15.
 */
public class BodyComp implements Component {
    public Body body;
    public float worldScale, invWorldScale;
    public boolean toDestroy;
    public boolean updateTextureTransform;

    public BodyComp(Body body, float worldScale){
        this.body = body;
        this.worldScale = worldScale;
        invWorldScale = 1.0f / worldScale;
        toDestroy = false;
        updateTextureTransform = true;
    }
}
