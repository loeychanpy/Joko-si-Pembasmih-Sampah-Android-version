package com.joko.pembasmihsampah.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class ScreenManager
{
    private static ScreenManager instance;
    private final Game game;
    private Screen currentScreen;

    private ScreenManager(Game game) {
        this.game = game;
    }

    public static void initialize(Game game) {
        if (instance == null) {
            instance = new ScreenManager(game);
        }
    }

    public static ScreenManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ScreenManager not initialized!");
        }
        return instance;
    }

    /** Switch to a new screen safely **/
    public void setScreen(Screen newScreen) {
        if (currentScreen != null) {
            currentScreen.hide();
        }
        currentScreen = newScreen;
        game.setScreen(currentScreen);
    }

    /** Optional: go back to current screen (e.g., pause -> resume) **/
    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public void dispose() {
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }

}
