package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.tests.utils.OrthoCamController;
import com.badlogic.gdx.tiled.TileAtlas;
import com.badlogic.gdx.tiled.TiledLoader;
import com.badlogic.gdx.tiled.TiledMap;
import com.badlogic.gdx.tiled.TiledMapRenderer;
import com.badlogic.gdx.tiled.TiledObject;
import com.badlogic.gdx.tiled.TiledObjectGroup;

public class TiledMapTest extends GdxTest{
	
	private static final boolean automove = false;
	
	private static final int SCREEN_WIDTH = 480;
	private static final int SCREEN_HEIGHT = 320;

	private static final int[] layersList = {0,1,2};
	
	SpriteBatch spriteBatch;
	BitmapFont font;
	
	OrthographicCamera cam;
	OrthoCamController camController;
	Vector3 camDirection = new Vector3(1,1,0);
	Vector2 maxCamPosition = new Vector2(0,0);
	
	TiledLoader tLoader;
	TiledMapRenderer tiledMapRenderer;
	TiledMap map;
	TileAtlas atlas;
	
	long startTime = System.nanoTime();
	
	@Override public void render () {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		cam.update();
		
		if(automove){
			updateCameraPosition();
		}
		
		tiledMapRenderer.getProjectionMatrix().set(cam.getCombinedMatrix());
		tiledMapRenderer.render((int)cam.getScreenToWorldX(0), tiledMapRenderer.getMapHeightPixels() - (int)cam.getScreenToWorldY(0), Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), layersList);
		//tmSpriteCache.render();
		
		if (System.nanoTime() - startTime >= 1000000000) {
			Gdx.app.log("TiledMapTest", "fps: " + Gdx.graphics.getFramesPerSecond());
			startTime = System.nanoTime();
		}
		
		spriteBatch.begin();
			font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 20);
			font.draw(spriteBatch, "InitialCol, LastCol: " + tiledMapRenderer.getInitialCol() + "," + tiledMapRenderer.getLastCol(), 20, 40);
			font.draw(spriteBatch, "InitialRow, LastRow: " + tiledMapRenderer.getInitialRow() + "," + tiledMapRenderer.getLastRow(), 20, 60);
			font.draw(spriteBatch, "cam.getScreenToWorldY(0): " + cam.getScreenToWorldY(0), 20, 80);
		spriteBatch.end();
	}

	private void updateCameraPosition() {
		cam.getPosition().add(camDirection.tmp().mul(Gdx.graphics.getDeltaTime()).mul(60));
		
		if (cam.getPosition().x < 0){
			cam.getPosition().x = 0;
			camDirection.x = 1;
		}
		if (cam.getPosition().x > maxCamPosition.x){
			cam.getPosition().x = maxCamPosition.x;
			camDirection.x = -1;
		}
		if (cam.getPosition().y < 0){
			cam.getPosition().y = 0;
			camDirection.y = 1;
		}
		if (cam.getPosition().y > maxCamPosition.y){
			cam.getPosition().y = maxCamPosition.y;
			camDirection.y = -1;
		}
	}

	@Override public void create () {
		int i;
		long startTime, endTime;
		font = new BitmapFont();
		font.setColor(Color.RED);
		
		spriteBatch = new SpriteBatch();
		 
		tLoader = new TiledLoader();
		
		FileHandle mapHandle = Gdx.files.internal("data/tilemap gzip.tmx");
		FileHandle packfile = Gdx.files.internal("data/tilemap gzip packfile");
		FileHandle baseDir = Gdx.files.internal("data");
		
		startTime = System.currentTimeMillis();
		map = tLoader.createMap(mapHandle, baseDir);
		endTime = System.currentTimeMillis();
		System.out.println("Loaded map in " + (endTime - startTime) + "mS");
		
		atlas = new TileAtlas(map, packfile, baseDir);
		
		int blockWidth = SCREEN_WIDTH/2;
		int blockHeight = SCREEN_HEIGHT/2;
		
		startTime = System.currentTimeMillis();
		tiledMapRenderer = new TiledMapRenderer(map, atlas, blockWidth, blockHeight);
		endTime = System.currentTimeMillis();
		System.out.println("Created cache in " + (endTime - startTime) + "mS");
		
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
		cam.getPosition().set(tiledMapRenderer.getMapWidthPixels()/2, tiledMapRenderer.getMapHeightPixels()/2 ,0);
		camController = new OrthoCamController(cam);
		Gdx.input.setInputProcessor(camController);
		
		float maxX = tiledMapRenderer.getMapWidthPixels();
		float maxY = tiledMapRenderer.getMapHeightPixels();
		maxCamPosition.set(maxX,maxY);
	}
	
	@Override
	public boolean needsGL20() {
		return false;
	}
}
