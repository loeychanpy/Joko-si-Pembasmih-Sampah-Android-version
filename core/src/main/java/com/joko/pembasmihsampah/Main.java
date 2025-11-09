package com.joko.pembasmihsampah;

import com.badlogic.gdx.Game;

import com.joko.pembasmihsampah.screens.StartScreen;


public class Main extends Game {



    @Override
    public void create() {
        setScreen(new StartScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
