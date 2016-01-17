package pt.ruim.sdc;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import pt.ruim.sdc.factories.MonsterFactory;
import pt.ruim.sdc.factories.PlatformFactory;
import pt.ruim.sdc.systems.PhysicsSys;

/**
 * Created by ruimadeira on 30/12/15.
 */
public class LevelBuilder {

    Array<Entity> platforms, monsters, walls;
    PlatformFactory platformFactory;
    MonsterFactory monsterFactory;


    public LevelBuilder(World world, float worldScale, Engine engine){

        platforms = new Array<Entity>();
        monsters = new Array<Entity>();
        walls = new Array<Entity>();

        platformFactory = new PlatformFactory(world, worldScale);
        monsterFactory = new MonsterFactory(world, worldScale);

        int numFloors = 4;
        float baseY = 0;
        float yAdd = (float)Gdx.graphics.getHeight() / (float)numFloors;
        for(int i=0; i<numFloors; i++){
            createFloor(world, i, baseY, worldScale);
            baseY += yAdd;
        }

        createMonsters();
        createWalls();

        addToEngine(engine);
    }

    private void createFloor(World world, int index, float baseY, float worldScale){
        float h = 25f;
        float windowW = Gdx.graphics.getWidth();
        Rectangle rect = new Rectangle();
        rect.y = baseY;
        rect.height = h;
        Entity p;
        switch(index){
            case 0:
                rect.x = 0f;
                rect.width = windowW;
                p = platformFactory.createPlatform(rect, index);
                platforms.add(p);
                break;
            case 1:
                rect.width = windowW * 0.75f;
                rect.x = (windowW - rect.width)*0.5f;
                p = platformFactory.createPlatform(rect, index);
                platforms.add(p);
                break;
            case 2:
                rect.width = windowW / 3f;
                rect.x = 0f;
                p = platformFactory.createPlatform(rect, index);
                platforms.add(p);
                rect.x = windowW - windowW/3f;
                p = platformFactory.createPlatform(rect, index);
                platforms.add(p);
                break;
            case 3:
                rect.width = windowW * 0.5f;
                rect.x = (windowW - rect.width)*0.5f;
                p = platformFactory.createPlatform(rect, index);
                platforms.add(p);
                break;
        }
    }

    private void addToEngine(Engine engine){
        for(int i=0; i<platforms.size; i++){
            engine.addEntity(platforms.get(i));
        }
        for(int i=0; i<monsters.size; i++){
            engine.addEntity(monsters.get(i));
        }
        for(int i=0; i<walls.size; i++){
            engine.addEntity(walls.get(i));
        }
    }

    private void createMonsters(){
        Entity top = platforms.peek();
        Entity platformMonster = monsterFactory.createPlatformMonster(top);
        monsters.add(platformMonster);

        for(int i=0; i<2; i++){
            Entity bottom = platforms.first();
            Entity m = monsterFactory.createPlatformMonster(bottom);
            monsters.add(m);
        }
    }

    private void createWalls(){
        float wallW = 25f;
        float wallH = Gdx.graphics.getHeight() * 2;
        Rectangle rect = new Rectangle(-wallW, 0, wallW, wallH);
        Entity leftWall = platformFactory.createInvisibleWall(rect);
        walls.add(leftWall);

        rect.x = Gdx.graphics.getWidth();
        Entity rightWall = platformFactory.createInvisibleWall(rect);
        walls.add(rightWall);
    }


}
