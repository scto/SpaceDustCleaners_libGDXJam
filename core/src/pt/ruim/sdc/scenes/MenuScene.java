package pt.ruim.sdc.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pt.ruim.sdc.AssetLoader;

/**
 * Created by ruimadeira on 16/01/16.
 */
public class MenuScene extends Scene {


    Texture menuImage;
    SpriteBatch spriteBatch;
    Music music;

    public MenuScene(){
        super(SceneManager.SceneType.MENU);
        menuImage = new Texture(Gdx.files.internal("mainMenu.png"));
        spriteBatch = new SpriteBatch();

        music = AssetLoader.getInstance().getMusic("menuMusic.mp3");
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();

        disposables.add(menuImage);
        disposables.add(spriteBatch);
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
        super.dispose();
        music.stop();
        music.setPosition(0);
    }

}
