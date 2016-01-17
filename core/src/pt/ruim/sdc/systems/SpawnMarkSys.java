package pt.ruim.sdc.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import pt.ruim.sdc.components.RemoveComp;
import pt.ruim.sdc.components.SpawnMarkComp;
import pt.ruim.sdc.components.TextureComp;

/**
 * Created by ruimadeira on 16/01/16.
 */
public class SpawnMarkSys extends IteratingSystem {

    private static Family family = Family.all(SpawnMarkComp.class, TextureComp.class).get();

    public SpawnMarkSys() {
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SpawnMarkComp sm = entity.getComponent(SpawnMarkComp.class);
        TextureComp t = entity.getComponent(TextureComp.class);
        if(sm.countdown > 0){
            sm.countdown -= deltaTime;
            if(sm.countdown <= 0){
                sm.countdown = 0;
            }
            float growSpeed = 20f;
            if(t.width < sm.targetWidth){
                t.width += growSpeed;
                if(t.width > sm.targetWidth){
                    t.width = sm.targetWidth;
                }
            }
            if(t.height < sm.targetHeight){
                t.height += growSpeed;
                if(t.height > sm.targetHeight){
                    t.height = sm.targetHeight;
                }
            }
        } else {
            t.color.a -= 0.05f;
            if(t.color.a <= 0){
                t.color.a = 0;
                RemoveComp r = entity.getComponent(RemoveComp.class);
                r.remove = true;
            }
        }
    }
}
