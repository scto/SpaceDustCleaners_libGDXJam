package pt.ruim.sdc;

import com.badlogic.gdx.ApplicationAdapter;
import pt.ruim.sdc.scenes.SceneManager;

public class SpaceDustCleanersGame extends ApplicationAdapter {

	SceneManager sceneManager;
	
	@Override
	public void create () {

        AssetLoader al = AssetLoader.getInstance();
        al.loadTexture("player.png");
        al.loadTexture("platform.png");
        al.loadTexture("dust.png");
        al.loadTexture("monsterBlue.png");
        al.loadTexture("dogMonster.png");
        al.loadTexture("comet.png");
        al.loadTexture("circle.png");
        al.loadTexture("halfExplosion.png");
        al.loadTexture("explosion.png");
        al.loadFontTTF("font-light.ttf", AssetLoader.FontSize.BIG);
        al.loadFontTTF("font-light.ttf", AssetLoader.FontSize.SMALL);
		al.loadMusic("menuMusic.mp3");
		al.loadMusic("gameMusic.mp3");

        sceneManager = new SceneManager();
		sceneManager.goToScene(SceneManager.SceneType.MENU);
	}

	@Override
	public void render () {
        sceneManager.tick();
	}


}
