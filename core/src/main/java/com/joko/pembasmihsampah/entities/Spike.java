package com.joko.pembasmihsampah.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class Spike {
    private final float x;
    private final float y;
    private final float width = 40f;
    private final float height = 40f;
    private final Rectangle bounds;

    public Spike(float x, float y) {
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void render(SpriteBatch batch, Texture texture) {
        batch.draw(texture, x, y, width, height);
    }

    public Rectangle getBounds() {
        bounds.setPosition(x, y);
        return bounds;
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
