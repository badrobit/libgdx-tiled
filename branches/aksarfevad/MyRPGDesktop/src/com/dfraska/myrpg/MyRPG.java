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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.WindowedMean;
import com.badlogic.gdx.tiled.TiledLayerSpriteCache;
import com.badlogic.gdx.tiled.TiledMap;
import com.badlogic.gdx.tiled.TiledMapFactory;

public class MyRPG implements ApplicationListener {	
	SpriteBatch spriteBatch;
	BitmapFont font;
	
	Vector2 mapPosition = new Vector2(100,100);
	Vector2 maxMapPosition = new Vector2(0,0);
	Vector2 mapDirection = new Vector2(1,1);
	
	TiledMapFactory tmFactory;
	ArrayList<TiledLayerSpriteCache> tmSpriteCache;
	TiledMap map;
	
	@Override public void dispose () {
		
	}

	@Override public void render () {
		int i;
		
		updateMapPosition();
		
		for(i = 0; i < tmSpriteCache.size(); i++){
			tmSpriteCache.get(i).getTransformMatrix().setToTranslation(-mapPosition.x, -mapPosition.y, 1f);
			tmSpriteCache.get(i).render((int)mapPosition.x, (int)mapPosition.y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		
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
		for(i=0; i < tmSpriteCache.size(); i++){
			tmSpriteCache.get(i).getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		}
		
		mapPosition.set(0,0);
		float x = tmSpriteCache.get(0).getLayerWidthPixels() - Gdx.graphics.getWidth();
		float y = tmSpriteCache.get(0).getLayerHeightPixels() - Gdx.graphics.getHeight();
		maxMapPosition.set(x,y);
	}

	@Override public void create () {
		int i;
		font = new BitmapFont();
		font.setColor(Color.RED);
		
		spriteBatch = new SpriteBatch();
		 
		tmFactory = new TiledMapFactory();
		
		map = tmFactory.createMap("data/tilemap.tmx", "data/", FileType.Internal);
		
		tmSpriteCache = new ArrayList<TiledLayerSpriteCache>(map.layer.size());
		for(i = 0; i < map.layer.size(); i++){
			tmSpriteCache.add(new TiledLayerSpriteCache(map.layer.get(i), map.tileSet.get(i), (int)(Gdx.graphics.getWidth()/(4*map.tileSet.get(i).tileWidth)), (int)(Gdx.graphics.getHeight()/(4*map.tileSet.get(i).tileHeight))));
		}
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}
}