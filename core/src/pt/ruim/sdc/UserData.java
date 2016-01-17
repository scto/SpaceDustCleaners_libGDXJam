package pt.ruim.sdc;

import com.badlogic.ashley.core.Entity;

/**
 * Created by ruimadeira on 30/12/15.
 */
public class UserData {
    public enum Type{
        PLAYER, PLAYER_FOOT, PLATFORM, DUST, WALL, MONSTER, COMET, EXPLOSION, COMET_SENSOR, MONSTER_SENSOR
    }

    public Type type;
    public Entity parent;

    public UserData(Entity parent, Type type){
        this.parent = parent;
        this.type = type;
    }

}
