package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import pt.ruim.sdc.AssetLoader;

/**
 * Created by ruimadeira on 28/12/15.
 */
public class TextureComp implements Component {
    public Texture texture;
    public Matrix4 transform;
    public Vector2 offset;
    public float width, height;
    public Color color;
    public boolean visible;

    public TextureComp(String file){
        this(AssetLoader.getInstance().getTexture(file));
    }

    public TextureComp(Texture t){
        texture = t;
        transform = new Matrix4();
        width = t.getWidth();
        height = t.getHeight();
        color = new Color(1, 1, 1, 1);
        offset = new Vector2();
        visible = true;
    }
}
