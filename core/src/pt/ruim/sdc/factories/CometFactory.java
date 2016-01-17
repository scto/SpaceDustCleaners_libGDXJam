package pt.ruim.sdc.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import pt.ruim.sdc.ColorUtils;
import pt.ruim.sdc.PhysicsFilters;
import pt.ruim.sdc.UserData;
import pt.ruim.sdc.components.*;

/**
 * Created by ruimadeira on 04/01/16.
 */
public class CometFactory extends Factory{

    Color color;

    public CometFactory(World world, float worldScale){
        super(world, worldScale);
        color = ColorUtils.fromRGB(254, 244, 79);
    }

    public Entity create(final Vector2 pos, final Vector2 iniVel, float delay){
        Entity e = new Entity();

        TextureComp tc = new TextureComp("comet.png");
        tc.color.set(color);
        e.add(tc);

        boolean active = delay == 0;
        Body body = createBody(world, pos, iniVel, active);
        float radius = tc.width * 0.5f;
        createMainFixture(body, radius);
        //createSensorFixture(body, e, 100f);

        body.setUserData(new UserData(e, UserData.Type.COMET));

        BodyComp bc = new BodyComp(body, worldScale);
        e.add(bc);

        if(delay > 0){
            DelayBodyComp db = new DelayBodyComp(delay);
            e.add(db);
            tc.visible = false;
        }

        e.add(new RemoveComp());

        CometComp cc = new CometComp(2f, radius);
        e.add(cc);

        return e;
    }

    private void createMainFixture(Body body, float radius){
        CircleShape shape = new CircleShape();
        shape.setRadius(radius*worldScale);
        Fixture fix = body.createFixture(shape, 1);
        fix.setRestitution(0.2f);
        fix.setFriction(10000f);
        Filter filter = fix.getFilterData();
        filter.categoryBits = PhysicsFilters.COMET_MASK;
        filter.maskBits = PhysicsFilters.COMET_MASK | PhysicsFilters.PLAYER_MASK | PhysicsFilters.PLATFORM_MASK | PhysicsFilters.EXPLOSION_MASK | PhysicsFilters.WALL_MASK;
        fix.setFilterData(filter);
    }

    private Body createBody(World world, Vector2 pos, Vector2 iniVel, boolean active){
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.linearVelocity.set(iniVel);
        bd.position.x = pos.x * worldScale;
        bd.position.y = pos.y * worldScale;
        bd.gravityScale = 0.5f;
        bd.linearDamping = 2f;
        bd.active = active;
        Body body = world.createBody(bd);
        return body;
    }


}
