package com.joko.pembasmihsampah;

import com.badlogic.gdx.Game;
import com.joko.pembasmihsampah.entities.Platform;
import com.joko.pembasmihsampah.screens.ScreenManager;
import com.joko.pembasmihsampah.screens.StartScreen;
import com.joko.pembasmihsampah.utils.Assets;

public class Main extends Game {

    @Override
    public void create() {
        //setScreen(new StartScreen(this));
        Assets.init();
        ScreenManager.initialize(this);
        ScreenManager.getInstance().setScreen(new StartScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        //super.dispose();
        ScreenManager.getInstance().dispose();
        //Platform.dispose();
        Assets.dispose();

    }
}
