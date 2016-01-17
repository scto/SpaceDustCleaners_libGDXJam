package pt.ruim.sdc.factories;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import pt.ruim.sdc.AssetLoader;
import pt.ruim.sdc.ColorUtils;
import pt.ruim.sdc.components.*;

/**
 * Created by ruimadeira on 15/01/16.
 */
public class UIFactory {

    Vector2 uiCenter;
    Vector2 tempPos;
    Color labelColor;

    public UIFactory(){
        uiCenter = new Vector2();
        uiCenter.x = Gdx.graphics.getWidth() / 2;
        uiCenter.y = Gdx.graphics.getHeight() * 0.95f;

        tempPos = new Vector2();
        labelColor = new Color(1, 1, 1, 1);
    }

    public Entity createProgressLabel(){
        Entity e = new Entity();

        BitmapFont font = AssetLoader.getInstance().getFont(AssetLoader.FontSize.SMALL);
        String text = "00/00";
        Color color = new Color(1, 1, 1, 1);
        TextComp t = new TextComp(text, font, color);
        t.transform.translate(uiCenter.x, uiCenter.y - 20f, 0f);
        e.add(t);

        e.add(new ProgressLabelComp());

        return e;
    }

    public Array<Entity> createGameOverLabels(int percent, int numCollected, int total, float duration){
        tempPos.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        Entity e1 = createLabel("" + percent + "% CLEANED", tempPos, labelColor);

        tempPos.y -= 25f;
        Entity e2 = createLabel("COLLECTED " + numCollected + " OF " + total, tempPos, labelColor);

        tempPos.y -= 25f;
        String timeStr = getTimeString(duration);
        Entity e3 = createLabel(timeStr, tempPos, labelColor);

        tempPos.y -= 70f;
        Entity e4 = createLabel("PRESS P TO PLAY  /  PRESS M FOR MENU", tempPos, labelColor);


        Array<Entity> ret = new Array<Entity>(2);
        ret.add(e1);
        ret.add(e2);
        ret.add(e3);
        ret.add(e4);
        return ret;
    }

    private Entity createLabel(String txt, Vector2 pos, Color color){
        Entity e = new Entity();

        BitmapFont font = AssetLoader.getInstance().getFont(AssetLoader.FontSize.SMALL);
        TextComp t = new TextComp(txt, font, color);
        t.transform.translate(pos.x, pos.y, 0);
        e.add(t);

        return e;
    }

    public Entity createProgressRect(){
        Entity e = new Entity();

        Rectangle bounds = new Rectangle();
        bounds.width = 325;
        bounds.height = 10;
        bounds.x = uiCenter.x - bounds.width * 0.5f;
        bounds.y = uiCenter.y;

        Texture tex = AssetLoader.getInstance().getTexture("platform.png");

        ProgressRectComp pr = new ProgressRectComp(bounds, tex);
        e.add(pr);

        return e;
    }

    public Entity createGameOverLabel(){
        String text = "GAME OVER";
        return createGameEndLabel(text);
    }

    public Entity createGameWinLabel(){
        String text = "ALL CLEAN";
        return createGameEndLabel(text);
    }

    private Entity createGameEndLabel(String text){
        Entity e = new Entity();

        BitmapFont font = AssetLoader.getInstance().getFont(AssetLoader.FontSize.BIG);

        Color color = ColorUtils.fromRGB(254, 244, 79);
        TextComp t = new TextComp(text, font, color);
        float x = (float)Gdx.graphics.getWidth() * 0.5f;
        float y = (float)Gdx.graphics.getHeight() * 0.65f;
        t.transform.translate(x, y, 0);

        e.add(t);

        return e;
    }

    private String getTimeString(float seconds){
        int secs = MathUtils.ceil(seconds) % 60;
        int mins = MathUtils.ceil(seconds) / 60;
        String secsStr = "" + secs;
        if(secsStr.length() == 1){
            secsStr = "0" + secsStr;
        }
        String minsStr = "" + mins;
        if(minsStr.length() == 1){
            minsStr = "0" + minsStr;
        }
        return minsStr + ":" + secsStr;
    }

}
