package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by ruimadeira on 03/01/16.
 */
public class CometComp implements Component {
    public enum State{
        FALLING, IDLE, TRIGGERED
    }

    public float triggerDuration, triggerCountdown, radius;
    public State state;

    public CometComp(float triggerDuration, float radius){
        this.triggerDuration = triggerDuration;
        this.triggerCountdown = triggerDuration;
        this.radius = radius;
        state = State.FALLING;
    }
}
