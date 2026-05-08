package com.joko.pembasmihsampah.entities;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Gate {
    private float x;
    private float y;
    private final float width = 80f;
    private final float height = 120f;
    private final Rectangle bounds;
    private boolean isLocked = true;
    private final int trashRequired; // Number of trash to collect before gate opens
    private int trashCollected = 0;

    public Gate(float x, float y,int trashRequired) {
        this.x = x;
        this.y = y;
        this.trashRequired = trashRequired;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void render(SpriteBatch batch, Texture lockedTexture, Texture unlockedTexture) {
        Texture current = isLocked ? lockedTexture : unlockedTexture;
        batch.draw(current, x, y, width, height);
    }

    public void addTrash() {
        trashCollected++;
        if (trashCollected >= trashRequired) {
            unlock();
        }
    }

    public void unlock() {
        isLocked = false;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setPositions(float x,float y){
        this.x = x;
        this.y = y;
        this.bounds.setPosition(x, y);
    }
    public Rectangle getBounds() {
        bounds.setPosition(x, y);
        return bounds;
    }


    public float getX() { return x; }
    public float getY() { return y; }
    public int getTrashCollected() { return trashCollected; }
    public int getTrashRequired() { return trashRequired; }
}
