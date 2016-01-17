package pt.ruim.sdc.components.monsters;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Created by ruimadeira on 02/01/16.
 */
public class MonsterComp implements Component {
    public enum Type{
        PLATFORM_MONSTER, DOG_MONSTER
    }

    public Type type;

    public MonsterComp(Type type){
        this.type = type;
    }

}
