/*
 * Copyright 2010 David Fraska (dfraska@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.dfraska.myrpg;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.tiled.TileAtlas;
import com.badlogic.gdx.tiled.TiledLayerSpriteCache;
import com.badlogic.gdx.tiled.TiledLoader;
import com.badlogic.gdx.tiled.TiledMap;
import com.badlogic.gdx.tiled.TiledObject;
import com.badlogic.gdx.tiled.TiledObjectGroup;

public class MyRPG implements ApplicationListener {	
	SpriteBatch spriteBatch;
	BitmapFont font;
	
	Vector2 mapPosition = new Vector2(100,100);
	Vector2 maxMapPosition = new Vector2(0,0);
	Vector2 mapDirection = new Vector2(1,1);
	
	TiledLoader tLoader;
	TiledLayerSpriteCache tmSpriteCache;
	TiledMap map;
	TileAtlas atlas;
	
	@Override public void dispose () {
		
	}

	@Override public void render () {
		int i;
		
		updateMapPosition();
		
		tmSpriteCache.getTransformMatrix().setToTranslation(-mapPosition.x, -mapPosition.y, 1f);
		tmSpriteCache.render((int)mapPosition.x, (int)mapPosition.y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		spriteBatch.begin();
			font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 20);
		spriteBatch.end();
	}

	private void updateMapPosition(){
		mapPosition.add(mapDirection.tmp().mul(Gdx.graphics.getDeltaTime()).mul(60));
		
		if (mapPosition.x < 0){
			mapPosition.x = 0;
			mapDirection.x = 1;
		}
		if (mapPosition.x > maxMapPosition.x){
			mapPosition.x = maxMapPosition.x;
			mapDirection.x = -1;
		}
		if (mapPosition.y < 0){
			mapPosition.y = 0;
			mapDirection.y = 1;
		}
		if (mapPosition.y > maxMapPosition.y){
			mapPosition.y = maxMapPosition.y;
			mapDirection.y = -1;
		}
	}
	
	@Override public void resize (int width, int height) {
		int i;
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		tmSpriteCache.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		
		mapPosition.set(0,0);
		float maxX = tmSpriteCache.getMapWidthPixels() - width;
		float maxY = tmSpriteCache.getMapHeightPixels() - height;
		maxMapPosition.set(maxX,maxY);
	}

	@Override public void create () {
		int i;
		font = new BitmapFont();
		font.setColor(Color.RED);
		
		spriteBatch = new SpriteBatch();
		 
		tLoader = new TiledLoader();
		
		FileHandle mapHandle = Gdx.files.internal("data/tilemap.tmx");
		FileHandle packfile = Gdx.files.internal("data/tilemap_packfile");
		FileHandle baseDir = Gdx.files.internal("data");
		
		map = tLoader.createMap(mapHandle, baseDir);
		atlas = new TileAtlas(map, packfile, baseDir);
		
		int blockWidth = (int)(Gdx.graphics.getWidth()/2);
		int blockHeight = (int)(Gdx.graphics.getHeight()/2);
		
		tmSpriteCache = new TiledLayerSpriteCache(map, atlas, blockWidth, blockHeight);
		
		//Add sprites where objects occur
		for(TiledObjectGroup group: map.objectGroups){
			for(TiledObject object: group.objects){
				//TODO: draw the objects
				System.out.println("Object " + object.name + " x,y = " + object.x + "," + object.y
						+ " width,height = " + object.width + "," + object.height);
			}
		}
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}
}
