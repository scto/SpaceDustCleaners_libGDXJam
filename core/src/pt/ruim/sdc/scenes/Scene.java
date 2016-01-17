package pt.ruim.sdc.scenes;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by ruimadeira on 16/01/16.
 */
public class Scene implements Disposable {


    Array<Disposable> disposables;
    boolean willRestart;
    SceneManager.SceneType type;

    public Scene(SceneManager.SceneType type){
        this.type = type;
        disposables = new Array<Disposable>();
    }

    SceneManager.SceneType getType(){
        return type;
    }

    public void tick(){

    }

    public void willRestartScene(){
        willRestart = true;
    }

    @Override
    public void dispose() {
        for(int i=0; i<disposables.size; i++){
            disposables.get(i).dispose();
        }
        disposables.clear();
    }
}
