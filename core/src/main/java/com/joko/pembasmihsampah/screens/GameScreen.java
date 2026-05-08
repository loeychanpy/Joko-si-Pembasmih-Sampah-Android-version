package com.joko.pembasmihsampah.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.joko.pembasmihsampah.Main;
import com.joko.pembasmihsampah.entities.Block;
import com.joko.pembasmihsampah.entities.Gate;
import com.joko.pembasmihsampah.entities.Heart;
import com.joko.pembasmihsampah.entities.Player;
import com.joko.pembasmihsampah.entities.Platform;
import com.joko.pembasmihsampah.entities.Spike;
import com.joko.pembasmihsampah.entities.Trash;
import com.joko.pembasmihsampah.utils.Assets;
import com.joko.pembasmihsampah.utils.GameConstant;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.joko.pembasmihsampah.utils.LevelData;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    private final Main game;
    private OrthographicCamera camera;
    private OrthographicCamera uicamera;
    private SpriteBatch batch;

    private Player player;
    private List<Platform> platforms;
    private List<Block> blocks;
    private Array<Trash> trashObjects;
    private Array<Spike> spikes;
    private Array<Heart> hearts;
    private Gate gate;

    private int currentLevel = 1;
    private LevelData.Level levelData;

    // UI
    private BitmapFont font;
    private int score = 0;
    private float timer = 0f;
    private final int MAXTIMER = 180;
    private int lives = 3;
    private final int maxLives = 3;

    private boolean gameOver = false;
    private boolean levelWon = false;
    private boolean gameComplete = false;

    // Input jawaban (pakai keyboard HP)
    private String currentInput = "";
    private boolean showingInputDialog = false;
    private int trashCollected = 0;

    // Textures
    private Texture backgroundTexture;
    private Texture leftArrowTexture;
    private Texture rightArrowTexture;
    private Texture jumpButtonTexture;
    private Texture entButtonTexture;
    private Texture heartTexture;
    private Texture spikeTexture;
    private Texture gateLocked;
    private Texture gateUnlocked;

    // Tombol UI
    private Rectangle leftArrowRect;
    private Rectangle rightArrowRect;
    private Rectangle jumpButtonRect;
    private Rectangle entButtonRect;

    private final Vector3 touchPoint = new Vector3();

    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;
    private boolean isJumpPressed = false;

    private float levelTransitionTimer = 0f;
    private final float LEVEL_TRANSITION_DELAY = 2f;
    public GameScreen(Main game) {
        this(game, 1);
    }

    public GameScreen(Main game, int startlevel) {
        this.game = game;
        this.currentLevel = startlevel;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstant.SCREEN_WIDTH, GameConstant.SCREEN_HEIGHT);

        uicamera = new OrthographicCamera();
        uicamera.setToOrtho(false, GameConstant.SCREEN_WIDTH, GameConstant.SCREEN_HEIGHT);

        platforms = new ArrayList<>();
        trashObjects = new Array<>();
        spikes = new Array<>();
        hearts = new Array<>();
        blocks = new ArrayList<>();

        // Muat tekstur untuk tombol dari folder assets (sesuaikan nama file)
        backgroundTexture = Assets.get("background_lvl1.png", Texture.class);
        leftArrowTexture = Assets.get("left_arrow.png", Texture.class);
        rightArrowTexture = Assets.get("right_arrow.png", Texture.class);
        jumpButtonTexture = Assets.get("jump_button.png", Texture.class);
        entButtonTexture = Assets.get("ent_num.png", Texture.class); // <- tombol ENT
        heartTexture = Assets.get("heart.png", Texture.class);
        spikeTexture = Assets.get("spike.png", Texture.class);
        gateLocked = Assets.get("gate.png", Texture.class);
        gateUnlocked = Assets.get("gate_open.png", Texture.class);

        font = new BitmapFont();

        float buttonSize = 100f; // Ukuran tombol dalam piksel virtual
        float padding = 25f;     // Jarak dari tepi layar

        leftArrowRect = new Rectangle(padding, padding, buttonSize, buttonSize);
        rightArrowRect = new Rectangle(padding + buttonSize + padding, padding, buttonSize, buttonSize);
        jumpButtonRect = new Rectangle(GameConstant.SCREEN_WIDTH - buttonSize - padding, padding, buttonSize, buttonSize);

        // Tombol ENT di tengah bawah
        entButtonRect = new Rectangle(
            GameConstant.SCREEN_WIDTH / 2f - buttonSize / 2f,
            padding,
            buttonSize,
            buttonSize
        );

        levelData = LevelData.getLevel(currentLevel);
        createLevels();

        for (int i = 0; i < lives; i++) {
            hearts.add(new Heart(GameConstant.SCREEN_WIDTH - 200 - i * 50, GameConstant.SCREEN_HEIGHT - 50));
        }

        timer = 0f;
        levelTransitionTimer = 0f;
        trashCollected = 0;
        gameOver = false;
        levelWon = false;
        gameComplete = false;
        currentInput = "";
        showingInputDialog = false;
    }

    private void createLevels() {
        platforms.clear();
        trashObjects.clear();
        spikes.clear();
        blocks.clear();

        switch (currentLevel) {
            case 1:
                createLevel1();
                break;
            case 2:
                createLevel2();
                break;
            case 3:
                createLevel3();
                break;
            default:
                createLevel1();
                break;
        }
    }

    private void createLevel1() {
        platforms.clear();
        trashObjects.clear();
        spikes.clear();
        blocks.clear();

        int worldHeight = GameConstant.SCREEN_HEIGHT;
        int tileWidth = 50;
        platforms.add(new Platform(0, 0, GameConstant.SCREEN_WIDTH, 150));

        // Ground layer 3 (converted)
        for (int i = 5; i < GameConstant.SCREEN_WIDTH / tileWidth - 2; i++) {
            blocks.add(new Block(i * tileWidth + tileWidth / 2, 220, 50, 50));
        }

        // Individual blocks (converted)
        blocks.add(new Block(32, worldHeight - 197, 50, 50));     // block43
        blocks.add(new Block(99, worldHeight - 240, 50, 50));     // block44
        blocks.add(new Block(166, worldHeight - 282, 50, 50));    // block45
        blocks.add(new Block(234, worldHeight - 320, 50, 50));    // block46
        blocks.add(new Block(304, worldHeight - 360, 50, 50));    // block47
        blocks.add(new Block(600, worldHeight - 420, 50, 50));
        blocks.add(new Block(668, worldHeight - 352, 50, 50));    // block48
        blocks.add(new Block(776, worldHeight - 273, 50, 50));    // block49
        blocks.add(new Block(876, worldHeight - 206, 50, 50));    // block50
        blocks.add(new Block(938, worldHeight - 206, 50, 50));    // block51
        blocks.add(new Block(1000, worldHeight - 206, 50, 50));   // block52

        // Spikes
        spikes.add(new Spike(450, worldHeight - 485));
        spikes.add(new Spike(150, worldHeight - 600));

        // Trash
        trashObjects.add(new Trash(600, worldHeight - 395));
        trashObjects.add(new Trash(90, worldHeight - 620));
        trashObjects.add(new Trash(611, worldHeight - 485));
        trashObjects.add(new Trash(1007, worldHeight - 628));
        trashObjects.add(new Trash(1000, worldHeight - 184));

        // Player
        player = new Player(34, worldHeight - 166);

        // Gate
        gate = new Gate(782, worldHeight - 485, levelData.trashRequired);
    }

    private void createLevel2() {

        platforms.add(new Platform(0, 0, GameConstant.SCREEN_WIDTH, 150));

        // More platforms (harder)
        blocks.add(new Block(100, 150, 250, 30));
        blocks.add(new Block(380, 200, 250, 30));
        blocks.add(new Block(660, 250, 250, 30));
        blocks.add(new Block(200, 350, 200, 30));
        blocks.add(new Block(500, 400, 200, 30));
        blocks.add(new Block(800, 350, 200, 30));
        blocks.add(new Block(950, 250, 150, 30));

        player = new Player(levelData.playerStartX, levelData.playerStartY);

        // More trash
        trashObjects.add(new Trash(150, 100));
        trashObjects.add(new Trash(420, 130));
        trashObjects.add(new Trash(700, 180));
        trashObjects.add(new Trash(250, 300));
        trashObjects.add(new Trash(550, 350));
        trashObjects.add(new Trash(850, 300));

        // More spikes
        spikes.add(new Spike(350, 60));
        spikes.add(new Spike(630, 60));
        spikes.add(new Spike(900, 60));

        gate = new Gate(levelData.gateX, 150, levelData.trashRequired);
    }

    private void createLevel3() {
        platforms.add(new Platform(0, 0, GameConstant.SCREEN_WIDTH, 150));

        // Staircase pattern (harder)
        blocks.add(new Block(100, 150, 200, 30));
        blocks.add(new Block(320, 220, 200, 30));
        blocks.add(new Block(540, 290, 200, 30));
        blocks.add(new Block(760, 220, 200, 30));
        blocks.add(new Block(350, 400, 300, 30));
        blocks.add(new Block(800, 350, 200, 30));
        blocks.add(new Block(200, 350, 100, 30));

        player = new Player(levelData.playerStartX, levelData.playerStartY);

        // More trash scattered
        trashObjects.add(new Trash(150, 100));
        trashObjects.add(new Trash(350, 150));
        trashObjects.add(new Trash(580, 240));
        trashObjects.add(new Trash(800, 150));
        trashObjects.add(new Trash(450, 350));
        trashObjects.add(new Trash(250, 300));
        trashObjects.add(new Trash(850, 300));

        // More spikes
        spikes.add(new Spike(250, 60));
        spikes.add(new Spike(480, 60));
        spikes.add(new Spike(700, 60));
        spikes.add(new Spike(980, 60));

        gate = new Gate(levelData.gateX, 150, levelData.trashRequired);
    }

    @Override
    public void render(float delta) {
        handleGameStateShortcuts();

        if (!gameOver && !levelWon && !gameComplete) {
            update(delta);
        }

        // Clear screen
        Gdx.gl.glClearColor(0.1f, 0.15f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // World camera
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, GameConstant.SCREEN_WIDTH, GameConstant.SCREEN_HEIGHT);

        // Draw platforms & blocks
        for (Platform platform : platforms) {
            platform.render(batch);
        }
        for (Block block : blocks) {
            block.render(batch);
        }

        // Spikes
        for (Spike spike : spikes) {
            spike.render(batch, spikeTexture);
        }

        // Trash
        for (Trash trash : trashObjects) {
            if (!trash.isCollected()) {
                trash.render(batch, font);
            }
        }

        // Gate & player
        gate.render(batch, gateLocked, gateUnlocked);
        player.render(batch);

        // UI text (score, timer, dll) di world layer (opsional)
        drawUI();
        batch.end();

        // UI camera (tombol)
        batch.setProjectionMatrix(uicamera.combined);
        batch.begin();

        batch.draw(leftArrowTexture, leftArrowRect.x, leftArrowRect.y,
            leftArrowRect.width, leftArrowRect.height);
        batch.draw(rightArrowTexture, rightArrowRect.x, rightArrowRect.y,
            rightArrowRect.width, rightArrowRect.height);
        batch.draw(jumpButtonTexture, jumpButtonRect.x, jumpButtonRect.y,
            jumpButtonRect.width, jumpButtonRect.height);

        // Tombol ENT
        batch.draw(entButtonTexture, entButtonRect.x, entButtonRect.y,
            entButtonRect.width, entButtonRect.height);

        batch.end();
    }

    private void handleGameStateShortcuts() {
        if (gameOver || levelWon || gameComplete) {
            if (Gdx.input.justTouched()) {
                if (gameComplete) {
                    game.setScreen(new StartScreen(game));
                } else if (levelWon) {
                    nextLevel();
                } else {
                    dispose();
                    show();
                }
            }
        }
    }

    private void drawUI() {
        // Score
        font.setColor(Color.GREEN);
        font.getData().setScale(1.8f);
        font.draw(batch, "Score: " + score, 20, GameConstant.SCREEN_HEIGHT - 20);

        // Level
        font.setColor(Color.CYAN);
        font.getData().setScale(1.8f);
        font.draw(batch, "Level: " + currentLevel + "/5", 200, GameConstant.SCREEN_HEIGHT - 20);

        // Timer
        font.setColor(Color.RED);
        font.getData().setScale(1.5f);
        font.draw(batch, "Time: " + (int) timer + "s", 450, GameConstant.SCREEN_HEIGHT - 20);

        // Trash progress
        font.setColor(Color.YELLOW);
        font.getData().setScale(1.3f);
        font.draw(batch, "Trash: " + gate.getTrashCollected() + "/" + gate.getTrashRequired(),
            700, GameConstant.SCREEN_HEIGHT - 20);

        // Hearts
        for (int i = 0; i < hearts.size; i++) {
            hearts.get(i).render(batch, heartTexture);
        }

        // Tampilkan teks "Answer" berdasarkan currentInput
        if (player.getNearbyTrash() != null) {
            font.setColor(Color.WHITE);
            font.getData().setScale(2f);
            String display = currentInput.isEmpty() ? "_" : currentInput;
            font.draw(batch, "Answer: " + display,
                GameConstant.SCREEN_WIDTH / 2f - 100, 200);
        }

        if (gameOver) {
            font.setColor(new Color(1, 0, 0, 0.9f));
            font.getData().setScale(3f);
            font.draw(batch, "GAME OVER", GameConstant.SCREEN_WIDTH / 2f - 200,
                GameConstant.SCREEN_HEIGHT / 2f + 100);
            font.getData().setScale(1.5f);
            font.draw(batch, "Level: " + currentLevel,
                GameConstant.SCREEN_WIDTH / 2f - 100, GameConstant.SCREEN_HEIGHT / 2f);
            font.draw(batch, "Press SPACE to Restart",
                GameConstant.SCREEN_WIDTH / 2f - 150, GameConstant.SCREEN_HEIGHT / 2f - 50);
        }

        // In drawUI() method:
        if (levelWon) {
            font.setColor(Color.YELLOW);
            font.getData().setScale(3f);
            font.draw(batch, "LEVEL " + currentLevel + " COMPLETE!",
                GameConstant.SCREEN_WIDTH / 2f - 300, GameConstant.SCREEN_HEIGHT / 2f + 100);
            font.getData().setScale(1.8f);
            font.draw(batch, "Score: " + score + "  Time: " + (int) timer + "s",
                GameConstant.SCREEN_WIDTH / 2f - 200, GameConstant.SCREEN_HEIGHT / 2f);

            if (currentLevel < 5) {
                font.getData().setScale(1.5f);
                // ✅ Changed message:
                font.draw(batch, "Tap Screen for Level " + (currentLevel + 1),
                    GameConstant.SCREEN_WIDTH / 2f - 180, GameConstant.SCREEN_HEIGHT / 2f - 50);
            } else {
                font.getData().setScale(2f);
                font.setColor(Color.GOLD);
                font.draw(batch, "GAME COMPLETE!",
                    GameConstant.SCREEN_WIDTH / 2f - 180, GameConstant.SCREEN_HEIGHT / 2f - 100);
            }
        }

        if (gameComplete) {
            font.setColor(Color.GOLD);
            font.getData().setScale(3f);
            font.draw(batch, "ALL LEVELS COMPLETE!",
                GameConstant.SCREEN_WIDTH / 2f - 350, GameConstant.SCREEN_HEIGHT / 2f + 100);
            font.getData().setScale(2f);
            font.draw(batch, "Final Score: " + score,
                GameConstant.SCREEN_WIDTH / 2f - 180, GameConstant.SCREEN_HEIGHT / 2f);
            font.getData().setScale(1.5f);
            font.draw(batch, "Press SPACE to Return to Menu",
                GameConstant.SCREEN_WIDTH / 2f - 200, GameConstant.SCREEN_HEIGHT / 2f - 100);
        }
    }

    private void update(float delta) {
        timer += delta;

        if (levelWon) {
            levelTransitionTimer += delta;
            if (levelTransitionTimer >= LEVEL_TRANSITION_DELAY) {
                nextLevel();
                return;
            }
            return;
        }

        player.update(delta, platforms, blocks);
        handleInput();
        checkTrashCollision();

        if (timer >= MAXTIMER) {
            loseLife();
            timer = 0;
        }

        // Spike collision
        for (Spike spike : spikes) {
            if (player.getBounds().overlaps(spike.getBounds())) {
                loseLife();
                return;
            }
        }

        // Gate
        if (!gate.isLocked() && player.getBounds().overlaps(gate.getBounds())) {
            levelWon = true;
            levelTransitionTimer = 0f;
        }

        // Fall off screen
        if (player.getY() < -50) {
            loseLife();
        }
    }

    private void handleInput() {
        isLeftPressed = false;
        isRightPressed = false;
        isJumpPressed = false;

        // Touch input for movement buttons
        for (int i = 0; i < 3; i++) {
            if (Gdx.input.isTouched(i)) {
                touchPoint.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                uicamera.unproject(touchPoint);

                if (leftArrowRect.contains(touchPoint.x, touchPoint.y)) {
                    isLeftPressed = true;
                }
                if (rightArrowRect.contains(touchPoint.x, touchPoint.y)) {
                    isRightPressed = true;
                }
                if (jumpButtonRect.contains(touchPoint.x, touchPoint.y)) {
                    isJumpPressed = true;
                }
            }
        }

        // Movement
        if (isLeftPressed) {
            player.moveLeft();
        } else if (isRightPressed) {
            player.moveRight();
        } else {
            player.stopMoving();
        }

        if (isJumpPressed) {
            player.jump();
        }

        // Keyboard fisik (opsional)
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.jump();
        }

        // Tap sekali untuk tombol ENT -> buka keyboard HP
        // Tap sekali: ENT = buka numpad Android
        if (Gdx.input.justTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            uicamera.unproject(touchPoint);

            // Jika sentuh tombol ENT
            if (entButtonRect.contains(touchPoint.x, touchPoint.y)) {
                showSystemKeyboardForAnswer();   // <-- method baru di bawah
            }
        }

    }

    private void showSystemKeyboardForAnswer() {
        // Hanya kalau lagi dekat trash
        final Trash nearby = player.getNearbyTrash();
        if (nearby == null || nearby.isCollected()) return;
        if (showingInputDialog) return;

        showingInputDialog = true;
        currentInput = "";

        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {
                showingInputDialog = false;

                if (text == null) return;
                text = text.trim();
                if (text.isEmpty()) return;

                try {
                    int answer = Integer.parseInt(text);
                    processAnswer(nearby, answer);
                } catch (NumberFormatException e) {
                    currentInput = "";
                }
            }

            @Override
            public void canceled() {
                showingInputDialog = false;
            }
        }, "Masukkan jawaban", "", "");
    }

    private void processAnswer(Trash target, int answer) {
        currentInput = String.valueOf(answer);

        if (answer == target.getCorrectAnswer()) {
            target.collect();
            trashCollected++;
            score += 10 * levelData.difficulty;
            gate.addTrash();
            currentInput = "";
            // Lepaskan referensi nearby trash di player
            player.setNearbyTrash(null);

        } else {
            // Jawaban salah, kosongkan input
            currentInput = "";
        }
    }

    private void checkTrashCollision() {
        for (Trash trash : trashObjects) {
            float distance = player.distanceTo(trash);

            if (distance < GameConstant.DETECTION_RANGE && !trash.isCollected()) {
                trash.showProblem(true);
                player.setNearbyTrash(trash);
            } else {
                trash.showProblem(false);
            }
        }
    }

    private void loseLife() {
        lives--;
        if (hearts.size > 0) {
            hearts.removeIndex(hearts.size - 1);
        }

        if (lives <= 0) {
            gameOver = true;
        } else {
            player.setPosition(levelData.playerStartX, levelData.playerStartY);
            player.resetVelocity();
        }
    }

    private void nextLevel() {
        if (currentLevel < LevelData.getTotalLevels()) {
            currentLevel++;
            levelWon = false;
            gameOver = false;
            levelTransitionTimer = 0f;
            lives = maxLives;
            score = 0; // atau dipertahankan antar level
            timer = 0;
            dispose();
            show();
        } else {
            gameComplete = true;
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, GameConstant.SCREEN_WIDTH, GameConstant.SCREEN_HEIGHT);
        uicamera.setToOrtho(false, GameConstant.SCREEN_WIDTH, GameConstant.SCREEN_HEIGHT);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();

    }
}
