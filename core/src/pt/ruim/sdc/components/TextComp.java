package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Matrix4;

/**
 * Created by ruimadeira on 15/01/16.
 */
public class TextComp implements Component {
    public String text;
    public BitmapFont font;
    public Matrix4 transform;
    public Color color;

    public TextComp(String text, BitmapFont font){
        this.text = text;
        this.font = font;
        transform = new Matrix4();
        color = new Color(1, 1, 1, 1);
    }

    public TextComp(String text, BitmapFont font, Color color){
        this.text = text;
        this.font = font;
        transform = new Matrix4();
        this.color = color;
    }
}
