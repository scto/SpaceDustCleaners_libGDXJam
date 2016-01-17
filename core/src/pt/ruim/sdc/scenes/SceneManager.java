package pt.ruim.sdc.scenes;


import com.badlogic.gdx.utils.Array;

/**
 * Created by ruimadeira on 16/01/16.
 */
public class SceneManager {

    private static SceneManager sharedInstance = null;

    public enum SceneType{
        GAME, MENU
    }

    Scene currScene;
    SceneType nextScene;
    boolean hasNextScene;
    Array<Scene> toRemove, toAdd;

    public static SceneManager getSharedInstance(){
        return sharedInstance;
    }

    public SceneManager(){
        sharedInstance = this;
        toRemove = new Array<Scene>();
        toAdd = new Array<Scene>();
    }

    public void tick(){
        if(toRemove.size > 0){
            for(int i=0; i<toRemove.size; i++){
                toRemove.get(i).dispose();
            }
            toRemove.clear();
        }
        if(hasNextScene){
            currScene = allocScene(nextScene);
            hasNextScene = false;
        }
        if(currScene != null){
            currScene.tick();
        }
    }

    public void goToScene(SceneType type){
        /*Scene s;
        switch(type){
            case GAME:
                s = new GameScene();
                break;
            case MENU:
                s = new MenuScene();
                break;
            default: s = new GameScene();
        }

        if(currScene != null){
            toRemove.add(currScene);
        }

        currScene = s;*/
        nextScene = type;
        hasNextScene = true;
        if(currScene != null){
            if(currScene.getType() == type){
                currScene.willRestartScene();
            }
            toRemove.add(currScene);
        }
    }

    private Scene allocScene(SceneType type){
        Scene s = null;
        switch(type){
            case GAME:
                s = new GameScene();
                break;
            case MENU:
                s = new MenuScene();
                break;
        }
        return s;
    }


}
