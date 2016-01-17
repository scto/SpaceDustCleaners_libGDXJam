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
    Array<Scene> toRemove;

    public static SceneManager getSharedInstance(){
        return sharedInstance;
    }

    public SceneManager(){
        sharedInstance = this;
        toRemove = new Array<Scene>();
    }

    public void tick(){
        if(toRemove.size > 0){
            for(int i=0; i<toRemove.size; i++){
                toRemove.get(i).dispose();
            }
            toRemove.clear();
        }
        if(currScene != null){
            currScene.tick();
        }
    }

    public void goToScene(SceneType type){
        Scene s;
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

        currScene = s;
    }


}
