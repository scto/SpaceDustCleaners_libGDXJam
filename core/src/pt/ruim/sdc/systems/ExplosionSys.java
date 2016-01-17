package pt.ruim.sdc.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import pt.ruim.sdc.UserData;
import pt.ruim.sdc.components.BodyComp;
import pt.ruim.sdc.components.ExplosionComp;
import pt.ruim.sdc.components.RemoveComp;
import pt.ruim.sdc.components.TextureComp;
import pt.ruim.sdc.factories.ExplosionFactory;

/**
 * Created by ruimadeira on 07/01/16.
 */
public class ExplosionSys extends IteratingSystem implements ContactListener{

    private static Family family = Family.all(ExplosionComp.class, RemoveComp.class, BodyComp.class).get();

    PhysicsSys physicsSys;
    ExplosionFactory factory;
    Vector2 tempDiff;

    public ExplosionSys(PhysicsSys physicsSys) {
        super(family);
        this.physicsSys = physicsSys;
        World world = physicsSys.getWorld();
        float worldScale = physicsSys.getWorldScale();
        factory = new ExplosionFactory(world, worldScale);
        tempDiff = new Vector2();
    }

    public void createExplosion(ExplosionComp.Type type, final Vector2 pos, float radius, float duration){
        Entity explosion = factory.create(type, pos, radius, duration);
        getEngine().addEntity(explosion);
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
        ExplosionComp e = entity.getComponent(ExplosionComp.class);
        if(e.countdown <= 0){
            RemoveComp r = entity.getComponent(RemoveComp.class);
            r.remove = true;
        } else {
            e.countdown -= deltaTime;
            BodyComp b = entity.getComponent(BodyComp.class);
            e.currRadius += e.radiusAdd * deltaTime;
            b.body.destroyFixture(e.currFixture);

            e.shape = factory.createExplosionShape(e.currRadius);
            e.currFixture = factory.createExplosionFixture(b.body, e.shape);

            TextureComp t = entity.getComponent(TextureComp.class);

            switch(e.type){
                case HALF_EXPOSION:
                    updateHalfExplosionTexture(b, e, t);
                    break;
                case FULL_EXPLOSION:
                    updateFullExplosionTexture(b, e, t);
                    break;
            }

            /*
            Vector2 explosionPos = b.body.getPosition();
            explosionPos.scl(b.invWorldScale);
            if(e.contactEntities.size > 0){
                for(int i=0; i<e.contactEntities.size; i++){
                    Entity c = e.contactEntities.get(i);
                    RemoveComp r = c.getComponent(RemoveComp.class);
                    if(r != null){
                        if(r.remove){
                            e.contactEntities.removeValue(c, true);
                            continue;
                        }
                    }
                    BodyComp body = c.getComponent(BodyComp.class);
                    //NOT WORKING
                    applyExplosionForce(explosionPos, e, body);
                }
            }*/
        }
    }

    @Override
    public void beginContact(Contact contact) {
        Entity explosion = PhysicsSys.getEntityFromBodyData(UserData.Type.EXPLOSION, contact);
        if(explosion != null){
            Entity player = PhysicsSys.getEntityFromBodyData(UserData.Type.PLAYER, contact);
            if(player != null){
                System.out.println("Player+Explosion collision! DEAD!!!");
                return;
            }

            Entity dust = PhysicsSys.getEntityFromBodyData(UserData.Type.DUST, contact);
            if(dust != null){
                ExplosionComp e = explosion.getComponent(ExplosionComp.class);
                e.contactEntities.add(dust);
            }

            Entity monster = PhysicsSys.getEntityFromBodyData(UserData.Type.MONSTER, contact);
            if(monster != null){
                ExplosionComp e = explosion.getComponent(ExplosionComp.class);
                e.contactEntities.add(monster);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Entity explosion = PhysicsSys.getEntityFromBodyData(UserData.Type.EXPLOSION, contact);
        if(explosion != null){
            Entity dust = PhysicsSys.getEntityFromBodyData(UserData.Type.DUST, contact);
            if(dust != null){
                ExplosionComp e = explosion.getComponent(ExplosionComp.class);
                e.contactEntities.removeValue(dust, true);
            }

            Entity monster = PhysicsSys.getEntityFromBodyData(UserData.Type.MONSTER, contact);
            if(monster != null){
                ExplosionComp e = explosion.getComponent(ExplosionComp.class);
                e.contactEntities.removeValue(monster, true);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    //NOT WORKING
    private void applyExplosionForce(Vector2 pos, ExplosionComp ec, BodyComp bc){
        float force = 20f;
        Vector2 bodyPos = bc.body.getPosition();
        tempDiff.set(bodyPos);
        tempDiff.sub(pos);
        tempDiff.nor();
        tempDiff.scl(force);
        bc.body.applyLinearImpulse(tempDiff, bc.body.getWorldCenter(), true);
    }

    private void updateFullExplosionTexture(BodyComp b, ExplosionComp e, TextureComp t){
        t.width = e.currRadius*2f;
        t.height = e.currRadius*2f;

        Vector2 pos = b.body.getPosition();
        pos.scl(b.invWorldScale);
        //pos.y += e.currRadius*0.5f;
        t.transform.idt();
        t.transform.translate(pos.x, pos.y, 0);
    }

    private void updateHalfExplosionTexture(BodyComp b, ExplosionComp e, TextureComp t){
        t.width = e.currRadius*2f;
        t.height = e.currRadius;

        Vector2 pos = b.body.getPosition();
        pos.scl(b.invWorldScale);
        pos.y += e.currRadius*0.5f;
        t.transform.idt();
        t.transform.translate(pos.x, pos.y, 0);
    }
}
