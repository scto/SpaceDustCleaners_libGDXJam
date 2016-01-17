package pt.ruim.sdc.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import pt.ruim.sdc.ColorUtils;
import pt.ruim.sdc.components.BodyComp;
import pt.ruim.sdc.components.RemoveComp;
import pt.ruim.sdc.components.SpawnMarkComp;
import pt.ruim.sdc.components.TextureComp;
import pt.ruim.sdc.components.monsters.MonsterComp;

/**
 * Created by ruimadeira on 16/01/16.
 */
public class SpawnMarkFactory {

    Vector2 markPos;

    public SpawnMarkFactory(){
        markPos = new Vector2(0, Gdx.graphics.getHeight());
    }

    public Entity createForComet(Entity e, float delay){
        BodyComp b = e.getComponent(BodyComp.class);
        Vector2 pos = b.body.getPosition();
        pos.scl(b.invWorldScale);
        return createForComet(pos, delay);
    }

    public Entity createForMonster(Entity e, float delay){
        BodyComp b = e.getComponent(BodyComp.class);
        Vector2 pos = b.body.getPosition();
        pos.scl(b.invWorldScale);
        MonsterComp m = e.getComponent(MonsterComp.class);
        return createForMonster(pos, delay, m.type);
    }

    public Entity createForComet(Vector2 pos, float delay){
        Color color = ColorUtils.fromRGBA(254, 244, 79, 100);
        return create(pos, delay, color);
    }

    public Entity createForMonster(Vector2 pos, float delay, MonsterComp.Type type){
        Color color;
        switch(type){
            case PLATFORM_MONSTER:
                color = ColorUtils.fromRGBA(0, 174, 239, 100);
                break;
            case DOG_MONSTER:
                color = ColorUtils.fromRGBA(151, 179, 70, 100);
                break;
            default:color = ColorUtils.fromRGBA(0, 174, 239, 100);
        }
        return create(pos, delay, color);
    }

    private Entity create(Vector2 pos, float delay, Color color){
        markPos.x = pos.x;
        markPos.y = Gdx.graphics.getHeight();
        Entity e = new Entity();

        e.add(new SpawnMarkComp(delay));

        TextureComp t = new TextureComp("halfExplosion.png");
        t.transform.translate(markPos.x, markPos.y, 0);
        t.transform.scale(1, -1, 1);
        t.color.set(color);
        t.width *= 0.5f;
        t.width = 0;
        t.height = 0;
        e.add(t);

        e.add(new RemoveComp());

        SpawnMarkComp s = new SpawnMarkComp(delay);
        s.targetWidth = (float)t.texture.getWidth() * 0.5f;
        s.targetHeight = (float)t.texture.getHeight() * 0.5f;
        e.add(s);
        return e;
    }
}
