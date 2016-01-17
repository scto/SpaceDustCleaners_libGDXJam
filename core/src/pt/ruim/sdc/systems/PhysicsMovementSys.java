package pt.ruim.sdc.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import pt.ruim.sdc.components.BodyComp;
import pt.ruim.sdc.components.PhysicsMovementComp;

/**
 * Created by ruimadeira on 04/01/16.
 */
public class PhysicsMovementSys extends IteratingSystem {


    private static Family family = Family.all(PhysicsMovementComp.class, BodyComp.class).get();
    Vector2 tempVel;

    public PhysicsMovementSys(){
        super(family);
        tempVel = new Vector2();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComp b = entity.getComponent(BodyComp.class);
        PhysicsMovementComp pm = entity.getComponent(PhysicsMovementComp.class);
        if(pm.applyImpulse){
            b.body.applyLinearImpulse(pm.impulse, b.body.getWorldCenter(), true);
        }
        if(pm.applyForce){
            b.body.applyForceToCenter(pm.force, true);
        }
        if(pm.applyVelX || pm.applyVelY) {
            Vector2 currVel = b.body.getLinearVelocity();
            if (pm.applyVelX) {
                if (pm.easeVel) {

                } else {

                }
            }
            if (pm.applyVelY) {
                if (pm.easeVel) {

                } else {

                }
            }
        }
    }
}
