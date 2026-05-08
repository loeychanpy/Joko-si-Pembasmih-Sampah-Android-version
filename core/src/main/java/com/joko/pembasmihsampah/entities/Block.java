package com.joko.pembasmihsampah.entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.joko.pembasmihsampah.utils.Assets;

/**
 * Block Class (TEXTURED BLOCK)
 * Block dirender menggunakan asset texture "tanah.png"
 * Digunakan sebagai obstacle/rintangan atau dekoratif tiles
 *
 * Visual: Image texture (tanah.png)
 * Purpose: Textured collision surface / obstacle
 */
public class Block {

    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final Rectangle bounds;
    private final Texture blockTexture;
    private static final float COLLISION_TOLERANCE = 2f;

    public Block(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(x, y, width, height);

        // Load texture from asset
        this.blockTexture = Assets.get("tanah.png", Texture.class);
    }

    /**
     * Alternative constructor dengan custom texture
     */
    public Block(float x, float y, float width, float height, String texturePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(x, y, width, height);

        this.blockTexture = Assets.get(texturePath, Texture.class);
    }

    /**
     * Check collision dengan player
     * SAMA INTERFACE seperti Platform untuk consistency!
     */
    public CollisionResult checkCollision(Rectangle playerBounds, float playerVelocityY) {
        if (!playerBounds.overlaps(bounds)) {
            return new CollisionResult(false, 0, "none");
        }

        String direction = determineCollisionDirection(playerBounds, playerVelocityY);
        return new CollisionResult(true, this.y + this.height, direction);
    }

    /**
     * Determine arah collision
     */
    private String determineCollisionDirection(Rectangle playerBounds, float playerVelocityY) {
        float playerBottom = playerBounds.y + playerBounds.height;
        float blockTop = bounds.y + bounds.height;
        float blockBottom = bounds.y;

        // Dari atas (landing on block)
        if (playerVelocityY <= 0 && playerBottom >= blockTop - COLLISION_TOLERANCE) {
            return "top";
        }

        // Dari bawah (head bump on block)
        if (playerVelocityY > 0 && playerBounds.y <= blockBottom + COLLISION_TOLERANCE) {
            return "bottom";
        }

        // Side collision
        return "side";
    }

    /**
     * Resolve collision - untuk top collision
     */
    public float resolveTopCollision(Rectangle playerBounds) {
        return bounds.y + bounds.height;
    }

    /**
     * Resolve collision - untuk bottom collision
     */
    public float resolveBottomCollision(Rectangle playerBounds) {
        return bounds.y - playerBounds.height;
    }

    /**
     * Resolve side collision
     */
    public float resolveSideCollision(Rectangle playerBounds, String side) {
        if ("left".equals(side)) {
            return bounds.x - playerBounds.width;
        } else {
            return bounds.x + bounds.width;
        }
    }

    /**
     * Render block dengan texture
     */
    public void render(SpriteBatch batch) {
        if (blockTexture != null) {
            batch.draw(blockTexture, x, y, width, height);
        }
    }

    // ===== GETTERS =====
    public Rectangle getBounds() { return bounds; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }

    public void dispose() {
        // Texture managed by AssetManager, tidak perlu dispose
    }

    /**
     * Inner class untuk collision result
     * Sama seperti Platform.CollisionResult untuk consistency!
     */
    public static class CollisionResult {
        public boolean collided;
        public float landingY;
        public String direction;

        public CollisionResult(boolean collided, float landingY, String direction) {
            this.collided = collided;
            this.landingY = landingY;
            this.direction = direction;
        }
    }
}
