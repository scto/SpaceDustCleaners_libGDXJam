package pt.ruim.sdc.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;
import pt.ruim.sdc.components.BodyComp;
import pt.ruim.sdc.components.RemoveComp;

/**
 * Created by ruimadeira on 30/12/15.
 */
public class RemoveSys extends IteratingSystem {

    private static Family family = Family.all(RemoveComp.class).get();

    ComponentMapper<RemoveComp> rm;
    ComponentMapper<BodyComp> bm;
    PhysicsSys physicsSys;

    public RemoveSys(PhysicsSys physicsSys) {
        super(family);
        this.physicsSys = physicsSys;
        rm = ComponentMapper.getFor(RemoveComp.class);
        bm = ComponentMapper.getFor(BodyComp.class);

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        RemoveComp r = rm.get(entity);
        if(r.removeWithCountdown){
            if(r.countdown > 0){
                r.countdown -= deltaTime;
            }
            if(r.countdown <= 0){
                r.countdown = 0;
                r.remove = true;
            }
        }
        if(r.remove) {
            BodyComp b = bm.get(entity);
            if(b != null){
                physicsSys.getWorld().destroyBody(b.body);
            }
            getEngine().removeEntity(entity);
        }

    }
}
