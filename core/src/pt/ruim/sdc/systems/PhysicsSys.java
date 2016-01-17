package pt.ruim.sdc.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import pt.ruim.sdc.UserData;
import pt.ruim.sdc.components.BodyComp;
import pt.ruim.sdc.components.TextureComp;

/**
 * Created by ruimadeira on 30/12/15.
 */
public class PhysicsSys extends IteratingSystem implements Disposable, ContactListener{

    public static Entity getEntityFromFixtureData(UserData.Type type, Contact c){
        Fixture fA = c.getFixtureA();
        Entity ret = getEntityFromData(fA.getUserData(), type);
        if(ret != null){
            return ret;
        }
        Fixture fB = c.getFixtureB();
        ret = getEntityFromData(fB.getUserData(), type);
        if(ret != null){
            return ret;
        }
        return null;
    }

    public static Entity getEntityFromBodyData(UserData.Type type, Contact c){
        Body bA = c.getFixtureA().getBody();
        Entity ret = getEntityFromData(bA.getUserData(), type);
        if(ret != null){
            return ret;
        }
        Body bB = c.getFixtureB().getBody();
        ret = getEntityFromData(bB.getUserData(), type);
        if(ret != null){
            return ret;
        }
        return null;
    }

    private static Family family = Family.all(BodyComp.class, TextureComp.class).get();

    ComponentMapper<BodyComp> bm;
    ComponentMapper<TextureComp> tm;
    Array<ContactListener> listeners;

    World world;
    float worldScale;
    float timeStep;

    public boolean paused;

    public PhysicsSys(){
        super(family);
        world = new World(new Vector2(0, -500f), true);
        worldScale = 0.1f;
        timeStep = 1.0f/60.0f;

        bm = ComponentMapper.getFor(BodyComp.class);
        tm = ComponentMapper.getFor(TextureComp.class);

        listeners = new Array<ContactListener>();
        world.setContactListener(this);
    }

    public void update(float deltaTime){
        if(paused){
            return;
        }
        world.step(timeStep, 5, 5);
        super.update(deltaTime);
    }

    public void addListener(ContactListener l){
        listeners.add(l);
    }

    public boolean removeListener(ContactListener l){
        return listeners.removeValue(l, true);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComp b = bm.get(entity);
        if(b.body.isActive() && b.updateTextureTransform){
            Vector2 pos = b.body.getPosition();
            float x = pos.x * b.invWorldScale;
            float y = pos.y * b.invWorldScale;
            TextureComp t = tm.get(entity);
            t.transform.idt();
            t.transform.translate(x, y, 0);
            if(b.body.isFixedRotation() == false) {
                float rads = b.body.getAngle();
                t.transform.rotateRad(0, 0, 1, rads);
            }
        }
    }

    public World getWorld(){
        return world;
    }

    public float getWorldScale(){
        return worldScale;
    }

    @Override
    public void dispose() {
        world.dispose();
    }

    private static Entity getEntityFromData(Object data, UserData.Type type){
        if(data == null){
            return null;
        }
        if(data instanceof UserData){
            UserData userData = (UserData)data;
            if(userData.type == type){
                return userData.parent;
            }
        }
        return null;
    }

    @Override
    public void beginContact(Contact contact) {
        for(int i=0; i<listeners.size; i++){
            listeners.get(i).beginContact(contact);
        }
    }

    @Override
    public void endContact(Contact contact) {
        for(int i=0; i<listeners.size; i++){
            listeners.get(i).endContact(contact);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        for(int i=0; i<listeners.size; i++){
            listeners.get(i).preSolve(contact, oldManifold);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        for(int i=0; i<listeners.size; i++){
            listeners.get(i).postSolve(contact, impulse);
        }
    }
}
