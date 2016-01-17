package pt.ruim.sdc;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by ruimadeira on 15/01/16.
 */
public class ColorUtils {

    public static Color fromRGB(int r, int g, int b){
        return fromRGBA(r, g, b, 255);
    }

    public static Color fromRGBA(int r, int g, int b, int a){
        float inv255 = 1f/255f;
        float _r = (float)r*inv255;
        float _g = (float)g*inv255;
        float _b = (float)b*inv255;
        float _a = (float)a*inv255;
        return new Color(_r, _g, _b, _a);
    }
}
