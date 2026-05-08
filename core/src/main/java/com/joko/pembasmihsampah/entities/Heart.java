package com.joko.pembasmihsampah.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Heart {
    private float x, y;
    private final float width = 45f;
    private final float height = 40f;
    private final Rectangle bounds;

    public Heart(float x, float y) {
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void render(SpriteBatch batch, Texture texture) {
        batch.draw(texture, x, y, width, height);
        bounds.setPosition(x, y);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        bounds.setPosition(x, y);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public Rectangle getBounds() { return bounds; }
}
