package pt.ruim.sdc.components.monsters;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by ruimadeira on 02/01/16.
 */
public class DogMonsterComp implements Component{
    public Entity currPlatform;
    public boolean shouldExitStage;
    public boolean shouldShangeFilterMasks;
    public float standCountdown, standDuration;
    public MonsterMovementComp.MoveType prevMoveType;

    public DogMonsterComp(){
        standDuration = 0.2f;
        prevMoveType = MonsterMovementComp.MoveType.STAND;
    }
}
