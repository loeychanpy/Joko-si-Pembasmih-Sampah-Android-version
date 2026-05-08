package com.joko.pembasmihsampah.entities;

import static com.joko.pembasmihsampah.utils.GameConstant.GRAVITY;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.joko.pembasmihsampah.utils.Assets;
import com.joko.pembasmihsampah.utils.GameConstant;

import java.util.List;

/**
 * Player Class - Fixed Version
 *
 * FIXES:
 * - Added missing resolveHorizontal() method
 * - Removed unused animationTimer variable
 * - Added getters for nearbyTrash and inputAnswer
 * - Improved collision detection
 * - Better input validation
 */
public class Player {

    private float x, y;
    private float velocityY = 0;
    private float velocityX = 0;
    private boolean onGround = true;
    private boolean facingRight = true;
    private boolean isJumping = false;

    // Animation
    private Animation<TextureRegion> walkAnimationRight;
    private Animation<TextureRegion> walkAnimationLeft;

    private TextureRegion idleFrameRight;
    private TextureRegion idleFrameLeft;
    private TextureRegion jumpFrameRight;
    private TextureRegion jumpFrameLeft;

    private float stateTime = 0f;
    private final float fallMultiplier = 1.8f;
    private final float jumpStrength = 450f;

    // Input
    private String inputAnswer = "";
    private Trash nearbyTrash = null;

    private final Rectangle bounds;

