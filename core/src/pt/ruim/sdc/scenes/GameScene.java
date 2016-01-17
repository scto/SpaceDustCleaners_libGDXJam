package pt.ruim.sdc.scenes;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.World;
import pt.ruim.sdc.AssetLoader;
import pt.ruim.sdc.ColorUtils;
import pt.ruim.sdc.LevelBuilder;
import pt.ruim.sdc.factories.GameCameraFactory;
import pt.ruim.sdc.factories.PlayerFactory;
import pt.ruim.sdc.factories.UIFactory;
import pt.ruim.sdc.systems.*;
import pt.ruim.sdc.systems.monsters.DogMonsterSys;
import pt.ruim.sdc.systems.monsters.MonsterSys;
import pt.ruim.sdc.systems.monsters.PlatformMonsterSys;

/**
 * Created by ruimadeira on 16/01/16.
 */
public class GameScene extends Scene {


    Engine engine;
    Color backgroundColor;
    Music music;

    public GameScene(){
        super(SceneManager.SceneType.GAME);
        engine = new Engine();
        createSystems();
        createEntities();
        backgroundColor = ColorUtils.fromRGB(20, 54, 56);
        music = AssetLoader.getInstance().getMusic("gameMusic.mp3");
        music.setLooping(true);
        if(music.isPlaying() == false) {
            music.play();
        }
    }

    public void tick(){
        clearBg(backgroundColor);
        engine.update(Gdx.graphics.getDeltaTime());
    }


    @Override
    public void dispose() {
        super.dispose();
        if(willRestart == false) {
            music.stop();
            music.setPosition(0);
        }
    }

    private void createSystems(){
        PhysicsSys physicsSys = new PhysicsSys();
        engine.addSystem(physicsSys);

        GameCameraSys gc = new GameCameraSys();
        engine.addSystem(gc);

        engine.addSystem(new RemoveSys(physicsSys));

        PlayerSys playerSys = new PlayerSys(physicsSys, gc);
        engine.addSystem(playerSys);

        engine.addSystem(new BodyEnableDelaySys());

        MonsterSys monsterSys = new MonsterSys(physicsSys, gc);
        engine.addSystem(monsterSys);
        engine.addSystem(new PlatformMonsterSys(physicsSys));
        engine.addSystem(new DogMonsterSys(physicsSys));

        DustSys dustSys = new DustSys(physicsSys);
        engine.addSystem(dustSys);
        ExplosionSys explosionSys = new ExplosionSys(physicsSys);
        engine.addSystem(explosionSys);
        engine.addSystem(new CometSys(physicsSys, explosionSys, gc));
        engine.addSystem(new SpawnMarkSys());

        RenderSys renderSys = new RenderSys(gc);
        engine.addSystem(renderSys);

        engine.addSystem(new GameManagerSys(dustSys, physicsSys, playerSys, monsterSys));

        disposables.add(physicsSys);
        disposables.add(renderSys);
    }

    private void createEntities(){

        PhysicsSys physicsSys = engine.getSystem(PhysicsSys.class);
        World world = physicsSys.getWorld();
        float worldScale = physicsSys.getWorldScale();
        PlayerFactory pf = new PlayerFactory(physicsSys.getWorld(), physicsSys.getWorldScale());
        Entity player = pf.create();
        engine.addEntity(player);

        new LevelBuilder(world, worldScale, engine);

        DustSys dustSys = engine.getSystem(DustSys.class);
        dustSys.spawnRandDust(400);

        UIFactory uiFactory = new UIFactory();

        Entity progressRect = uiFactory.createProgressRect();
        engine.addEntity(progressRect);

        Entity progressLabel = uiFactory.createProgressLabel();
        engine.addEntity(progressLabel);

        GameCameraFactory camFactory = new GameCameraFactory();
        Entity gameCam = camFactory.create();
        engine.addEntity(gameCam);
    }

    private void clearBg(Color c){
        Gdx.gl.glClearColor(c.r, c.g, c.b, c.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
