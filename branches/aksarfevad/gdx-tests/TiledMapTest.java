package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.tests.utils.OrthoCamController;
import com.badlogic.gdx.tiled.TileAtlas;
import com.badlogic.gdx.tiled.TiledLayerSpriteCache;
import com.badlogic.gdx.tiled.TiledLoader;
import com.badlogic.gdx.tiled.TiledMap;
import com.badlogic.gdx.tiled.TiledObject;
import com.badlogic.gdx.tiled.TiledObjectGroup;

public class TiledMapTest extends GdxTest{

	private static final int SCREEN_WIDTH = 480;
	private static final int SCREEN_HEIGHT = 320;
	
	SpriteBatch spriteBatch;
	BitmapFont font;
	
	OrthographicCamera cam;
	OrthoCamController camController;
	
	TiledLoader tLoader;
	TiledLayerSpriteCache tmSpriteCache;
	TiledMap map;
	TileAtlas atlas;
	
	@Override public void render () {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		cam.update();
		
		tmSpriteCache.getProjectionMatrix().set(cam.getCombinedMatrix());
		tmSpriteCache.render((int)cam.getScreenToWorldX(0), (int)cam.getScreenToWorldY(0)-Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		spriteBatch.begin();
			font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 20);
			font.draw(spriteBatch, "InitialCol, LastCol: " + tmSpriteCache.getInitialCol() + "," + tmSpriteCache.getLastCol(), 20, 40);
			font.draw(spriteBatch, "InitialRow, LastRow: " + tmSpriteCache.getInitialRow() + "," + tmSpriteCache.getLastRow(), 20, 60);
		spriteBatch.end();
	}

	@Override public void create () {
		int i;
		font = new BitmapFont();
		font.setColor(Color.RED);
		
		spriteBatch = new SpriteBatch();
		 
		tLoader = new TiledLoader();
		
		FileHandle mapHandle = Gdx.files.internal("data/tilemap.tmx");
		FileHandle packfile = Gdx.files.internal("data/packfile");
		FileHandle baseDir = Gdx.files.internal("data");
		
		map = tLoader.createMap(mapHandle, baseDir);
		atlas = new TileAtlas(map, packfile, baseDir);
		
		int blockWidth = SCREEN_WIDTH/2;
		int blockHeight = SCREEN_HEIGHT/2;
		
		tmSpriteCache = new TiledLayerSpriteCache(map, atlas, blockWidth, blockHeight);
		
		//Add sprites where objects occur
		for(TiledObjectGroup group: map.objectGroups){
			for(TiledObject object: group.objects){
				//TODO: draw the objects
				System.out.println("Object " + object.name + " x,y = " + object.x + "," + object.y
						+ " width,height = " + object.width + "," + object.height);
			}
		}
		
		cam = new OrthographicCamera();
		cam.setViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
		cam.getPosition().set(tmSpriteCache.getMapWidthPixels()/2, tmSpriteCache.getMapHeightPixels()/2 ,0);
		camController = new OrthoCamController(cam);
		Gdx.input.setInputProcessor(camController);
	}
	
	@Override
	public boolean needsGL20() {
		return false;
	}
}
