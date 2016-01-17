package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Created by ruimadeira on 28/12/15.
 */
public class PlayerMovementComp implements Component {
    public float moveSpeed;
    public float jumpImpulseAmount;
    public float damp;
    public float prevVelX;
    public float scaleX;
    public float jumpCountdown, maxJumpCountdown;
    public int numFloorContacts;
    public boolean remove;

    public PlayerMovementComp(){
        moveSpeed = 80f;
        jumpImpulseAmount = 17f;
        remove = false;
        damp = 8f;
        scaleX = 1;
        maxJumpCountdown = 0.2f;
    }

}