    // Collision constants
    private static final float HORIZONTAL_COLLISION_TOLERANCE = 1f;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, GameConstant.PLAYER_WIDTH, GameConstant.PLAYER_HEIGHT);
        loadTextures();
    }

    private void loadTextures() {
        TextureRegion[] walkFramesLeft = new TextureRegion[3];
        for (int i = 0; i < walkFramesLeft.length; i++) {
            Texture frameTexture = Assets.get("walk" + (i + 1) + ".png", Texture.class);
            walkFramesLeft[i] = new TextureRegion(frameTexture);
        }
        walkAnimationLeft = new Animation<>(GameConstant.ANIMATION_FRAME_DURATION, walkFramesLeft);
        walkAnimationLeft.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] walkFramesRight = new TextureRegion[walkFramesLeft.length];
        for (int j = 0; j < walkFramesRight.length; j++) {
            walkFramesRight[j] = new TextureRegion(walkFramesLeft[j]);
            walkFramesRight[j].flip(true, false);
        }
        walkAnimationRight = new Animation<>(GameConstant.ANIMATION_FRAME_DURATION, walkFramesRight);
        walkAnimationRight.setPlayMode(Animation.PlayMode.LOOP);

        idleFrameLeft = new TextureRegion(Assets.get("stand.png", Texture.class));
        idleFrameRight = new TextureRegion(idleFrameLeft);
        idleFrameRight.flip(true, false);

        jumpFrameLeft = new TextureRegion(Assets.get("jump.png", Texture.class));
        jumpFrameRight = new TextureRegion(jumpFrameLeft);
        jumpFrameRight.flip(true, false);
    }

    /**
     * Main update method - handles physics and collision
     */
    public void update(float delta, List<Platform> platforms, List<Block> blocks) {
        applyGravity(delta);

        // Vertical movement and collision
        y += velocityY * delta;
        updateBounds();
        resolveVertical(platforms, blocks);

        // Horizontal movement and collision
        x += velocityX * delta;
        updateBounds();
        resolveHorizontal(platforms, blocks);

        applyFriction(delta);
        checkBoundaries();

        stateTime += delta;
    }

    /**
     * Apply gravity with fall multiplier for better feel
     */
    private void applyGravity(float delta) {
        if (!onGround) {
            if (velocityY < 0)
                velocityY -= GRAVITY * fallMultiplier * delta; // Fast fall
            else
                velocityY -= GRAVITY * delta; // Normal rise
        }
    }

    /**
     * Resolve vertical collisions with platforms and blocks
     */
    private void resolveVertical(List<Platform> platforms, List<Block> blocks) {
        onGround = false;

        // Check platform collisions first
        for (Platform p : platforms) {
            Platform.CollisionResult r = p.checkCollision(bounds, velocityY);
            if (r.collided) {
                handleVerticalCollision(r, p);
                return;
            }
        }

        // Then check block collisions
        for (Block b : blocks) {
            Block.CollisionResult r = b.checkCollision(bounds, velocityY);
            if (r.collided) {
                handleVerticalCollision(r, b);
                return;
            }
        }
    }

    /**
     * NEW METHOD - Resolve horizontal collisions with platforms and blocks
     * Prevents player from walking through walls
     */
    private void resolveHorizontal(List<Platform> platforms, List<Block> blocks) {
        // Check platform collisions
        for (Platform p : platforms) {
            if (bounds.overlaps(p.getBounds())) {
                if (velocityX > 0) {
                    // Moving right - hit left side of platform
                    x = p.getBounds().x - GameConstant.PLAYER_WIDTH - HORIZONTAL_COLLISION_TOLERANCE;
                } else if (velocityX < 0) {
                    // Moving left - hit right side of platform
                    x = p.getBounds().x + p.getBounds().width + HORIZONTAL_COLLISION_TOLERANCE;
                }
                velocityX = 0;
                updateBounds();
                return;
            }
        }

        // Check block collisions
        for (Block b : blocks) {
            if (bounds.overlaps(b.getBounds())) {
                if (velocityX > 0) {
                    // Moving right - hit left side of block
                    x = b.getBounds().x - GameConstant.PLAYER_WIDTH - HORIZONTAL_COLLISION_TOLERANCE;
                } else if (velocityX < 0) {
                    // Moving left - hit right side of block
                    x = b.getBounds().x + b.getBounds().width + HORIZONTAL_COLLISION_TOLERANCE;
                }
                velocityX = 0;
                updateBounds();
                return;
            }
        }
    }

    /**
     * Handle vertical collision resolution
     */
    private void handleVerticalCollision(Object r, Object surface) {
        if (r instanceof Platform.CollisionResult) {
            Platform.CollisionResult res = (Platform.CollisionResult) r;
            Platform p = (Platform) surface;

            if (res.direction.equals("top")) {
                y = p.resolveTopCollision(bounds);
                velocityY = 0;
                onGround = true;
                isJumping = false;
            } else if (res.direction.equals("bottom")) {
                y = p.resolveBottomCollision(bounds);
                velocityY = 0;
            }

        } else if (r instanceof Block.CollisionResult) {
            Block.CollisionResult res = (Block.CollisionResult) r;
            Block b = (Block) surface;

            if (res.direction.equals("top")) {
                y = b.resolveTopCollision(bounds);
                velocityY = 0;
                onGround = true;
                isJumping = false;
            } else if (res.direction.equals("bottom")) {
                y = b.resolveBottomCollision(bounds);
                velocityY = 0;
            }
        }

        updateBounds();
    }

    /**
     * Apply friction when on ground
     */
    private void applyFriction(float delta) {
        if (onGround) {
            velocityX *= 0.82f;
            if (Math.abs(velocityX) < 5f) velocityX = 0;
        }
    }

    /**
     * Update collision bounds position
     */
    private void updateBounds() {
        bounds.setPosition(x, y);
    }

    /**
     * Check and enforce screen boundaries
     */
    private void checkBoundaries() {
        // Left boundary
        if (x < 0) {
            x = 0;
            velocityX = 0;
        }

        // Right boundary
        if (x > GameConstant.SCREEN_WIDTH - GameConstant.PLAYER_WIDTH) {
            x = GameConstant.SCREEN_WIDTH - GameConstant.PLAYER_WIDTH;
            velocityX = 0;
        }

        // Bottom boundary (floor)
        if (y < 0) {
            y = 0;
            velocityY = 0;
            onGround = true;
            isJumping = false;
        }

        // Top boundary (fall off screen)
        if (y > GameConstant.SCREEN_HEIGHT) {
            respawn();
        }

        updateBounds();
    }

    /**
     * Respawn player at starting position
     */
    private void respawn() {
        x = 100;
        y = 300;
        velocityX = 0;
        velocityY = 0;
        onGround = false;
        bounds.setPosition(x, y);
    }

    /**
     * Render player with appropriate animation
     */
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame;

        if (isJumping) {
            currentFrame = facingRight ? jumpFrameRight : jumpFrameLeft;
        } else if (Math.abs(velocityX) > 10f) {
            currentFrame = facingRight ?
                walkAnimationRight.getKeyFrame(stateTime, true) :
                walkAnimationLeft.getKeyFrame(stateTime, true);
        } else {
            currentFrame = facingRight ? idleFrameRight : idleFrameLeft;
        }

        batch.draw(currentFrame, x, y, GameConstant.PLAYER_WIDTH, GameConstant.PLAYER_HEIGHT);
    }

    // ===== CONTROL METHODS =====

    public void moveLeft() {
        velocityX = -GameConstant.MOVE_SPEED;
        facingRight = false;
    }

    public void moveRight() {
        velocityX = +GameConstant.MOVE_SPEED;
        facingRight = true;
    }

    public void jump() {
        if (onGround && !isJumping) {
            velocityY = jumpStrength;
            onGround = false;
            isJumping = true;
        }
    }

    public void stopMoving() {
        velocityX = 0;
    }

    public void resetVelocity() {
        velocityY = 0;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        bounds.setPosition(x, y);
    }

    // ===== TRASH COLLECTION / MATH INPUT METHODS =====

    /**
     * Calculate distance to trash object
     */
    public float distanceTo(Trash trash) {
        float dx = (x + GameConstant.PLAYER_WIDTH / 2) - trash.getX();
        float dy = (y + GameConstant.PLAYER_HEIGHT / 2) - trash.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public void setNearbyTrash(Trash trash) {
        this.nearbyTrash = trash;
    }

    /**
     * IMPROVED - Add digit with validation
     */
    public void inputDigit(String digit) {
        // Only allow digits 0-9
        if (inputAnswer.length() < 3 && digit.matches("\\d")) {
            inputAnswer += digit;
        }
    }

    public void backspaceInput() {
        if (inputAnswer.length() > 0) {
            inputAnswer = inputAnswer.substring(0, inputAnswer.length() - 1);
        }
    }

    /**
     * IMPROVED - Submit answer with better error handling
     */
    public boolean submitAnswer() {
        if (nearbyTrash == null || inputAnswer.isEmpty()) {
            inputAnswer = "";
            return false;
        }

        try {
            int userAnswer = Integer.parseInt(inputAnswer);
            int correctAnswer = nearbyTrash.getCorrectAnswer();
            inputAnswer = "";

            return userAnswer == correctAnswer;
        } catch (NumberFormatException e) {
            inputAnswer = "";
            return false;
        }
    }

    // ===== GETTERS =====

    public float getX() { return x; }
    public float getY() { return y; }
    public Rectangle getBounds() { return bounds; }
    public float getVelocityY() { return velocityY; }
    public float getVelocityX() { return velocityX; }
    public boolean isJumping() { return isJumping; }
    public boolean isOnGround() { return onGround; }

    // NEW GETTERS for UI and game logic
    public String getInputAnswer() { return inputAnswer; }
    public Trash getNearbyTrash() { return nearbyTrash; }

    // ===== SETTERS =====

    public void setOnGround(boolean value) {
        onGround = value;
    }

    public void dispose() {
        System.out.println("✓ Player disposed");
    }
}
