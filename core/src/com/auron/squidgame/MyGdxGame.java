package com.auron.squidgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture textureSpriteOne;
	private Texture textureSpriteBlood;

	private ShapeRenderer sr;

	private TextureRegion[][] framesSpriteOne;
	private TextureRegion[][] framesSpriteBlood;
	private float elapsedTime;

	private Player player;
	private List<Player> bots;

	private Music jugaremosNormalSound;
	private Music jugaremosLentoSound;
	private Music jugaremosRapidoSound;
	private Sound disparo;
	private float elaspedTimeSound;

	@Override
	public void create () {
		batch = new SpriteBatch();

		sr = new ShapeRenderer();
		sr.setColor(Color.WHITE);

		textureSpriteOne = new Texture("squid_1.png");
		textureSpriteBlood = new Texture("blood.png");
		framesSpriteOne = TextureRegion.split(textureSpriteOne,32,32);
		framesSpriteBlood = TextureRegion.split(textureSpriteBlood,32,32);

		player = new Player(framesSpriteOne,7,framesSpriteBlood[Utils.getRandomNumberInt(0,3)][Utils.getRandomNumberInt(0,2)]);
		bots = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			bots.add( new Player(framesSpriteOne, (int) Utils.getRandomNumber(0,7),framesSpriteBlood[Utils.getRandomNumberInt(0,3)][Utils.getRandomNumberInt(0,2)]));
		}

		jugaremosNormalSound = Gdx.audio.newMusic(Gdx.files.internal("jugaremos_normal.mp3"));
		jugaremosLentoSound = Gdx.audio.newMusic(Gdx.files.internal("jugaremos_lento.mp3"));
		jugaremosRapidoSound = Gdx.audio.newMusic(Gdx.files.internal("jugaremos_rapido.mp3"));
		disparo = Gdx.audio.newSound(Gdx.files.internal("disparo.mp3"));
		jugaremosNormalSound.setLooping(false);
		jugaremosLentoSound.setLooping(false);
		jugaremosRapidoSound.setLooping(false);
		elaspedTimeSound = 5;

	}

	@Override
	public void render () {

		elapsedTime += Gdx.graphics.getDeltaTime();

		ScreenUtils.clear(0.96f, 0.85f, 0.67f, 1);

		//dibujar lineas de estadio
		float widthLine = 8;
		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.circle(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f,48);
		sr.setColor(new Color(0.96f, 0.85f, 0.67f,1));
		sr.circle(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f,48-widthLine);
		sr.setColor(Color.WHITE);
		sr.rectLine(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight(),
				Gdx.graphics.getWidth()/2f, 0, widthLine);
		sr.rectLine(75, Gdx.graphics.getHeight(),
				75, 0, widthLine);
		sr.rectLine(Gdx.graphics.getWidth()-75, Gdx.graphics.getHeight(),
				Gdx.graphics.getWidth()-75, 0, widthLine);

		sr.end();

		//dibujar jugadores
		batch.begin();

		for (int i = bots.size() - 1; i >= 0; i--) {
			Player bot = bots.get(i);
			if(bot.isMuerto()){
				bot.draw(batch, elapsedTime);
			}
		}

		for (int i = bots.size() - 1; i >= 0; i--) {
			Player bot = bots.get(i);
			if(!bot.isMuerto()){
				bot.draw(batch, elapsedTime);
				if(isPlayJugaremos()){
					boolean morir = Utils.getRandomNumber(0,1)<0.9995;
					if(morir){
						bot.setMoviendose(false);
					}else{
						bot.setMuerto(true);
						if(bot.isMuerto()){
							disparo.play();
							bots.set(i, bots.get(bots.size()-1));
							bots.set(bots.size()-1, bot);
						}
					}
				}else if(bot.getTimeActionBot()>elapsedTime){
					if(bot.getActionBot()==ActionBot.MOVERSE){
						bot.moveRight();
					}else{
						bot.setMoviendose(false);
					}
				}else{
					if(Utils.getRandomNumberInt(0,2)==0){
						bot.moveRight();
						bot.setActionBot(ActionBot.MOVERSE);
						bot.setTimeActionBot(elapsedTime+Utils.getRandomNumber(2,6));
					}else{
						bot.setMoviendose(false);
						bot.setActionBot(ActionBot.PARADO);
						bot.setTimeActionBot(elapsedTime+Utils.getRandomNumber(2,6));
					}
				}
			}
		}

		if(!player.isMuerto()){
			player.setMoviendose(false);
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				player.moveLeft();
			}
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				player.moveRight();
			}
			if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
				player.moveUp();
			}
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				player.moveDown();
			}
			if(player.isMoviendose() && isPlayJugaremos()){
				player.setMuerto(true);
				if(player.isMuerto()) {
					disparo.play();
				}
			}
		}

		player.draw(batch, elapsedTime);

		batch.end();

		if(!isPlayJugaremos()){
			if(elaspedTimeSound<elapsedTime){
				int probabilidad = Utils.getRandomNumberInt(1,101);
				if(probabilidad<50){
					jugaremosNormalSound.play();
				}else if(probabilidad<85){
					jugaremosRapidoSound.play();
				}else{
					jugaremosLentoSound.play();
				}
			}
		}else{
			elaspedTimeSound=elapsedTime+Utils.getRandomNumberInt(3,7);
		}

	}

	boolean isPlayJugaremos(){
		return jugaremosNormalSound.isPlaying() || jugaremosLentoSound.isPlaying() || jugaremosRapidoSound.isPlaying();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		textureSpriteOne.dispose();
		jugaremosNormalSound.dispose();
		jugaremosLentoSound.dispose();
		jugaremosRapidoSound.dispose();
		disparo.dispose();
		textureSpriteBlood.dispose();
	}
}
