package com.joko.pembasmihsampah.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.joko.pembasmihsampah.utils.Assets;
import com.joko.pembasmihsampah.utils.GameConstant;

public class Trash {
    private final Texture texture;
    private final float x;
    private final float y;
    private int num1, num2;
    private char operator;
    private int correctAnswer;
    private boolean collected = false;

    private boolean showProblem = false;
    private final BitmapFont font;



    public Trash(float x, float y) {
        this.x = x;
        this.y = y;
        this.texture = Assets.get("trash.png",Texture.class);
        this.font = new BitmapFont();
        generateProblem();
    }

    private void generateProblem() {
        num1 = (int) (Math.random() * (GameConstant.MATH_MAX - GameConstant.MATH_MIN + 1)) + GameConstant.MATH_MIN;
        num2 = (int) (Math.random() * (GameConstant.MATH_MAX - GameConstant.MATH_MIN + 1)) + GameConstant.MATH_MIN;

        if (Math.random() < 0.5) {
            operator = '+';
            correctAnswer = num1 + num2;
        } else {
            operator = '-';
            // Ensure positive result
            if (num1 < num2) {
                int temp = num1;
                num1 = num2;
                num2 = temp;
            }
            correctAnswer = num1 - num2;
        }
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
    }

    public void showProblem(boolean show) {
        showProblem = show;
    }

    public void render(SpriteBatch batch,BitmapFont font) {
        // Draw trash object
        batch.draw(texture, x, y, 50, 50);

        // Draw problem text
        if (showProblem) {
            String problem = num1 + " " + operator + " " + num2 + " ?";
            font.draw(batch, problem, x - 30, y + 80);
        }
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public float getX() { return x; }
    public float getY() { return y; }
}

