package pt.ruim.sdc.systems.monsters;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import pt.ruim.sdc.UserData;
import pt.ruim.sdc.components.BodyComp;
import pt.ruim.sdc.components.RectComp;
import pt.ruim.sdc.components.monsters.MonsterComp;
import pt.ruim.sdc.components.monsters.MonsterMovementComp;
import pt.ruim.sdc.components.monsters.PlatformMonsterComp;
import pt.ruim.sdc.systems.PhysicsSys;

/**
 * Created by ruimadeira on 02/01/16.
 */
public class PlatformMonsterSys extends IteratingSystem implements ContactListener{

    private static Family family = Family.all(PlatformMonsterComp.class, MonsterMovementComp.class, BodyComp.class).get();
    private PhysicsSys phisicsSys;


    public PlatformMonsterSys(PhysicsSys physicsSys) {
        super(family);
        this.phisicsSys = physicsSys;
    }

    public void addedToEngine(Engine e){
        super.addedToEngine(e);
        phisicsSys.addListener(this);
    }

    public void removedFromEngine(Engine e){
        super.removedFromEngine(e);
        phisicsSys.removeListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlatformMonsterComp pm = entity.getComponent(PlatformMonsterComp.class);
        MonsterMovementComp mm = entity.getComponent(MonsterMovementComp.class);
        BodyComp b = entity.getComponent(BodyComp.class);
        Vector2 pos = b.body.getPosition();
        pos.x *= b.invWorldScale;
        pos.y *= b.invWorldScale;
        switch (mm.moveType){
            case LEFT:
                if(pos.x <= pm.minX){
                    standMonster(pm, mm);
                }
                break;
            case STAND:
                if(pm.standCountdown > 0){
                    pm.standCountdown -= deltaTime;
                }
                if(pm.standCountdown <= 0){
                    pm.standCountdown = 0;
                    if(pos.x <= pm.minX){
                        mm.moveType = MonsterMovementComp.MoveType.RIGHT;
                    } else {
                        mm.moveType = MonsterMovementComp.MoveType.LEFT;
                    }
                }
                break;
            case RIGHT:
                if(pos.x >= pm.maxX){
                    standMonster(pm, mm);
                }
                break;
        }
    }

    private void standMonster(PlatformMonsterComp pm, MonsterMovementComp mm){
        pm.standCountdown = pm.standInterval;
        mm.moveType = MonsterMovementComp.MoveType.STAND;
    }

    @Override
    public void beginContact(Contact contact) {
        Entity monster = PhysicsSys.getEntityFromBodyData(UserData.Type.MONSTER, contact);
        if(monster != null) {
            MonsterComp m = monster.getComponent(MonsterComp.class);
            if (m.type == MonsterComp.Type.PLATFORM_MONSTER) {
                Entity wall = PhysicsSys.getEntityFromBodyData(UserData.Type.WALL, contact);
                if (wall != null) {
                    Vector2 wallPos = getPos(wall);
                    Vector2 monsterPos = getPos(monster);
                    PlatformMonsterComp pm = monster.getComponent(PlatformMonsterComp.class);
                    if(wallPos.x < monsterPos.x){
                        pm.minX = wallPos.x + 5f;
                    } else {
                        pm.maxX = wallPos.x - 5f;
                    }
                }
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

    private Vector2 getPos(Entity e){
        BodyComp b = e.getComponent(BodyComp.class);
        Vector2 pos = b.body.getPosition();
        pos.scl(b.invWorldScale);
        return pos;
    }
}
