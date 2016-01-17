package pt.ruim.sdc.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import pt.ruim.sdc.PhysicsFilters;
import pt.ruim.sdc.UserData;
import pt.ruim.sdc.components.BodyComp;
import pt.ruim.sdc.components.DustComp;
import pt.ruim.sdc.components.RemoveComp;
import pt.ruim.sdc.components.TextureComp;

/**
 * Created by ruimadeira on 12/01/16.
 */
public class DustFactory extends Factory {

    public DustFactory(World world, float worldScale){
        super(world, worldScale);
    }

    public Entity create(float x, float y){
        Entity e = new Entity();

        float radius = 5f;

        //body component
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.position.set(x*worldScale, y*worldScale);
        bd.gravityScale *= 0.01f;
        bd.linearDamping = 0.5f;
        Body body = world.createBody(bd);
        CircleShape circle = new CircleShape();
        circle.setRadius(radius*worldScale*0.7f);
        Fixture fix = body.createFixture(circle, 1);
        Filter filter = fix.getFilterData();
        filter.categoryBits = PhysicsFilters.DUST_MASK;
        filter.maskBits = PhysicsFilters.PLATFORM_MASK | PhysicsFilters.DUST_MASK | PhysicsFilters.EXPLOSION_MASK | PhysicsFilters.WALL_MASK;
        fix.setFilterData(filter);

        //create hit area that only collides with player (its a sensor).
        circle = new CircleShape();
        circle.setRadius((radius*2f)*worldScale);
        fix = body.createFixture(circle, 1);
        fix.setSensor(true);
        fix.setUserData(new UserData(e, UserData.Type.DUST));
        filter.maskBits = PhysicsFilters.PLAYER_MASK;
        fix.setFilterData(filter);

        body.setUserData(new UserData(e, UserData.Type.DUST));
        e.add(new BodyComp(body, worldScale));

        //texture component
        TextureComp tc = new TextureComp("dust.png");
        tc.transform.translate(x, y, 0);
        tc.width = radius*2f;
        tc.height = radius*2f;
        e.add(tc);

        //remove component
        e.add(new RemoveComp());

        //dust component
        DustComp d = new DustComp();
        d.counter = MathUtils.random(0f, 1000f);
        d.counterAdd = MathUtils.random(0.1f, 0.2f);
        e.add(new DustComp());

        return e;
    }

}
