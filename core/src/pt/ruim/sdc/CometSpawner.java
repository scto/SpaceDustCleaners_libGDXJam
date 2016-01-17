package pt.ruim.sdc;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import pt.ruim.sdc.factories.CometFactory;
import pt.ruim.sdc.systems.PhysicsSys;

/**
 * Created by ruimadeira on 04/01/16.
 */
public class CometSpawner extends EntitySpawner{

    CometFactory factory;
    Vector2 pos, vel;

    public float delay;

    public CometSpawner(Engine engine, PhysicsSys physicsSys) {
        super(engine);
        World world = physicsSys.getWorld();
        float worldScale = physicsSys.getWorldScale();
        factory = new CometFactory(world, worldScale);

        pos = new Vector2();
        vel = new Vector2();

        delay = 0.5f;
    }

    protected Entity spawn(){
        pos.y = Gdx.graphics.getHeight() + 30;
        float w = (float)Gdx.graphics.getWidth();
        pos.x = MathUtils.random(w*0.1f, w*0.9f);

        vel.x = MathUtils.random(-50f, 50f);
        vel.y = 0;

        return factory.create(pos, vel, delay);
    }
}
