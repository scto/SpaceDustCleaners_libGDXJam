package pt.ruim.sdc.components.monsters;

import com.badlogic.ashley.core.Component;

/**
 * Created by ruimadeira on 02/01/16.
 */
public class MonsterMovementComp implements Component{
    public enum MoveType{
        LEFT, STAND, RIGHT
    }

    public float speed, scaleX, prevVelX;

    public MoveType moveType;

    public MonsterMovementComp(float speed){
        this.speed = speed;
        this.moveType = MoveType.STAND;
        scaleX = 1;

    }
}
