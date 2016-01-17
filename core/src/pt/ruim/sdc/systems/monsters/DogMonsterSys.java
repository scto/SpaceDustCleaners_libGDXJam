package pt.ruim.sdc.systems.monsters;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import pt.ruim.sdc.DogMonsterSpawner;
import pt.ruim.sdc.PhysicsFilters;
import pt.ruim.sdc.UserData;
import pt.ruim.sdc.components.BodyComp;
import pt.ruim.sdc.components.PlatformComp;
import pt.ruim.sdc.components.RemoveComp;
import pt.ruim.sdc.components.monsters.DogMonsterComp;
import pt.ruim.sdc.components.monsters.MonsterComp;
import pt.ruim.sdc.components.monsters.MonsterMovementComp;
import pt.ruim.sdc.factories.SpawnMarkFactory;
import pt.ruim.sdc.systems.PhysicsSys;

/**
 * Created by ruimadeira on 02/01/16.
 */
public class DogMonsterSys extends IteratingSystem implements ContactListener{

    private static Family family = Family.all(DogMonsterComp.class, MonsterMovementComp.class, BodyComp.class).get();

    PhysicsSys physicsSys;

    DogMonsterSpawner spawner;
    SpawnMarkFactory markFactory;


    public DogMonsterSys(PhysicsSys physicsSys) {
        super(family);
        this.physicsSys = physicsSys;
        markFactory = new SpawnMarkFactory();
    }

    public void disableSpawn(){
        spawner.stop();
    }

    public void addedToEngine(Engine engine){
        super.addedToEngine(engine);
        physicsSys.addListener(this);
        if(spawner == null){
            spawner = new DogMonsterSpawner(engine);
            spawner.setInterval(2, 4);
            spawner.setAmount(1, 2);
            spawner.start();
        }
    }

    public void removedFromEngine(Engine engine){
        super.removedFromEngine(engine);
        physicsSys.removeListener(this);
    }

    public void update(float deltaTime){
        if(spawner.update(deltaTime)){
            for(int i=0; i<spawner.lastCreated.size; i++) {
                Entity e = spawner.lastCreated.get(i);
                Entity spawnMark = markFactory.createForMonster(e, spawner.delay*2);
                System.out.println("DogMonsterSys - created mark");
                getEngine().addEntity(spawnMark);
            }

        }
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DogMonsterComp dm = entity.getComponent(DogMonsterComp.class);
        if(dm.shouldExitStage){
            BodyComp b = entity.getComponent(BodyComp.class);
            if(dm.shouldShangeFilterMasks) {
                short maskBits = PhysicsFilters.PLATFORM_MASK | PhysicsFilters.PLAYER_MASK;
                Array<Fixture> fixtures = b.body.getFixtureList();
                for (int i = 0; i < fixtures.size; i++) {
                    Fixture fix = fixtures.get(i);
                    Filter filter = fix.getFilterData();
                    filter.maskBits = maskBits;
                    fix.setFilterData(filter);
                }
                dm.shouldShangeFilterMasks = false;
            }
            Vector2 pos = b.body.getPosition();
            float x = pos.x * b.invWorldScale;
            float y = pos.y * b.invWorldScale;
            if(x < 0 || x > Gdx.graphics.getWidth()){
                RemoveComp r = entity.getComponent(RemoveComp.class);
                r.remove = true;
                return;
            }
        }
        if(dm.standCountdown > 0){
            dm.standCountdown -= deltaTime;
            if(dm.standCountdown <= 0){
                dm.standCountdown = 0;
                MonsterMovementComp mm = entity.getComponent(MonsterMovementComp.class);
                if(mm.moveType == MonsterMovementComp.MoveType.STAND){
                    if(dm.prevMoveType == MonsterMovementComp.MoveType.STAND) {
                        if (MathUtils.random(0f, 1f) < 0.5f) {
                            mm.moveType = MonsterMovementComp.MoveType.LEFT;
                        } else {
                            mm.moveType = MonsterMovementComp.MoveType.RIGHT;
                        }
                        dm.prevMoveType = mm.moveType;
                    } else {
                        mm.moveType = dm.prevMoveType;
                    }
                }
            }
        }

    }

    @Override
    public void beginContact(Contact contact) {
        Entity monster = PhysicsSys.getEntityFromBodyData(UserData.Type.MONSTER, contact);
        if(monster == null){
            return;
        }
        MonsterComp m = monster.getComponent(MonsterComp.class);
        if(m.type == MonsterComp.Type.DOG_MONSTER){
            Entity platform = PhysicsSys.getEntityFromBodyData(UserData.Type.PLATFORM, contact);
            Entity wall = PhysicsSys.getEntityFromBodyData(UserData.Type.WALL, contact);
            if(platform != null){
                DogMonsterComp dm = monster.getComponent(DogMonsterComp.class);
                MonsterMovementComp mm = monster.getComponent(MonsterMovementComp.class);
                dm.standCountdown = dm.standDuration;
                dm.prevMoveType = mm.moveType;
                mm.moveType = MonsterMovementComp.MoveType.STAND;
                PlatformComp p = platform.getComponent(PlatformComp.class);

                if(p.floorIndex == 0){
                    dm.shouldExitStage = true;
                    dm.shouldShangeFilterMasks = true;
                }
            } else if(wall != null){
                //System.out.println("DogMonsterSys - dog hit wall");
                BodyComp wallBody = wall.getComponent(BodyComp.class);
                Vector2 wallPos = wallBody.body.getPosition();
                wallPos.scl(wallBody.invWorldScale);
                MonsterMovementComp mm = monster.getComponent(MonsterMovementComp.class);
                DogMonsterComp dm = monster.getComponent(DogMonsterComp.class);
               /* if(mm.moveType == MonsterMovementComp.MoveType.LEFT){
                    mm.moveType = MonsterMovementComp.MoveType.RIGHT;
                } else {
                    mm.moveType = MonsterMovementComp.MoveType.LEFT;
                }*/
                mm.moveType = MonsterMovementComp.MoveType.STAND;
                dm.standCountdown = dm.standDuration;

                //is left wall
                if(wallPos.x < 0){
                    dm.prevMoveType = MonsterMovementComp.MoveType.RIGHT;
                } else {
                    dm.prevMoveType = MonsterMovementComp.MoveType.LEFT;
                }
                //dm.prevMoveType = mm.moveType;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Entity monster = PhysicsSys.getEntityFromBodyData(UserData.Type.MONSTER, contact);
        if(monster == null){
            return;
        }
        MonsterComp m = monster.getComponent(MonsterComp.class);
        if(m.type == MonsterComp.Type.DOG_MONSTER){
            Entity platform = PhysicsSys.getEntityFromBodyData(UserData.Type.PLATFORM, contact);
            if(platform != null){
                DogMonsterComp dm = monster.getComponent(DogMonsterComp.class);
                dm.currPlatform = null;
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
