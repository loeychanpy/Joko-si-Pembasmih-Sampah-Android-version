package com.joko.pembasmihsampah.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.joko.pembasmihsampah.Main;
import com.joko.pembasmihsampah.utils.GameConstant;
public class StartScreen implements Screen {
    private final Main game;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture backgroundTexture;
    private Texture titleTexture;
    private Texture startButtonTexture;
    private Texture startButtonHoverTexture;
    private Texture optionButtonTexture;
    private Texture optionButtonHoverTexture;


    private Sprite startButton;
    private Sprite optionButton;
    private Sprite titleSprite;

    private BitmapFont font;

    private float titleScale = 1f;
    private boolean titleGrowing = true;
    private final float titleAnimationSpeed = 0.5f;

    private boolean startButtonHovered = false;
    private boolean optionButtonHovered = false;

    public StartScreen(Main game) {
        this.game = game;
    }
    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstant.SCREEN_WIDTH, GameConstant.SCREEN_HEIGHT);

        // Load textures
        backgroundTexture = new Texture(Gdx.files.internal("images/startbackground.png"));
        titleTexture = new Texture(Gdx.files.internal("images/titlegame.png"));
        startButtonTexture = new Texture(Gdx.files.internal("images/buttonstart.png"));
        startButtonHoverTexture = new Texture(Gdx.files.internal("images/hoverstart.png"));
        optionButtonTexture = new Texture(Gdx.files.internal("images/buttonoption.png"));
        optionButtonHoverTexture = new Texture(Gdx.files.internal("images/hoveroption.png"));


        // Create sprites
        titleSprite = new Sprite(titleTexture);
        titleSprite.setPosition(
            GameConstant.SCREEN_WIDTH / 2 - titleSprite.getWidth() / 2,
            GameConstant.SCREEN_HEIGHT - 300
        );
        startButton = new Sprite(startButtonTexture);
        startButton.setSize(250, 250 * startButton.getHeight() / startButton.getWidth());
        startButton.setPosition(
            GameConstant.SCREEN_WIDTH / 2 - startButton.getWidth() / 2,
            GameConstant.SCREEN_HEIGHT / 2 - 50
        );

        optionButton = new Sprite(optionButtonTexture);
        optionButton.setSize(250, 250 * optionButton.getHeight() / optionButton.getWidth());
        optionButton.setPosition(
            GameConstant.SCREEN_WIDTH / 2 - optionButton.getWidth() / 2,
            GameConstant.SCREEN_HEIGHT / 2 - 200
        );

        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
// Clear screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update
        updateTitle(delta);
        checkMouseHover();
        handleInput();

        // Draw
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw background
        batch.draw(backgroundTexture, 0, 0, GameConstant.SCREEN_WIDTH, GameConstant.SCREEN_HEIGHT);

        // Draw title with animation
        batch.draw(titleTexture,
            GameConstant.SCREEN_WIDTH / 2 - titleSprite.getWidth() * titleScale / 2,
            GameConstant.SCREEN_HEIGHT - 250,
            titleSprite.getWidth() * titleScale,
            titleSprite.getHeight() * titleScale
        );

        // Draw buttons
        startButton.draw(batch);
        optionButton.draw(batch);

        batch.end();
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
    }
    private void checkMouseHover() {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);

        startButtonHovered = startButton.getBoundingRectangle().contains(mousePos.x, mousePos.y);
        optionButtonHovered = optionButton.getBoundingRectangle().contains(mousePos.x, mousePos.y);
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            startGame();
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (startButtonHovered) {
                startGame();
            }
        }
    }

    private void startGame() {
        game.setScreen(new GameScreen(game));
        this.dispose();
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
        backgroundTexture.dispose();
        titleTexture.dispose();
        startButtonTexture.dispose();
        startButtonHoverTexture.dispose();
        optionButtonTexture.dispose();
        optionButtonHoverTexture.dispose();
        font.dispose();
    }
}
