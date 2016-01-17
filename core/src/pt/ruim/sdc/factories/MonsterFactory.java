package pt.ruim.sdc.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import pt.ruim.sdc.AssetLoader;
import pt.ruim.sdc.PhysicsFilters;
import pt.ruim.sdc.UserData;
import pt.ruim.sdc.components.*;
import pt.ruim.sdc.components.monsters.DogMonsterComp;
import pt.ruim.sdc.components.monsters.MonsterComp;
import pt.ruim.sdc.components.monsters.MonsterMovementComp;
import pt.ruim.sdc.components.monsters.PlatformMonsterComp;

/**
 * Created by ruimadeira on 02/01/16.
 */
public class MonsterFactory extends Factory{

    Vector2 tempOffset;

    public MonsterFactory(World world, float worldScale){
        super(world, worldScale);
        tempOffset = new Vector2();
    }

    public Entity createPlatformMonster(Entity platform){
        Texture texture = AssetLoader.getInstance().getTexture("monsterBlue.png");

        RectComp platformRect = platform.getComponent(RectComp.class);
        float x = platformRect.rectangle.x + MathUtils.random(platformRect.rectangle.width);
        float y = platformRect.rectangle.y + platformRect.rectangle.height + texture.getHeight()*0.5f;
        MonsterComp.Type type = MonsterComp.Type.PLATFORM_MONSTER;
        tempOffset.set(1f, 0.6f);
        short mask = PhysicsFilters.PLAYER_MASK | PhysicsFilters.PLATFORM_MASK | PhysicsFilters.EXPLOSION_MASK;
        Entity e = createBasicMonster(type, x, y, 27, 62, tempOffset, mask, 0, texture);

        e.add(new MonsterMovementComp(1000f));
        float minX = Math.max(40f, platformRect.rectangle.x);
        float maxX = Math.min(Gdx.graphics.getWidth()-40f, minX + platformRect.rectangle.width);
        e.add(new PlatformMonsterComp(minX, maxX));
        return e;
    }

    public Entity createDogMonster(float posX, float delay){
        Texture texture = AssetLoader.getInstance().getTexture("dogMonster.png");
        float y = Gdx.graphics.getHeight() + texture.getWidth();
        MonsterComp.Type type = MonsterComp.Type.DOG_MONSTER;

        tempOffset.set(11f, 3f);
        short mask = PhysicsFilters.PLAYER_MASK | PhysicsFilters.PLATFORM_MASK | PhysicsFilters.EXPLOSION_MASK | PhysicsFilters.WALL_MASK;
        Entity e = createBasicMonster(type, posX, y, 46, 32, tempOffset, mask, delay, texture);

        e.add(new MonsterMovementComp(2000f));
        e.add(new DogMonsterComp());

        return e;
    }

    private Entity createBasicMonster(MonsterComp.Type type, float x, float y, float boxW, float boxH, Vector2 offset, short mask, float delay, Texture texture){
        Entity e = new Entity();

        //texture comp
        TextureComp t = new TextureComp(texture);
        //t.offset.set(11f, 3f);
        t.offset.set(offset);
        e.add(t);

        //create body
        BodyDef bd = new BodyDef();
        bd.position.x = x * worldScale;
        bd.position.y = y * worldScale;
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.fixedRotation = true;
        if(delay > 0){
            bd.active = false;
            DelayBodyComp bed = new DelayBodyComp(delay);
            e.add(bed);
            t.visible = false;
        }
        Body body = world.createBody(bd);
        //create basic shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(boxW*worldScale*0.5f, boxH*worldScale*0.5f);
        Fixture fix = body.createFixture(shape, 1);
        Filter filter = fix.getFilterData();
        //set categories and masks
        filter.categoryBits = PhysicsFilters.MONSTER_MASK;
        filter.maskBits = mask;
        fix.setFilterData(filter);

        //set user data
        body.setUserData(new UserData(e, UserData.Type.MONSTER));

        //set fixed mass (don't let box2d calculate)
        MassData md = body.getMassData();
        md.mass = 1;
        body.setMassData(md);

        //add body
        e.add(new BodyComp(body, worldScale));

        //add monster comp
        e.add(new MonsterComp(type));

        e.add(new RemoveComp());

        return e;
    }

}
