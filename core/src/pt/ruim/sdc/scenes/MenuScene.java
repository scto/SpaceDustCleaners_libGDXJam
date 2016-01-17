package pt.ruim.sdc.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by ruimadeira on 16/01/16.
 */
public class MenuScene extends Scene {


    Texture menuImage;
    SpriteBatch spriteBatch;

    public MenuScene(){
        menuImage = new Texture(Gdx.files.internal("mainMenu.png"));
        spriteBatch = new SpriteBatch();
    }

    public void tick(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            SceneManager.getSharedInstance().goToScene(SceneManager.SceneType.GAME);
        }
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        spriteBatch.setColor(1, 1, 1, 1);
        spriteBatch.draw(menuImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();
    }

    public void dispose(){
        spriteBatch.dispose();
        menuImage.dispose();
    }

}
