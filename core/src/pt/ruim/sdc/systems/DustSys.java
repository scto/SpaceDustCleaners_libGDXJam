package pt.ruim.sdc.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import pt.ruim.sdc.UserData;
import pt.ruim.sdc.components.*;
import pt.ruim.sdc.factories.DustFactory;

/**
 * Created by ruimadeira on 30/12/15.
 */
public class DustSys extends IteratingSystem implements ContactListener{

    private static Family family = Family.all(DustComp.class).get();

    PhysicsSys physicsSys;
    DustFactory factory;

    public DustSys(PhysicsSys physicsSys) {
        super(family);
        this.physicsSys = physicsSys;
        factory = new DustFactory(physicsSys.getWorld(), physicsSys.getWorldScale());
    }

    public void spawnRandDust(int num){
        Array<RectComp> rects = getPlatformRects();

        Engine engine = getEngine();
        for(int i=0; i<num; i++){
            int index = MathUtils.random(0, rects.size-1);
            RectComp r = rects.get(index);
            Entity dust = createDustInPlatform(r);
            engine.addEntity(dust);
        }
    }

    public void addedToEngine(Engine engine){
        super.addedToEngine(engine);
        physicsSys.addListener(this);
    }

    public void removedFromEngine(Engine engine){
        super.removedFromEngine(engine);
        physicsSys.removeListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DustComp d = entity.getComponent(DustComp.class);
        d.counter += d.counterAdd * deltaTime;
        float alpha = Math.abs(MathUtils.sin(d.counter)) * 0.25f + 0.5f;
        TextureComp t = entity.getComponent(TextureComp.class);
        t.color.a = alpha;
        BodyComp b = entity.getComponent(BodyComp.class);
        if(b.body.getType() != BodyDef.BodyType.StaticBody){
            float step = 100f;
            float fx = MathUtils.random(-step, step);
            float fy = MathUtils.random(-step, step);
            b.body.applyForceToCenter(fx, fy, true);
        }
    }

    @Override
    public void beginContact(Contact contact) {
        //dust collision and make dust static when touching platform/other static dust
        /*if(areBothDust(contact)){
            UserData udA = (UserData)contact.getFixtureA().getBody().getUserData();
            Entity dustA = udA.parent;
            UserData udB = (UserData)contact.getFixtureB().getBody().getUserData();
            Entity dustB = udB.parent;

            DustComp dA = dm.get(dustA);
            DustComp dB = dm.get(dustB);
            if(dA.shouldBeStatic == false && dB.shouldBeStatic){
                dA.shouldBeStatic = true;
            } else if(dA.shouldBeStatic && dB.shouldBeStatic == false){
                dB.shouldBeStatic = true;
            }
        } else {
            Entity dust = PhysicsSys.getEntityFromBodyData(UserData.Type.DUST, contact);
            if (dust != null) {
                Entity player = PhysicsSys.getEntityFromBodyData(UserData.Type.PLAYER, contact);
                Entity platform = PhysicsSys.getEntityFromBodyData(UserData.Type.PLATFORM, contact);
                if (player != null) {
                    RemoveComp r = dust.getComponent(RemoveComp.class);
                    r.remove = true;
                } else if(platform != null){
                    DustComp d = dm.get(dust);
                    d.shouldBeStatic = true;
                }
            }
        }*/

        //collision with player (don't become static when touching platforms)
        Entity dust = PhysicsSys.getEntityFromBodyData(UserData.Type.DUST, contact);
        if (dust != null) {
            Entity player = PhysicsSys.getEntityFromBodyData(UserData.Type.PLAYER, contact);
            if (player != null) {
                RemoveComp r = dust.getComponent(RemoveComp.class);
                r.remove = true;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private boolean areBothDust(Contact c){
        Body bA = c.getFixtureA().getBody();
        Body bB = c.getFixtureB().getBody();
        Object dataA = bA.getUserData();
        Object dataB = bB.getUserData();
        if(dataA == null || dataB == null){
            return false;
        }
        if(dataA instanceof UserData && dataB instanceof UserData){
            UserData udA = (UserData)dataA;
            UserData udB = (UserData)dataB;
            return udA.type == UserData.Type.DUST && udB.type == UserData.Type.DUST;
        }
        return false;
    }

    private Array<RectComp> getPlatformRects(){
        Family platformFam = Family.all(PlatformComp.class).get();
        ImmutableArray<Entity> platformEntities = getEngine().getEntitiesFor(platformFam);
        Array<RectComp> rects = new Array<RectComp>();
        for(int i=0; i<platformEntities.size(); i++){
            Entity platform = platformEntities.get(i);
            RectComp r = platform.getComponent(RectComp.class);
            rects.add(r);
        }
        return rects;
    }

    private Entity createDustInPlatform(RectComp r){
        float x = r.rectangle.x +  MathUtils.random(r.rectangle.width);
        float y = r.rectangle.y + r.rectangle.height + MathUtils.random(0, 20f);
        return factory.create(x, y);
    }
}
