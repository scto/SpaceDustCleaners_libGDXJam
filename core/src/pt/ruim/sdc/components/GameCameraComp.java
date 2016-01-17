package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by ruimadeira on 16/01/16.
 */
public class GameCameraComp implements Component {
    public OrthographicCamera camera;
    public boolean shaking;
    public float shakeAmount, shakeSub;
    public Vector3 center;
    public Vector3 offset;

    public GameCameraComp(float w, float h){
        camera = new OrthographicCamera(w, h);
        camera.position.x = w * 0.5f;
        camera.position.y = h * 0.5f;
        center = new Vector3(camera.position);
        offset = new Vector3();
        camera.update();
    }

}
