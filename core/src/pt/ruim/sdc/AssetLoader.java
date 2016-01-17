package pt.ruim.sdc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ArrayMap;

/**
 * Created by ruimadeira on 30/12/15.
 */
public class AssetLoader {
    ArrayMap<String, Texture> textures;
    ArrayMap<FontSize, BitmapFont> fonts;
    ArrayMap<String, Music> musics;

    public enum FontSize{
        SMALL, BIG
    }


    private static AssetLoader instance = null;

    public static AssetLoader getInstance(){
        if(instance == null){
            instance = new AssetLoader();
        }
        return instance;
    }

    private AssetLoader(){
        textures = new ArrayMap<String, Texture>();
        fonts = new ArrayMap<FontSize, BitmapFont>();
        musics = new ArrayMap<String, Music>();
    }

    public Texture loadTexture(String file){
        Texture t = new Texture(Gdx.files.internal(file));
        textures.put(file, t);
        return t;
    }

    public Texture getTexture(String file){
        return textures.get(file);
    }

    public BitmapFont loadFontTTF(String file, FontSize size){
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(file));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = getFontSize(size);

        BitmapFont font = gen.generateFont(params);
        fonts.put(size, font);

        gen.dispose();
        return font;
    }

    public BitmapFont getFont(FontSize size){
        return fonts.get(size);
    }

    public Music loadMusic(String file){
        Music m = Gdx.audio.newMusic(Gdx.files.internal(file));
        musics.put(file, m);
        return m;
    }

    public Music getMusic(String file){
        return musics.get(file);
    }

    private int getFontSize(FontSize size){
        switch (size){
            case SMALL: return 20;
            case BIG: return 63;
        }
        return 63;
    }
}
