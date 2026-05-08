package com.joko.pembasmihsampah.utils;

import static com.joko.pembasmihsampah.utils.GameConstant.SCREEN_HEIGHT;

public class LevelData {

    public static class Level {
        public int playerStartX;
        public int playerStartY;
        public int gateX;
        public int gateY;
        public int trashRequired;
        public int difficulty;
        public String levelName;
    }

    // ===== LEVEL 1 (MyWorld from Greenfoot) =====
    public static Level LEVEL_1() {
        Level level = new Level();
        level.playerStartX = 34;
        level.playerStartY =  166;
        level.gateX = 782;
        level.gateY = SCREEN_HEIGHT - 413;
        level.trashRequired = 5;
        level.difficulty = 1;
        level.levelName = "Level 1 - Easy";
        return level;
    }

    // ===== LEVEL 2 (Level2 from Greenfoot) =====
    public static Level LEVEL_2() {
        Level level = new Level();
        level.playerStartX = 1127;
        level.playerStartY = SCREEN_HEIGHT -  598;
        level.gateX = 1112;
        level.gateY = SCREEN_HEIGHT - 391;
        level.trashRequired = 5;
        level.difficulty = 2;
        level.levelName = "Level 2 - Challenge";
        return level;
    }

    // ===== LEVEL 3 (BossWorld from Greenfoot) =====
    public static Level LEVEL_3() {
        Level level = new Level();
        level.playerStartX = 100;
        level.playerStartY =SCREEN_HEIGHT -  200;
        level.gateX = 1000;
        level.gateY =SCREEN_HEIGHT -  180;
        level.trashRequired = 5;
        level.difficulty = 3;
        level.levelName = "Level 3 - Boss Fight";
        return level;
    }

    public static Level getLevel(int levelNumber) {
        switch (levelNumber) {
            case 1: return LEVEL_1();
            case 2: return LEVEL_2();
            case 3: return LEVEL_3();
            default: return LEVEL_1();
        }
    }

    public static int getTotalLevels() {
        return 3;
    }
}
