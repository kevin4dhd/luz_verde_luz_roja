package com.auron.squidgame;

public class Utils {

    public static float getRandomNumber(float min, float max) {
        return (float) ((Math.random() * (max - min)) + min);
    }

    public static int getRandomNumberInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
