package pt.ruim.sdc.factories;

import com.badlogic.gdx.physics.box2d.World;
import pt.ruim.sdc.systems.PhysicsSys;

/**
 * Created by ruimadeira on 12/01/16.
 */
public class Factory {
    World world;
    float worldScale;

    public Factory(World world, float worldScale){
        this.world = world;
        this.worldScale = worldScale;
    }

    public Factory(PhysicsSys physicsSys){
        this(physicsSys.getWorld(), physicsSys.getWorldScale());
    }
}
