package pt.ruim.sdc.systems.monsters;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import pt.ruim.sdc.UserData;
import pt.ruim.sdc.components.BodyComp;
import pt.ruim.sdc.components.ExplosionComp;
import pt.ruim.sdc.components.RemoveComp;
import pt.ruim.sdc.components.monsters.MonsterComp;
import pt.ruim.sdc.components.monsters.MonsterMovementComp;
import pt.ruim.sdc.components.TextureComp;
import pt.ruim.sdc.factories.ExplosionFactory;
import pt.ruim.sdc.systems.GameCameraSys;
import pt.ruim.sdc.systems.PhysicsSys;

/**
 * Created by ruimadeira on 02/01/16.
 */
public class MonsterSys extends IteratingSystem implements ContactListener{

    private static Family family = Family.all(MonsterComp.class).get();
    private final GameCameraSys gameCameraSys;
    PhysicsSys physicsSys;
    ExplosionFactory explosionFactory;
    boolean killMonsters;

    public MonsterSys(PhysicsSys physicsSys, GameCameraSys gameCameraSys) {
        super(family);
        this.physicsSys = physicsSys;
        this.gameCameraSys = gameCameraSys;
        explosionFactory = new ExplosionFactory(physicsSys.getWorld(), physicsSys.getWorldScale());
    }

    public void killMonsters(){
        killMonsters = true;
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
        if(killMonsters){
            BodyComp b = entity.getComponent(BodyComp.class);
            Vector2 pos = b.body.getPosition();
            pos.scl(b.invWorldScale);

            RemoveComp r = entity.getComponent(RemoveComp.class);
            r.remove = true;

            Entity explosion = explosionFactory.create(ExplosionComp.Type.FULL_EXPLOSION, pos, 100, 0.2f);
            getEngine().addEntity(explosion);

            gameCameraSys.shakeCamera(10f, 1f);

            return;
        }
        BodyComp b = entity.getComponent(BodyComp.class);
        if(b.body.isActive() == false){
            return;
        }
        MonsterMovementComp mm = entity.getComponent(MonsterMovementComp.class);
        TextureComp t = entity.getComponent(TextureComp.class);
        Vector2 vel = b.body.getLinearVelocity();
        switch (mm.moveType){
            case LEFT:
                vel.x = -mm.speed * deltaTime;
                break;
            case STAND:
                vel.x = 0;
                break;
            case RIGHT:
                vel.x = mm.speed * deltaTime;
                break;
        }
        b.body.setLinearVelocity(vel);
        if(vel.x < 0 && mm.prevVelX >= 0){
            mm.scaleX = 1;
        } else if(vel.x > 0 && mm.prevVelX >= 0){
            mm.scaleX = -1;
        }
        mm.prevVelX = vel.x;
        Vector2 pos = b.body.getPosition();
        pos.x *= b.invWorldScale;
        pos.y *= b.invWorldScale;
        t.transform.idt();
        t.transform.translate(pos.x, pos.y, 0);
        t.transform.scale(mm.scaleX, 1, 1);
    }

    @Override
    public void beginContact(Contact contact) {

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

    private boolean containsFix(Fixture f, Contact c){
        return c.getFixtureA() == f || c.getFixtureB() == f;
    }
}
