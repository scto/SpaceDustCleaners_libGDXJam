package pt.ruim.sdc.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import pt.ruim.sdc.PhysicsFilters;
import pt.ruim.sdc.UserData;
import pt.ruim.sdc.components.*;
import pt.ruim.sdc.systems.PhysicsSys;

/**
 * Created by ruimadeira on 12/01/16.
 */
public class PlayerFactory extends Factory {

    public PlayerFactory(World world, float worldScale){
        super(world, worldScale);
    }

    public Entity create(){
        //create empty entity
        Entity e = new Entity();

        //create TextureComp
        TextureComp tc = new TextureComp("player.png");
        e.add(tc);

        //create body
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.fixedRotation = true;
        bd.position.x = (Gdx.graphics.getWidth() / 2) * worldScale;
        bd.position.y = Gdx.graphics.getHeight() / 2 * worldScale;
        Body body = world.createBody(bd);
        //create body user data
        body.setUserData(new UserData(e, UserData.Type.PLAYER));

        //create body shape
        PolygonShape ps = new PolygonShape();
        //float boxW = (float)tc.texture.getWidth() * worldScale;
        //float boxH = (float)tc.texture.getHeight() * worldScale;

        float boxW = 18f * worldScale;
        float boxH = 45f * worldScale;
        Vector2 center = new Vector2(0f, -5.7f);
        center.scl(worldScale);
        ps.setAsBox(boxW*0.5f, boxH*0.5f, center, 0);
        Fixture fix = body.createFixture(ps, 1);
        Filter filter = fix.getFilterData();
        filter.categoryBits = PhysicsFilters.PLAYER_MASK;
        filter.maskBits = PhysicsFilters.PLATFORM_MASK | PhysicsFilters.DUST_MASK | PhysicsFilters.MONSTER_MASK | PhysicsFilters.COMET_MASK | PhysicsFilters.EXPLOSION_MASK | PhysicsFilters.WALL_MASK;
       // filter.maskBits = PhysicsFilters.COMET_MASK | PhysicsFilters.
        fix.setFilterData(filter);

        //create foot shape (used to know when touching floor, to jump)
        PolygonShape foot = new PolygonShape();
        float footW = boxW * 0.5f;
        float footH = boxH*0.1f;

        center.y -= boxH*0.5f;
        foot.setAsBox(footW*0.5f, footH*0.5f, center, 0);
        Fixture footFix = body.createFixture(foot, 1);
        footFix.setSensor(true);
        footFix.setUserData(new UserData(e, UserData.Type.PLAYER_FOOT));
        filter.maskBits = PhysicsFilters.PLATFORM_MASK | PhysicsFilters.COMET_MASK;
        footFix.setFilterData(filter);

        //set mass as 1
        MassData md = body.getMassData();
        md.mass = 1;
        body.setMassData(md);

        //create BodyComponent
        e.add(new BodyComp(body, worldScale));

        //create PlayerMovementComponent
        e.add(new PlayerMovementComp());

        e.add(new RemoveComp());

        //return entity
        return e;
    }
}
