package com.joko.pembasmihsampah.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.joko.pembasmihsampah.Main;
import com.joko.pembasmihsampah.utils.Assets;
import com.joko.pembasmihsampah.utils.GameConstant;
public class StartScreen implements Screen {
    private final Main game;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Sprite titleSprite, startButton, optionButton;
    private Texture startTex, startHover, optTex, optHover;
    private float titleScale = 1f;
    private boolean titleGrowing = true;
    private final float titleAnimationSpeed = 0.5f;
    private Texture background;
    private Texture title;


    public StartScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstant.SCREEN_WIDTH, GameConstant.SCREEN_HEIGHT);

        // Use assets from manager
        this.background = Assets.get("startbackground.png", Texture.class);
        this.title = Assets.get("titlegame.png", Texture.class);
        this.startTex = Assets.get("buttonstart.png", Texture.class);
        this.startHover = Assets.get("hoverstart.png", Texture.class);
        this.optTex = Assets.get("buttonoption.png", Texture.class);
        this.optHover = Assets.get("hoveroption.png", Texture.class);

        titleSprite = new Sprite(title);
        titleSprite.setPosition(GameConstant.SCREEN_WIDTH / 2f - titleSprite.getWidth() / 2f, GameConstant.SCREEN_HEIGHT - 400);

        startButton = new Sprite(startTex);
        startButton.setSize(250, 250 * startButton.getHeight() / startButton.getWidth());
        startButton.setPosition(GameConstant.SCREEN_WIDTH / 2f - startButton.getWidth() / 2f, GameConstant.SCREEN_HEIGHT / 2f - 100);

        optionButton = new Sprite(optTex);
        optionButton.setSize(250, 250 * optionButton.getHeight() / optionButton.getWidth());
        optionButton.setPosition(GameConstant.SCREEN_WIDTH / 2f - optionButton.getWidth() / 2f, GameConstant.SCREEN_HEIGHT / 2f - 200);
    }


    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update
        updateTitle(delta);
//        handleInput();

        Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touch);


        // Draw
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw background
        batch.draw(this.background,0,0,GameConstant.SCREEN_WIDTH,GameConstant.SCREEN_HEIGHT);
        titleSprite.draw(batch);


        if (startButton.getBoundingRectangle().contains(touch.x, touch.y) && Gdx.input.isTouched())
            startButton.setTexture(startHover);
        else
            startButton.setTexture(startTex);
        startButton.draw(batch);

        if (optionButton.getBoundingRectangle().contains(touch.x, touch.y) && Gdx.input.isTouched())
            optionButton.setTexture(optHover);
        else
            optionButton.setTexture(optTex);
        optionButton.draw(batch);


        batch.end();
        if (Gdx.input.justTouched() && startButton.getBoundingRectangle().contains(touch.x, touch.y)) {
            ScreenManager.getInstance().setScreen(new GameScreen(game));
        }

    }


    private void updateTitle(float delta) {
        if (titleGrowing) {
            titleScale += titleAnimationSpeed * delta;
            if (titleScale >= 1.2f) {
                titleGrowing = false;
            }
        } else {
            titleScale -= titleAnimationSpeed * delta;
            if (titleScale <= 1f) {
                titleGrowing = true;
            }
        }
        titleSprite.setScale(titleScale);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, GameConstant.SCREEN_WIDTH, GameConstant.SCREEN_HEIGHT);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
