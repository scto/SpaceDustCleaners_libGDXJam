package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by ruimadeira on 04/01/16.
 */
public class PhysicsMovementComp implements Component {
    public Vector2 force, impulse, vel;
    public boolean applyForce, applyImpulse, applyVelX, applyVelY;
    public boolean easeVel;
    public float velDamp;

    public PhysicsMovementComp(){
        force = new Vector2();
        impulse = new Vector2();
        vel = new Vector2();
        easeVel = true;
        velDamp = 0.2f;
    }
}
