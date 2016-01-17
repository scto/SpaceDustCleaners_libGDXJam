package pt.ruim.sdc.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import pt.ruim.sdc.EntitySpawner;
import pt.ruim.sdc.PhysicsFilters;
import pt.ruim.sdc.UserData;
import pt.ruim.sdc.components.BodyComp;
import pt.ruim.sdc.components.ExplosionComp;
import pt.ruim.sdc.components.RemoveComp;
import pt.ruim.sdc.components.TextureComp;

/**
 * Created by ruimadeira on 04/01/16.
 */
public class ExplosionFactory extends Factory{

    Vector2 vertices[];
    Filter filter;

    public ExplosionFactory(World world, float worldScale){
        super(world, worldScale);
        int numVerts = 6;
        vertices = new Vector2[numVerts];
        for(int i=0; i<vertices.length; i++){
            vertices[i] = new Vector2();
        }

        filter = new Filter();
        filter.categoryBits = PhysicsFilters.EXPLOSION_MASK;
        //filter.maskBits = PhysicsFilters.PLAYER_MASK | PhysicsFilters.DUST_MASK | PhysicsFilters.MONSTER_MASK | PhysicsFilters.COMET_MASK;
        filter.maskBits = PhysicsFilters.PLAYER_MASK | PhysicsFilters.MONSTER_MASK | PhysicsFilters.COMET_MASK;
    }

    public Entity create(ExplosionComp.Type type, final Vector2 pos, float targetRadius, float duration){
        Entity e = new Entity();

        String texFile;
        if(type == ExplosionComp.Type.FULL_EXPLOSION){
            texFile = "explosion.png";
        } else {
            texFile = "halfExplosion.png";
        }

        TextureComp tc = new TextureComp(texFile);
        e.add(tc);

        e.add(new RemoveComp());

        BodyDef bd = new BodyDef();
        bd.position.x = pos.x * worldScale;
        bd.position.y = pos.y * worldScale;
        bd.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bd);
        body.setUserData(new UserData(e, UserData.Type.EXPLOSION));

        //CircleShape shape = new CircleShape();
        //shape.setRadius(targetRadius*worldScale);

        PolygonShape shape = createExplosionShape(10f);

        Fixture fix = createExplosionFixture(body, shape);

        BodyComp bc = new BodyComp(body, worldScale);
        bc.updateTextureTransform = false;
        e.add(bc);

        ExplosionComp ec = new ExplosionComp(type, shape, fix, targetRadius, duration);
        e.add(ec);

        return e;
    }

    public PolygonShape createExplosionShape(float radius){
        int numVerts = vertices.length;
        float angle = 0;
        float angleAdd = MathUtils.PI / (numVerts-1);
        for(int i=0; i<numVerts; i++){
            float x = MathUtils.cos(angle) * radius*worldScale;
            float y = MathUtils.sin(angle) * radius*worldScale;
            vertices[i].x = x;
            vertices[i].y = y;
            angle += angleAdd;
        }
        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        return shape;
    }

    public Fixture createExplosionFixture(Body body, PolygonShape shape){
        Fixture fix = body.createFixture(shape, 1);
        //fix.setSensor(true);
        fix.setFilterData(filter);
        return fix;
    }
}
