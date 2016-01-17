package pt.ruim.sdc.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import pt.ruim.sdc.ColorUtils;

/**
 * Created by ruimadeira on 15/01/16.
 */
public class ProgressRectComp implements Component {
    public Rectangle backBounds, fillBounds;
    public float progress;
    public Color backColor, fillColor;
    public Texture texture;
    public Matrix4 defaultTransform;
    public float interp;

    public ProgressRectComp(Rectangle bounds, Texture texture){
        this.backBounds = bounds;
        this.texture = texture;
        fillBounds = new Rectangle(bounds.x, bounds.y, 0, bounds.height);
        backColor = new Color(1, 1, 1, 0.3f);
        fillColor = ColorUtils.fromRGB(254, 244, 79);
        defaultTransform = new Matrix4();
        interp = 5f;
    }
}
