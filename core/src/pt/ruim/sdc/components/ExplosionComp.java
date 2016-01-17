package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

/**
 * Created by ruimadeira on 04/01/16.
 */
public class ExplosionComp implements Component{

    public enum Type{
        HALF_EXPOSION, FULL_EXPLOSION
    }

    public float currRadius, maxRadius, duration, countdown, radiusAdd;
    public PolygonShape shape;
    public Fixture currFixture;
    public Array<Entity> contactEntities;

    public Type type;

    public ExplosionComp(Type type, PolygonShape shape, Fixture currFixture, float maxRadius, float duration){
        this.type = type;
        this.shape = shape;
        this.currFixture = currFixture;
        this.maxRadius = maxRadius;
        this.duration = duration;
        currRadius = 0;
        countdown = duration;
        radiusAdd = maxRadius/duration;
        contactEntities = new Array<Entity>();
    }

}
