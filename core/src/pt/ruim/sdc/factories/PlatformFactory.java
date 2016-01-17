package pt.ruim.sdc.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import pt.ruim.sdc.PhysicsFilters;
import pt.ruim.sdc.UserData;
import pt.ruim.sdc.components.BodyComp;
import pt.ruim.sdc.components.PlatformComp;
import pt.ruim.sdc.components.RectComp;
import pt.ruim.sdc.components.TextureComp;

/**
 * Created by ruimadeira on 02/01/16.
 */
public class PlatformFactory extends Factory{

    public PlatformFactory(World world, float worldScale){
        super(world, worldScale);
    }


    public Entity createPlatform(Rectangle rect, int index){
        UserData.Type type = UserData.Type.PLATFORM;
        short categoryBits = PhysicsFilters.PLATFORM_MASK;
        short maskBits = PhysicsFilters.PLAYER_MASK | PhysicsFilters.DUST_MASK | PhysicsFilters.MONSTER_MASK | PhysicsFilters.COMET_MASK;

        Entity e = createRectEntity(rect, categoryBits, maskBits, type);

        TextureComp tc = new TextureComp("platform.png");
        tc.width = rect.width;
        tc.height = rect.height;
        tc.transform.idt();
        float posX = rect.x+rect.width*0.5f;
        float posY = rect.y+rect.height*0.5f;
        tc.transform.translate(posX, posY, 0);
        e.add(tc);

        PlatformComp pc = new PlatformComp(index);
        e.add(pc);
        return e;

    }

    //creates a platform without a visible texture
    public Entity createInvisibleWall(Rectangle rect){

        UserData.Type type = UserData.Type.WALL;
        short categoryBits = PhysicsFilters.WALL_MASK;
        short maskBits = PhysicsFilters.PLAYER_MASK | PhysicsFilters.DUST_MASK | PhysicsFilters.MONSTER_MASK | PhysicsFilters.COMET_MASK;
        Entity e = createRectEntity(rect, categoryBits, maskBits, type);
        return e;
    }

    private Entity createRectEntity(Rectangle rect, short categoryBits, short maskBits, UserData.Type type){
        Entity e = new Entity();

        float centerX = rect.x + rect.width*0.5f;
        float centerY = rect.y + rect.height*0.5f;

        BodyDef bd = new BodyDef();
        bd.position.set(centerX*worldScale, centerY*worldScale);
        bd.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bd);
        body.setUserData(new UserData(e, type));

        PolygonShape ps = new PolygonShape();
        ps.setAsBox(rect.width*worldScale*0.5f, rect.height*worldScale*0.5f);
        Fixture fix = body.createFixture(ps, 1);
        Filter filter = fix.getFilterData();
        filter.categoryBits = categoryBits;
        filter.maskBits = maskBits;
        fix.setFilterData(filter);
        e.add(new BodyComp(body, worldScale));

        RectComp rc = new RectComp(rect);
        e.add(rc);

        return e;
    }

}
