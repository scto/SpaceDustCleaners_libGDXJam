package pt.ruim.sdc.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import pt.ruim.sdc.CometSpawner;
import pt.ruim.sdc.UserData;
import pt.ruim.sdc.components.*;
import pt.ruim.sdc.factories.SpawnMarkFactory;

/**
 * Created by ruimadeira on 04/01/16.
 */
public class CometSys extends IteratingSystem implements ContactListener{

    private static Family family = Family.all(CometComp.class, RemoveComp.class).get();
    GameCameraSys gameCameraSys;
    private PhysicsSys physicsSys;

    CometSpawner spawner;
    SpawnMarkFactory markFactory;
    ExplosionSys explosionSys;
    Vector2 explosionPos;
    Vector2 tempImpulse;

    public CometSys(PhysicsSys physicsSys, ExplosionSys explosionSys, GameCameraSys gameCameraSys) {
        super(family);
        this.physicsSys = physicsSys;
        this.explosionSys = explosionSys;
        this.gameCameraSys = gameCameraSys;
        explosionPos = new Vector2();
        tempImpulse = new Vector2();
        markFactory = new SpawnMarkFactory();
    }

    public void disableSpawn(){
        spawner.stop();
    }

    public void addedToEngine(Engine e){
        super.addedToEngine(e);
        physicsSys.addListener(this);
        if(spawner == null){
            spawner = new CometSpawner(e, e.getSystem(PhysicsSys.class));
            spawner.setInterval(3, 6);
            spawner.setAmount(1, 3);
            spawner.start();
        }
    }

    public void removedFromEngine(Engine e){
        super.removedFromEngine(e);
        physicsSys.removeListener(this);
    }

    public void update(float delta){
       if(spawner.update(delta)){
            for(int i=0; i<spawner.lastCreated.size; i++){
                Entity e = spawner.lastCreated.get(i);
                Entity mark = markFactory.createForComet(e, spawner.delay*2);
                getEngine().addEntity(mark);
            }
       }
        super.update(delta);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CometComp c = entity.getComponent(CometComp.class);
        RemoveComp r = entity.getComponent(RemoveComp.class);
        if(r.remove){
            return;
        }
        if(c.state == CometComp.State.TRIGGERED){
            c.triggerCountdown -= deltaTime;
            if(c.triggerCountdown <= 0){
                BodyComp b = entity.getComponent(BodyComp.class);
                Vector2 pos = b.body.getPosition();
                explosionPos.set(pos);
                explosionPos.scl(b.invWorldScale);
                explosionPos.y -= c.radius;
                float radius = 150f;
                float dur = 0.2f;
                explosionSys.createExplosion(ExplosionComp.Type.HALF_EXPOSION, explosionPos, radius, dur);
                r.remove = true;
                gameCameraSys.shakeCamera(10f, 1f);
                //TODO spawn dust
            } else {
                BodyComp b = entity.getComponent(BodyComp.class);
                float step = 20f;
                tempImpulse.x = MathUtils.random(-step, step);
                tempImpulse.y = MathUtils.random(-step, step);
                b.body.applyLinearImpulse(tempImpulse, b.body.getWorldCenter(), true);
            }
        }
    }

    @Override
    public void beginContact(Contact contact) {
        Entity cometBody = PhysicsSys.getEntityFromBodyData(UserData.Type.COMET, contact);
        if(cometBody != null){
            CometComp c = cometBody.getComponent(CometComp.class);
            Entity player = PhysicsSys.getEntityFromBodyData(UserData.Type.PLAYER, contact);
            if(player != null){
                if(c.state == CometComp.State.FALLING){
                    System.out.println("CometSys - player hit by comet!!!");
                    return;
                }
            } else {
                ;if(c.state == CometComp.State.FALLING){
                    c.state = CometComp.State.TRIGGERED;
                    gameCameraSys.shakeCamera(10f, 0.5f);
                    //return;
                }
            }
        }
/*
        Entity cometSensor = PhysicsSys.getEntityFromFixtureData(UserData.Type.COMET_SENSOR, contact);
        if(cometSensor != null){
            Entity player = PhysicsSys.getEntityFromBodyData(UserData.Type.PLAYER, contact);
            if(player != null) {
                CometComp c = cometSensor.getComponent(CometComp.class);
                if (c.state == CometComp.State.IDLE) {
                    c.state = CometComp.State.TRIGGERED;
                    TextureComp t = cometSensor.getComponent(TextureComp.class);
                    t.color.set(1, 0, 0, 1);
                    System.out.println("CometSys - trigger comet");
                    return;
                }
            }
        }*/
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
}
