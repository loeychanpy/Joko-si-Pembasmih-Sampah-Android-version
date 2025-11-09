package com.joko.pembasmihsampah.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.joko.pembasmihsampah.Main;
import com.joko.pembasmihsampah.entities.Player;
import com.joko.pembasmihsampah.entities.Platform;
import com.joko.pembasmihsampah.entities.Trash;
import com.joko.pembasmihsampah.utils.GameConstant;

public class GameScreen implements Screen {
    private final Main game;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Player player;
    private Array<Platform> platforms;
    private Array<Trash> trashObjects;

    private int score = 0;
    private BitmapFont font;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstant.SCREEN_WIDTH, GameConstant.SCREEN_HEIGHT);

        font = new BitmapFont();

        // Initialize player
        player = new Player(300, 200);

        // Initialize platforms
        platforms = new Array<>();
        createLevels();

        // Initialize trash objects
        trashObjects = new Array<>();
        trashObjects.add(new Trash(300, 300));
        trashObjects.add(new Trash(700, 400));
        trashObjects.add(new Trash(500, 500));
    }

    private void createLevels() {
        // Ground
        platforms.add(new Platform(0, 30, GameConstant.SCREEN_WIDTH, 30));

        // Platforms
        platforms.add(new Platform(100, 150, 300, 30));
        platforms.add(new Platform(500, 250, 300, 30));
        platforms.add(new Platform(150, 400, 250, 30));
        platforms.add(new Platform(700, 350, 300, 30));
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.1f, 0.15f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update
        handleInput();
        player.update(delta);
        checkCollisions();
        checkTrashCollision();

        // Draw
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw platforms
        for (Platform platform : platforms) {
            platform.render(batch);
        }

        // Draw trash
        for (Trash trash : trashObjects) {
            trash.render(batch);
        }

        // Draw player
        player.render(batch);

        // Draw UI
        font.draw(batch, "Score: " + score, 20, GameConstant.SCREEN_HEIGHT - 20);

        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.jump();
        }

        // Number input for math answers
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) player.inputDigit("0");
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) player.inputDigit("1");
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) player.inputDigit("2");
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) player.inputDigit("3");
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) player.inputDigit("4");
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) player.inputDigit("5");
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) player.inputDigit("6");
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) player.inputDigit("7");
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) player.inputDigit("8");
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) player.inputDigit("9");

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            player.backspaceInput();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            boolean correct = player.submitAnswer();
            if (correct) {
                score++;
            }
        }
    }

    private void checkCollisions() {
        for (Platform platform : platforms) {
            if (player.checkCollision(platform)) {
                player.land(platform.getY() + platform.getHeight());
            }
        }
    }

    private void checkTrashCollision() {
        for (int i = trashObjects.size - 1; i >= 0; i--) {
            Trash trash = trashObjects.get(i);
            float distance = player.distanceTo(trash);

            if (distance < GameConstant.DETECTION_RANGE) {
                trash.showProblem(true);
                player.setNearbyTrash(trash);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, GameConstant.SCREEN_WIDTH, GameConstant.SCREEN_HEIGHT);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
