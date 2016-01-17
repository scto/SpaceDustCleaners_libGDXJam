package pt.ruim.sdc;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * Created by ruimadeira on 03/01/16.
 */
public class EntitySpawner {
    float minSpawnTime, maxSpawnTime;
    float spawnCountdown;
    int minSpawnAmount, maxSpawnAmount;
    int spawnAmount;
    boolean fixedTime, fixedAmount;
    Engine engine;
    boolean enabled;
    public Array<Entity> lastCreated;

    public EntitySpawner(Engine engine){
        this.engine = engine;
        fixedTime = true;
        minSpawnTime = maxSpawnTime = 1;
        fixedAmount = true;
        minSpawnAmount = maxSpawnAmount = 1;
        enabled = true;
        lastCreated = new Array<Entity>();
    }

    public void setInterval(float s){
        minSpawnTime = maxSpawnTime = s;
        fixedTime = true;
    }

    public void setInterval(float min, float max){
        minSpawnTime = min;
        maxSpawnTime = max;
        fixedTime = false;
    }

    public void setAmount(int amount){
        minSpawnAmount = maxSpawnAmount = amount;
        fixedAmount = true;
    }

    public void setAmount(int min, int max){
        minSpawnAmount = min;
        maxSpawnAmount = max;
        fixedAmount = false;
    }

    public void start(){
        enabled = true;
        spawnCountdown = getNextInterval();
        spawnAmount = getNextAmount();
    }

    public void stop(){
        enabled = false;
    }

    public boolean update(float delta){
        if(enabled){
            boolean ret = false;
            spawnCountdown -= delta;
            lastCreated.clear();
            if(spawnCountdown <= 0){
                for(int i=0; i<spawnAmount; i++){
                    Entity e = spawn();
                    lastCreated.add(e);
                    engine.addEntity(e);
                    ret = true;
                }
                spawnCountdown = getNextInterval();
                spawnAmount = getNextAmount();
            }
            return ret;
        }
        return false;
    }

    private float getNextInterval(){
        if(fixedTime){
            return minSpawnTime;
        }
        return MathUtils.random(minSpawnTime, maxSpawnTime);
    }

    private int getNextAmount(){
        if(fixedAmount){
            return minSpawnAmount;
        }
        return MathUtils.random(minSpawnAmount, maxSpawnAmount);
    }

    protected Entity spawn(){
        return null;
    }

}
