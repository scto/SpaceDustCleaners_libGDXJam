package pt.ruim.sdc.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import pt.ruim.sdc.components.GameCameraComp;

/**
 * Created by ruimadeira on 16/01/16.
 */
public class GameCameraSys extends EntitySystem implements EntityListener {

    Family family = Family.all(GameCameraComp.class).get();
    Entity camera;


    public GameCameraSys() {

    }

    public void update(float deltaTime){
        super.update(deltaTime);
        if(camera != null){
            GameCameraComp gc = camera.getComponent(GameCameraComp.class);
            if(gc.shaking){
                gc.shakeAmount -= gc.shakeSub * deltaTime;
                if(gc.shakeAmount <= 0){
                    gc.shakeAmount = 0;
                    gc.shaking = false;
                }
                gc.offset.x = MathUtils.random(-gc.shakeAmount, gc.shakeAmount);
                gc.offset.y = MathUtils.random(-gc.shakeAmount, gc.shakeAmount);
                gc.camera.position.set(gc.center);
                gc.camera.position.add(gc.offset);
                gc.camera.update();

            }
        }
    }

    public Entity getCamera(){
        return camera;
    }

    public void shakeCamera(float amount, float duration){
        if(camera != null){
            GameCameraComp gc = camera.getComponent(GameCameraComp.class);
            gc.shaking = true;
            if(amount > gc.shakeAmount){
                gc.shakeAmount = amount;
            }
            gc.shakeSub = (1f/duration) * amount;
        }
    }

    public void addedToEngine(Engine e){
        super.addedToEngine(e);
        e.addEntityListener(this);
    }

    public void removedFromEngine(Engine e){
        super.removedFromEngine(e);
        e.removeEntityListener(this);
    }

    @Override
    public void entityAdded(Entity entity) {
        if(family.matches(entity)){
            camera = entity;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {

    }
}
