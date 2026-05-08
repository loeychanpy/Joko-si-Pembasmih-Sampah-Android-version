package com.joko.pembasmihsampah.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    private static AssetManager assetManager = new AssetManager();

    public static void init(){
        if(assetManager == null) {
            assetManager= new AssetManager();
        }
        loadAll();
        assetManager.finishLoading();

    }
    public static void loadAll(){
        assetManager.load("startbackground.png", Texture.class);
        assetManager.load("titlegame.png", Texture.class);
        assetManager.load("buttonstart.png", Texture.class);
        assetManager.load("hoverstart.png", Texture.class);
        assetManager.load("buttonoption.png", Texture.class);
        assetManager.load("hoveroption.png", Texture.class);
        assetManager.load("back_num.png", Texture.class);
        assetManager.load("0_num.png", Texture.class);
        assetManager.load("1_num.png", Texture.class);
        assetManager.load("2_num.png", Texture.class);
        assetManager.load("3_num.png", Texture.class);
        assetManager.load("4_num.png", Texture.class);
        assetManager.load("5_num.png", Texture.class);
        assetManager.load("6_num.png", Texture.class);
        assetManager.load("7_num.png", Texture.class);
        assetManager.load("8_num.png", Texture.class);
        assetManager.load("9_num.png", Texture.class);
        assetManager.load("ent_num.png", Texture.class);
        assetManager.load("remove_num.png", Texture.class);



        assetManager.load("background_lvl1.png", Texture.class);
        assetManager.load("left_arrow.png", Texture.class);
        assetManager.load("right_arrow.png", Texture.class);
        assetManager.load("jump_button.png", Texture.class);
        assetManager.load("bananas.png", Texture.class);
        assetManager.load("trash.png", Texture.class);
        assetManager.load("trash2.png", Texture.class);
        assetManager.load("heart.png", Texture.class);
        assetManager.load("spike.png", Texture.class);
        assetManager.load("tanah.png", Texture.class);
        assetManager.load("gate.png", Texture.class);
        assetManager.load("gate_open.png", Texture.class);


        // Player animation frames
        assetManager.load("walk1.png", Texture.class);
        assetManager.load("walk2.png", Texture.class);
        assetManager.load("walk3.png", Texture.class);
        assetManager.load("walk4.png", Texture.class);
        assetManager.load("stand.png", Texture.class);
        assetManager.load("jump.png", Texture.class);

        assetManager.finishLoading();

    }

    public static <T> T get(String filename, Class<T> type){
        return assetManager.get(filename, type);
    }


    public static void dispose() {
        if (assetManager != null) {
            assetManager.dispose();
            assetManager = null;
        }
    }

}
