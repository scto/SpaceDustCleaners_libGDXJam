package pt.ruim.sdc.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import pt.ruim.sdc.UserData;
import pt.ruim.sdc.components.*;
import pt.ruim.sdc.factories.ExplosionFactory;

/**
 * Created by ruimadeira on 28/12/15.
 */
public class PlayerSys extends IteratingSystem implements ContactListener{

    private static Family family = Family.all(PlayerMovementComp.class, BodyComp.class, TextureComp.class).get();
    GameCameraSys gameCameraSys;


    Vector2 tempImpulse;
    PhysicsSys physicsSys;
    ExplosionFactory explosionFactory;

    public PlayerSys(PhysicsSys physicsSys, GameCameraSys gameCameraSys) {
        super(family);
        this.physicsSys = physicsSys;
        this.gameCameraSys = gameCameraSys;
        tempImpulse = new Vector2();
        explosionFactory = new ExplosionFactory(physicsSys.getWorld(), physicsSys.getWorldScale());
    }

    public void addedToEngine(Engine engine){
        super.addedToEngine(engine);
        physicsSys.addListener(this);
    }

    public void removedFromEngine(Engine engine){
        super.removedFromEngine(engine);
        physicsSys.removeListener(this);
    }

    public void killPlayer(){
        ImmutableArray<Entity> players = getEntities();
        for(int i=0; i<players.size(); i++){
            Entity p = players.get(i);
            BodyComp b = p.getComponent(BodyComp.class);
            Vector2 pos = b.body.getPosition();
            pos.scl(b.invWorldScale);

            Entity explosion = explosionFactory.create(ExplosionComp.Type.FULL_EXPLOSION, pos, 100f, 0.2f);
            getEngine().addEntity(explosion);

            RemoveComp r = p.getComponent(RemoveComp.class);
            r.remove = true;

            gameCameraSys.shakeCamera(20f, 1f);
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerMovementComp p = entity.getComponent(PlayerMovementComp.class);

        if(p.remove){
            getEngine().removeEntity(entity);
            return;
        }

        BodyComp b = entity.getComponent(BodyComp.class);

        //jump logic
        if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)){
            if(p.numFloorContacts > 0){
                p.jumpCountdown = p.maxJumpCountdown;
            }
        } else {
            if(p.jumpCountdown > 0){
                p.jumpCountdown = 0;
            }
        }

        //left/right logic
        float targetVelX = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)){
            targetVelX = -p.moveSpeed;
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)){
            targetVelX = p.moveSpeed;
        }

        float currVelX = b.body.getLinearVelocity().x;
        float impulseX = (targetVelX-currVelX) * p.damp * b.body.getMass() * deltaTime;
        tempImpulse.set(impulseX, 0);
        b.body.applyLinearImpulse(tempImpulse, b.body.getWorldCenter(), true);

        if(p.jumpCountdown > 0){
            p.jumpCountdown -= deltaTime;
            tempImpulse.set(0, p.jumpImpulseAmount);
            b.body.applyLinearImpulse(tempImpulse, b.body.getWorldCenter(), true);
        }


        Vector2 pos = b.body.getPosition();
        TextureComp t = entity.getComponent(TextureComp.class);

        float posX = pos.x*b.invWorldScale;
        float posY = pos.y*b.invWorldScale;
        t.transform.idt();
        t.transform.translate(posX, posY, 0);

        Vector2 vel = b.body.getLinearVelocity();
        if(vel.x < 0 && p.prevVelX >= 0){
            p.scaleX = -1;
        } else if(vel.x > 0 && p.prevVelX <= 0){
            p.scaleX = 1;
        }
        p.prevVelX = vel.x;

        t.transform.scale(p.scaleX, 1, 1);
    }

    @Override
    public void beginContact(Contact contact) {
        Entity player = PhysicsSys.getEntityFromFixtureData(UserData.Type.PLAYER_FOOT, contact);
        Entity platform = PhysicsSys.getEntityFromBodyData(UserData.Type.PLATFORM, contact);
        if(player != null && platform != null){
            PlayerMovementComp playerMovementComp = player.getComponent(PlayerMovementComp.class);
            playerMovementComp.numFloorContacts++;
            return;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Entity player = PhysicsSys.getEntityFromFixtureData(UserData.Type.PLAYER_FOOT, contact);
        Entity platform = PhysicsSys.getEntityFromBodyData(UserData.Type.PLATFORM, contact);
        if(player != null && platform != null){
            PlayerMovementComp playerMovementComp = player.getComponent(PlayerMovementComp.class);
            playerMovementComp.numFloorContacts--;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
