package com.joko.pembasmihsampah.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.joko.pembasmihsampah.utils.GameConstant;

public class Player {
    private Sprite sprite;
    private float x, y;
    private float velocityY = 0;
    private boolean onGround = true;
    private boolean facingRight = true;

    // Animation
    private Texture[] walkFramesLeft;
    private Texture[] walkFramesRight;
    private Texture jumpTextureLeft;
    private Texture jumpTextureRight;
    private float animationTimer = 0;
    private int currentFrame = 0;
    private Texture currentTexture;

    // Input
    private String inputAnswer = "";
    private Trash nearbyTrash = null;

    private final Rectangle bounds;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, GameConstant.PLAYER_WIDTH, GameConstant.PLAYER_HEIGHT);

        loadTextures();
        //sprite = new Sprite(walkFramesLeft);
    }

    private void loadTextures() {
        // Load walk animations (3 frames)
        walkFramesLeft = new Texture[3];
        walkFramesRight = new Texture[3];
        for (int i = 0; i < 3; i++) {
            walkFramesLeft[i] = new Texture(Gdx.files.internal("images/walk" + (i + 1) + ".png"));
            walkFramesRight[i] = new Texture(Gdx.files.internal("images/walk" + (i + 1) + ".png"));
            // Note: For true horizontal flip, use TextureRegion.flip()
        }

        // Load jump textures
        jumpTextureLeft = new Texture(Gdx.files.internal("images/jump.png"));
        jumpTextureRight = new Texture(Gdx.files.internal("images/jump.png"));

        currentTexture = walkFramesLeft[currentFrame];
    }

    public void update(float delta) {
        // Apply gravity
        if (!onGround) {
            velocityY -= GameConstant.GRAVITY * delta;
        }

        // Update position
        y += velocityY * delta;

        // Update bounds
        bounds.setPosition(x, y);

        // Prevent falling off bottom
        if (y < 0) {
            y = 0;
            velocityY = 0;
            onGround = true;
        }

        // Update animation
        updateAnimation(delta);
    }

    private void updateAnimation(float delta) {
        animationTimer += delta;
        if (animationTimer >= GameConstant.ANIMATION_FRAME_DURATION) {
            animationTimer = 0;
            currentFrame = (currentFrame + 1) % 3;

            if (onGround) {
                currentTexture = facingRight ? walkFramesRight[currentFrame] : walkFramesLeft[currentFrame];
            } else {
                currentTexture = facingRight ? jumpTextureRight : jumpTextureLeft;
            }
        }
    }

    public void moveLeft() {
        x -= GameConstant.MOVE_SPEED * Gdx.graphics.getDeltaTime();
        facingRight = false;
    }

    public void moveRight() {
        x += GameConstant.MOVE_SPEED * Gdx.graphics.getDeltaTime();
        facingRight = true;
    }

    public void jump() {
        if (onGround) {
            velocityY = GameConstant.JUMP_VELOCITY;
            onGround = false;
        }
    }

    public boolean checkCollision(Platform platform) {
        Rectangle platformBounds = platform.getBounds();

        // Check if player is above platform and falling
        return bounds.overlaps(platformBounds) && velocityY <= 0;
    }

    public void land(float platformY) {
        y = platformY;
        velocityY = 0;
        onGround = true;
        animationTimer = 0;
        currentFrame = 0;
    }

    public float distanceTo(Trash trash) {
        float dx = (x + GameConstant.PLAYER_WIDTH / 2) - trash.getX();
        float dy = (y + GameConstant.PLAYER_HEIGHT / 2) - trash.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public void setNearbyTrash(Trash trash) {
        this.nearbyTrash = trash;
    }

    public void inputDigit(String digit) {
        if (inputAnswer.length() < 3) {
            inputAnswer += digit;
        }
    }

    public void backspaceInput() {
        if (inputAnswer.length() > 0) {
            inputAnswer = inputAnswer.substring(0, inputAnswer.length() - 1);
        }
    }

    public boolean submitAnswer() {
        if (nearbyTrash != null && !inputAnswer.isEmpty()) {
            int answer = nearbyTrash.getCorrectAnswer();
            if (Integer.parseInt(inputAnswer) == answer) {
                inputAnswer = "";
                return true;
            }
        }
        inputAnswer = "";
        return false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentTexture, x, y, GameConstant.PLAYER_WIDTH, GameConstant.PLAYER_HEIGHT);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public Rectangle getBounds() { return bounds; }
}
