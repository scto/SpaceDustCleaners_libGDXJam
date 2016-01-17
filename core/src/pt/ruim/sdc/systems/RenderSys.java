package pt.ruim.sdc.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import pt.ruim.sdc.components.*;
import pt.ruim.sdc.components.monsters.MonsterComp;

/**
 * Created by ruimadeira on 01/01/16.
 */
public class RenderSys extends EntitySystem implements Disposable, EntityListener{
    GameCameraSys gameCameraSys;
    SpriteBatch spriteBatch;
    Box2DDebugRenderer box2dDebugRenderer;

    Family playerFamily = Family.all(PlayerMovementComp.class, TextureComp.class).get();
    Family platformFamily = Family.all(PlatformComp.class, TextureComp.class).get();
    Family dustFamily = Family.all(DustComp.class, TextureComp.class).get();
    Family monsterFamily = Family.all(MonsterComp.class, TextureComp.class).get();
    Family cometFamily = Family.all(CometComp.class, TextureComp.class).get();
    Family explosionFamily = Family.all(ExplosionComp.class, TextureComp.class).get();
    Family textFamily = Family.all(TextComp.class).get();
    Family progressRectFamily = Family.all(ProgressRectComp.class).get();
    Family spawnMarkFamily = Family.all(SpawnMarkComp.class, TextureComp.class).get();

    ImmutableArray<Entity> players, platforms, dusts, monsters, comets, explosions, texts, spawnMarks;

    Camera uiCamera;

    Entity progressRect;

    GlyphLayout glyphLayout;

    public boolean debugRender = false;

    public RenderSys(GameCameraSys gameCameraSys){
        this.gameCameraSys = gameCameraSys;
        spriteBatch = new SpriteBatch();

        uiCamera = new OrthographicCamera(1024, 768);
        uiCamera.position.x = 1024/2;
        uiCamera.position.y = 768/2;
        uiCamera.update();

        glyphLayout = new GlyphLayout();

        box2dDebugRenderer = new Box2DDebugRenderer();
    }

    public void update(float deltaTime){
        super.update(deltaTime);
        Entity camera = gameCameraSys.getCamera();
        if(camera == null){
            return;
        }
        GameCameraComp gc = camera.getComponent(GameCameraComp.class);
        spriteBatch.setProjectionMatrix(gc.camera.combined);
        spriteBatch.begin();
        renderTextures(platforms);
        renderTextures(spawnMarks);
        renderTextures(dusts);
        renderTextures(monsters);
        renderTextures(players);
        renderTextures(comets);
        renderTextures(explosions);
        renderProgress(progressRect);
        spriteBatch.setProjectionMatrix(uiCamera.combined);
        renderTexts(texts);
        spriteBatch.end();

        if(debugRender) {
            PhysicsSys physicsSys = getEngine().getSystem(PhysicsSys.class);
            float worldScale = physicsSys.getWorldScale();
            float invWorldScale = 1.0f / worldScale;
            Matrix4 debugTrans = new Matrix4(gc.camera.combined);
            debugTrans.scale(invWorldScale, invWorldScale, 1);
            box2dDebugRenderer.render(physicsSys.getWorld(), debugTrans);
        }
    }

    public void addedToEngine(Engine engine){
        super.addedToEngine(engine);
        engine.addEntityListener(this);
    }

    public void removedFromEngine(Engine engine){
        super.removedFromEngine(engine);
        engine.removeEntityListener(this);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        box2dDebugRenderer.dispose();
    }

    private void renderTextures(ImmutableArray<Entity> entities){
        if(entities == null){
            return;
        }
        if(entities.size() == 0){
            return;
        }
        for(int i=0; i<entities.size(); i++){
            Entity e = entities.get(i);
            TextureComp t = e.getComponent(TextureComp.class);
            if(t.visible == false){
                continue;
            }
            float w = t.width;
            float h = t.height;
            spriteBatch.setTransformMatrix(t.transform);
            spriteBatch.setColor(t.color);
            spriteBatch.draw(t.texture, t.offset.x-w*0.5f, t.offset.y-h*0.5f, w, h);
        }
    }

    private void renderTexts(ImmutableArray<Entity> entities){
        if(entities == null){
            return;
        }
        if(entities.size() == 0){
            return;
        }
        for(int i=0; i<entities.size(); i++){
            Entity e = entities.get(i);
            TextComp t = e.getComponent(TextComp.class);
            glyphLayout.setText(t.font, t.text);
            spriteBatch.setTransformMatrix(t.transform);
            t.font.setColor(t.color);
            t.font.draw(spriteBatch, t.text, -glyphLayout.width*0.5f, glyphLayout.height*0.5f);
        }
    }

    private void renderProgress(Entity progressRect){
        if(progressRect == null){
            return;
        }
        ProgressRectComp pr = progressRect.getComponent(ProgressRectComp.class);
        if(pr == null){
            return;
        }
        spriteBatch.setTransformMatrix(pr.defaultTransform);
        spriteBatch.setColor(pr.backColor);
        drawRect(pr.texture, pr.backBounds);
        if(pr.fillBounds.width > 0) {
            spriteBatch.setColor(pr.fillColor);
            drawRect(pr.texture, pr.fillBounds);
        }
    }

    @Override
    public void entityAdded(Entity entity) {
        updateFamilies(entity);
    }

    @Override
    public void entityRemoved(Entity entity) {
        updateFamilies(entity);
    }

    private void updateFamilies(Entity entity){
        if(dustFamily.matches(entity)){
            dusts = getEngine().getEntitiesFor(dustFamily);
            return;
        }
        if(playerFamily.matches(entity)){
            players = getEngine().getEntitiesFor(playerFamily);
            return;
        }
        if(platformFamily.matches(entity)){
            platforms = getEngine().getEntitiesFor(platformFamily);
            return;
        }
        if(monsterFamily.matches(entity)){
            monsters = getEngine().getEntitiesFor(monsterFamily);
            return;
        }
        if (cometFamily.matches(entity)) {
            comets = getEngine().getEntitiesFor(cometFamily);
            return;
        }
        if(explosionFamily.matches(entity)){
            explosions = getEngine().getEntitiesFor(explosionFamily);
            return;
        }
        if(textFamily.matches(entity)){
            texts = getEngine().getEntitiesFor(textFamily);
        }
        if(progressRectFamily.matches(entity)){
            progressRect = entity;
        }
        if(spawnMarkFamily.matches(entity)){
            spawnMarks = getEngine().getEntitiesFor(spawnMarkFamily);
        }
    }

    private void drawRect(Texture tex, Rectangle r){
        spriteBatch.draw(tex, r.x, r.y, r.width, r.height);
    }
}
