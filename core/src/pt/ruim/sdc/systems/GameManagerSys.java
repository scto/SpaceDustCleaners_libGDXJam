package pt.ruim.sdc.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import pt.ruim.sdc.UserData;
import pt.ruim.sdc.components.*;
import pt.ruim.sdc.factories.UIFactory;
import pt.ruim.sdc.scenes.SceneManager;
import pt.ruim.sdc.systems.monsters.DogMonsterSys;
import pt.ruim.sdc.systems.monsters.MonsterSys;

/**
 * Created by ruimadeira on 14/01/16.
 */
public class GameManagerSys extends EntitySystem implements EntityListener, ContactListener{


    Family dustFamily;
    DustSys dustSys;
    PhysicsSys physicsSys;
    PlayerSys playerSys;
    MonsterSys monsterSys;
    int totalDustCreated, dustCollected;
    float progress;

    UIFactory uiFactory;

    Family progressRectFamily, progressLabelFamily;
    Entity progressRect;
    Entity progressLabel;

    boolean haltAndKillPlayer, haltAndKillMonsters;
    float pausePhysicsCountdown;

    float gameDuration;

    boolean gameOver;

    public GameManagerSys(DustSys dustSys, PhysicsSys physicsSys, PlayerSys playerSys, MonsterSys monsterSys) {
        this.dustSys = dustSys;
        this.physicsSys = physicsSys;
        this.playerSys = playerSys;
        this.monsterSys = monsterSys;
        dustFamily = Family.all(DustComp.class).get();
        uiFactory = new UIFactory();

        progressRectFamily = Family.all(ProgressRectComp.class).get();
        progressLabelFamily = Family.all(ProgressLabelComp.class).get();
    }

    public void addedToEngine(Engine e){
        super.addedToEngine(e);
        e.addEntityListener(this);
        physicsSys.addListener(this);
    }

    public void removedFromEngine(Engine e){
        super.removedFromEngine(e);
        e.removeEntityListener(this);
        physicsSys.removeListener(this);
    }

    public void looseGame(){
        gameOver = true;
        pausePhysicsCountdown = 0.5f;
        physicsSys.paused = true;
        haltAndKillPlayer = true;
        Engine e = getEngine();
        DogMonsterSys dogMonsters = e.getSystem(DogMonsterSys.class);
        dogMonsters.disableSpawn();
        CometSys comets = e.getSystem(CometSys.class);
        comets.disableSpawn();

        Entity gameOver = uiFactory.createGameOverLabel();
        getEngine().addEntity(gameOver);

        Array<Entity> labels = uiFactory.createGameOverLabels((int)(progress*100f), dustCollected, totalDustCreated, gameDuration);
        for(int i=0; i<labels.size; i++){
            getEngine().addEntity(labels.get(i));
        }
    }

    public void winGame(){
        gameOver = true;
        pausePhysicsCountdown = 0.5f;
        physicsSys.paused = true;
        haltAndKillMonsters = true;
        Engine e = getEngine();
        DogMonsterSys dogMonsters = e.getSystem(DogMonsterSys.class);
        dogMonsters.disableSpawn();
        CometSys comets = e.getSystem(CometSys.class);
        comets.disableSpawn();

        Entity gameWin = uiFactory.createGameWinLabel();
        getEngine().addEntity(gameWin);

        Array<Entity> labels = uiFactory.createGameOverLabels((int)(progress*100f), dustCollected, totalDustCreated, gameDuration);
        for(int i=0; i<labels.size; i++){
            getEngine().addEntity(labels.get(i));
        }

        progress = 1f;
        ProgressRectComp pr = progressRect.getComponent(ProgressRectComp.class);
        pr.progress = 1f;
    }

    public void update(float deltaTime){
        if(totalDustCreated == 0){
            progress = 0;
        } else {
            if(gameOver == false) {
                gameDuration += deltaTime;

                progress = (float) dustCollected / (float) totalDustCreated;
                if (progressRect != null) {
                    ProgressRectComp pr = progressRect.getComponent(ProgressRectComp.class);
                    pr.progress += (progress - pr.progress) * pr.interp * deltaTime;
                    pr.fillBounds.width = pr.backBounds.width * pr.progress;
                }
                if (progressLabel != null) {
                    TextComp t = progressLabel.getComponent(TextComp.class);
                    int num = (int) (progress * 100f);
                    t.text = "" + num + "%";

                }
            }
        }
        if(haltAndKillPlayer){
            if(pausePhysicsCountdown > 0){
                pausePhysicsCountdown -= deltaTime;
                if(pausePhysicsCountdown <= 0){
                    pausePhysicsCountdown = 0;
                    physicsSys.paused = false;
                    playerSys.killPlayer();
                    haltAndKillPlayer = false;
                }
            }
        }
            if(pausePhysicsCountdown > 0){
                pausePhysicsCountdown -= deltaTime;
                if(pausePhysicsCountdown <= 0){
                    pausePhysicsCountdown = 0;
                    physicsSys.paused = false;
                    if(haltAndKillPlayer){
                        playerSys.killPlayer();
                        haltAndKillMonsters = false;
                    }
                    if(haltAndKillMonsters){
                        monsterSys.killMonsters();
                        haltAndKillMonsters = false;
                    }
                }
            }
        if(gameOver){
            if(Gdx.input.isKeyPressed(Input.Keys.P)){
                SceneManager.getSharedInstance().goToScene(SceneManager.SceneType.GAME);
            } else if(Gdx.input.isKeyPressed(Input.Keys.M)){
                SceneManager.getSharedInstance().goToScene(SceneManager.SceneType.MENU);
            }
        }
    }

    @Override
    public void entityAdded(Entity entity) {
        if(dustFamily.matches(entity)){
            totalDustCreated++;
        }
        if(progressRectFamily.matches(entity)){
            progressRect = entity;
        }
        if(progressLabelFamily.matches(entity)){
            progressLabel = entity;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if(dustFamily.matches(entity)){
            dustCollected++;
            if(dustCollected == totalDustCreated){
                winGame();
            }
        }
    }

    @Override
    public void beginContact(Contact contact) {
        if(gameOver){
            return;
        }
        Entity player = PhysicsSys.getEntityFromBodyData(UserData.Type.PLAYER, contact);
        if(player == null){
            return;
        }
        Entity monster = PhysicsSys.getEntityFromBodyData(UserData.Type.MONSTER, contact);
        if(monster != null){
            System.out.println("GameManagerSys - death by monster!!!!");
            looseGame();
            return;
        }

        Entity explosion = PhysicsSys.getEntityFromBodyData(UserData.Type.EXPLOSION, contact);
        if(explosion != null){
            System.out.println("GameManagerSys - death by explosion!!!!");
            looseGame();
            return;
        }

        Entity comet = PhysicsSys.getEntityFromBodyData(UserData.Type.COMET, contact);
        if(comet != null){
            CometComp c = comet.getComponent(CometComp.class);
            if(c.state == CometComp.State.FALLING){
                System.out.println("GameManagerSys - death by falling comet!!!!");
                looseGame();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
