package pt.ruim.sdc.factories;

import com.badlogic.ashley.core.Entity;
import pt.ruim.sdc.components.GameCameraComp;

/**
 * Created by ruimadeira on 16/01/16.
 */
public class GameCameraFactory {
    public GameCameraFactory(){

    }

    public Entity create(){
        Entity e = new Entity();
        GameCameraComp gc = new GameCameraComp(1024, 768);
        e.add(gc);
        return e;
    }

}
