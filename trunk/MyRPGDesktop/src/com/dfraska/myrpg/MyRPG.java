
package com.dfraska.myrpg;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.WindowedMean;
import com.badlogic.gdx.tiled.TiledLayerMesh;
import com.badlogic.gdx.tiled.TiledMap;
import com.badlogic.gdx.tiled.TiledMapFactory;

public class MyRPG implements ApplicationListener {	
	SpriteBatch spriteBatch;
	Texture texture;
	BitmapFont font;
	Vector2 textPosition = new Vector2(100, 100);
	Vector2 textDirection = new Vector2(1, 1);
	
	private OrthographicCamera camera;
	
	TiledMapFactory tmFactory;
	ArrayList<TiledLayerMesh> tmMesh;
	TiledMap map;
	
	WindowedMean renderTime;
	
	//TODO: create a state machine that has states for:
	//loading, paused and/or menu, running environment, running fight, etc.
	
	@Override public void dispose () {
		
	}

	@Override public void render () {
		int centerX = Gdx.graphics.getWidth() / 2;
		int centerY = Gdx.graphics.getHeight() / 2;
		
		int i;
		
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (textPosition.x < 0 || textPosition.x > Gdx.graphics.getWidth()) textDirection.x = -textDirection.x;
		if (textPosition.y < 0 || textPosition.y > Gdx.graphics.getHeight()) textDirection.y = -textDirection.y;

		textPosition.add(textDirection.tmp().mul(Gdx.graphics.getDeltaTime()).mul(60));
		
		long startTime = System.nanoTime();
		for(i = 0; i < tmMesh.size(); i++){
			tmMesh.get(i).render(0, tmMesh.get(i).getLayerHeightPixels(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		long endTime = System.nanoTime();
		renderTime.addValue(endTime - startTime);
		
		spriteBatch.begin();
		spriteBatch.draw(texture, centerX - texture.getWidth() / 2, centerY - texture.getHeight() / 2, 0, 0, texture.getWidth(),
			texture.getHeight(), Color.WHITE);
		font.draw(spriteBatch, "Hello World!", (int)textPosition.x, (int)textPosition.y, Color.RED);
		font.draw(spriteBatch, "Map Render Time (nS): " + renderTime.getMean(), 20, 20, Color.RED);
		spriteBatch.end();
	}

	@Override public void resize (int width, int height) {
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		//camera.setViewport(width, height);
		textPosition.set(0,0);
	}

	@Override public void create () {
		renderTime = new WindowedMean(100);
		
		int i;
		font = new BitmapFont();
		texture = Gdx.graphics.newTexture(Gdx.files.getFileHandle("data/arrow.png", FileType.Internal), TextureFilter.Linear,
			TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
		spriteBatch = new SpriteBatch();
		 
		tmFactory = new TiledMapFactory();
		
		map = tmFactory.createMap("data/tilemap.tmx", "data/", FileType.Internal);
		
		tmMesh = new ArrayList<TiledLayerMesh>(map.layer.size());
		for(i = 0; i < map.layer.size(); i++){
			tmMesh.add(new TiledLayerMesh(map.layer.get(i), map.tileSet.get(i)));
		}
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}
}