package pt.ruim.sdc.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import pt.ruim.sdc.components.BodyComp;
import pt.ruim.sdc.components.DelayBodyComp;
import pt.ruim.sdc.components.TextureComp;

/**
 * Created by ruimadeira on 16/01/16.
 */
public class BodyEnableDelaySys extends IteratingSystem {

    private static Family family = Family.all(BodyComp.class, DelayBodyComp.class, TextureComp.class).get();

    public BodyEnableDelaySys() {
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DelayBodyComp bed = entity.getComponent(DelayBodyComp.class);
        bed.countdown -= deltaTime;
        if(bed.countdown <= 0){
            BodyComp b = entity.getComponent(BodyComp.class);
            b.body.setActive(true);
            TextureComp t = entity.getComponent(TextureComp.class);
            t.visible = true;
            entity.remove(DelayBodyComp.class);
        }
    }
}
