package com.auron.squidgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {

    public static final float VELOCITY_PLAYER = 25;

    private Animation<TextureRegion> animationDown;
    private Animation<TextureRegion> animationLeft;
    private Animation<TextureRegion> animationRight;
    private Animation<TextureRegion> animationUp;

    private Direction direction;
    private boolean moviendose;

    private float x;
    private float y;

    private ActionBot actionBot;
    private float timeActionBot;

    private TextureRegion textureRegionMuerto;
    private boolean muerto;

    public Player(TextureRegion[][] textureRegion, int skin, TextureRegion textureRegionMuerto) {
        int moveY = (skin/4)*4;
        int moveX = (skin%4)*3;
        animationDown = new Animation<TextureRegion>(1f / 6f,
                textureRegion[0+moveY][0+moveX], textureRegion[0+moveY][1+moveX], textureRegion[0+moveY][2+moveX]);
        animationLeft = new Animation<TextureRegion>(1f / 6f,
                textureRegion[1+moveY][0+moveX], textureRegion[1+moveY][1+moveX], textureRegion[1+moveY][2+moveX]);
        animationRight = new Animation<TextureRegion>(1f / 6f,
                textureRegion[2+moveY][0+moveX], textureRegion[2+moveY][1+moveX], textureRegion[2+moveY][2+moveX]);
        animationUp = new Animation<TextureRegion>(1f / 6f,
                textureRegion[3+moveY][0+moveX], textureRegion[3+moveY][1+moveX], textureRegion[3+moveY][2+moveX]);
        moviendose = false;
        x = Utils.getRandomNumber(0, 75-32);
        y = Utils.getRandomNumber(0, Gdx.graphics.getHeight());
        direction = Direction.RIGHT;

        actionBot = ActionBot.PARADO;
        timeActionBot = 2.5f;
        muerto = false;
        this.textureRegionMuerto = textureRegionMuerto;
    }

    public boolean isMuerto() {
        return muerto;
    }

    public void setMuerto(boolean muerto) {
        if(x>75-5&&x<Gdx.graphics.getWidth()-75-32){
            this.muerto = muerto;
        }
    }

    public ActionBot getActionBot() {
        return actionBot;
    }

    public void setActionBot(ActionBot actionBot) {
        this.actionBot = actionBot;
    }

    public float getTimeActionBot() {
        return timeActionBot;
    }

    public void setTimeActionBot(float timeActionBot) {
        this.timeActionBot = timeActionBot;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void moveLeft() {
        x -= VELOCITY_PLAYER * Gdx.graphics.getDeltaTime();
        setDirection(Direction.LEFT);
    }

    public void moveRight() {
        x += VELOCITY_PLAYER * Gdx.graphics.getDeltaTime();
        setDirection(Direction.RIGHT);
    }

    public void moveUp() {
        y += VELOCITY_PLAYER * Gdx.graphics.getDeltaTime();
        setDirection(Direction.UP);
    }

    public void moveDown() {
        y -= VELOCITY_PLAYER * Gdx.graphics.getDeltaTime();
        setDirection(Direction.DOWN);
    }

    public boolean isMoviendose() {
        return moviendose;
    }

    public void setMoviendose(boolean moviendose) {
        this.moviendose = moviendose;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        moviendose = true;
    }

    void normalizeXY() {
        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;
        if (x > Gdx.graphics.getWidth()-32)
            x = Gdx.graphics.getWidth()-32;
        if (y > Gdx.graphics.getHeight()-32)
            y = Gdx.graphics.getHeight()-32;
    }

    public void draw(Batch batch, float elapsedTime) {
        Animation<TextureRegion> animation = direction == Direction.DOWN ? animationDown : direction == Direction.LEFT ? animationLeft : direction == Direction.RIGHT ? animationRight : animationUp;
        normalizeXY();
        if(muerto){
            batch.draw(textureRegionMuerto, x, y);
        }else{
            batch.draw(moviendose ? animation.getKeyFrame(elapsedTime, true) : animation.getKeyFrames()[1], x, y);
        }
    }

}
