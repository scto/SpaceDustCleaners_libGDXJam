package pt.ruim.sdc;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import pt.ruim.sdc.factories.SpawnMarkFactory;
import pt.ruim.sdc.factories.MonsterFactory;
import pt.ruim.sdc.systems.PhysicsSys;

/**
 * Created by ruimadeira on 03/01/16.
 */
public class DogMonsterSpawner extends EntitySpawner {

    MonsterFactory factory;
    SpawnMarkFactory markFactory;

    public float delay;

    public DogMonsterSpawner(Engine engine) {
        super(engine);
        PhysicsSys physicsSys = engine.getSystem(PhysicsSys.class);
        factory = new MonsterFactory(physicsSys.getWorld(), physicsSys.getWorldScale());
        markFactory = new SpawnMarkFactory();
        delay = 0.5f;
    }

    public void start(){
        super.start();
        spawnCountdown = 0;
    }

    @Override
    protected Entity spawn() {
        float w =  (float)Gdx.graphics.getWidth();
        float x = MathUtils.random(w*0.1f, w*0.9f);
        return factory.createDogMonster(x, delay);
    }
}
