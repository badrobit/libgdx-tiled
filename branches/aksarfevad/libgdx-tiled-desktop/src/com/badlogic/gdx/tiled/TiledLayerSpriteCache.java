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

package com.badlogic.gdx.tiled;

import com.badlogic.gdx.graphics.SpriteCache;
import com.badlogic.gdx.graphics.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class TiledLayerSpriteCache {
	private SpriteCache cache;
	private int normalCacheId[][];//, blendedCacheId[][];
	//TODO: will need 2 cache Ids for each area chunk (block), one for normal and one for blended
	//Ideally, blocks should be just large enough so that at most 4 are visible at a time
	
	private TiledMap map;
	private TileAtlas atlas;
	
	private int mapWidthPixels, mapHeightPixels;
	private int mapHeightBlocks, mapWidthBlocks;
	private int blockHeightTiles, blockWidthTiles;
	
	//TODO: add constructor that takes a ShaderProgram for OpenGL ES 2.0 support
	
    /**
     * Draws a Tiled layer using a Sprite Cache
     * @param layers The layer to be drawn
     * @param tileSets The tile set used to draw this layer - note only one tile set per Tiled layer
     * @param blockWidth The width of each block to be drawn, in pixels
     * @param blockHeight The width of each block to be drawn, in pixels
     */
	public TiledLayerSpriteCache(TiledMap map, TileAtlas atlas, int blockWidth, int blockHeight) {
		//TODO: add a constructor that takes a ShaderProgram for OpenGL ES 2
		this.map = map;
		this.atlas = atlas;
		
		if (!map.orientation.equals("orthogonal"))
			throw new GdxRuntimeException("Only orthogonal maps supported!");
		
		blockHeightTiles = blockHeight;
		blockWidthTiles = blockWidth;
		
		mapHeightPixels = map.height * map.tileHeight;
		mapWidthPixels = map.width * map.tileWidth;
		
		mapWidthBlocks = (int) Math.ceil((float)map.width/(float)blockWidth);
		mapHeightBlocks = (int) Math.ceil((float)map.height/(float)blockHeight);
		
		normalCacheId = new int[mapHeightBlocks][mapWidthBlocks];
		
		int maxCacheSize = 0;
		for(int i = 0; i < map.layers.size(); i++){
			maxCacheSize += map.layers.get(i).height * map.layers.get(i).width;
		}
		
		cache = new SpriteCache(maxCacheSize, false);
		//TODO: Don't really need a cache that holds all tiles,
		//really only need room for all VISIBLE tiles.
		//If using compiled TMX format, compute this during that phase
		
		for(int row = 0; row < mapHeightBlocks; row++){
			for(int col = 0; col < mapWidthBlocks; col++){
				normalCacheId[row][col] = addBlock(row, col);
			}
		}
		
	}
	
	private int addBlock(int blockRow, int blockCol){
		int tile;
		cache.beginCache();
		
		int tileRow = blockRow*blockHeightTiles;
		int tileCol = blockCol*blockWidthTiles;
		
		float x = tileCol*map.tileWidth;
		float y = (tileRow+1)*map.tileHeight;
		
		for(int row = 0; row < blockHeightTiles && tileRow < map.height; row++){
			for(int col = 0; col < blockWidthTiles && tileCol < map.width; col++){
				for(int i = 0; i < map.layers.size(); i++){
					tile = map.layers.get(i).tile[map.layers.get(i).height - tileRow - 1][tileCol];
					if(tile != 0){
						AtlasRegion region = atlas.getRegion(tile);
						cache.add(region, x, y);
					}
				}
				x += map.tileWidth;
				tileCol++;
			}
			y += map.tileHeight;
			tileCol = blockCol*blockWidthTiles;
			x = tileCol*map.tileWidth;
			tileRow++;
		}
		
		return cache.endCache();
	}
	
	//This function should not be used most of the time. Use render(int x, int y, int width, int height) instead.
	public void render() {
		render(0,0,mapWidthPixels,mapHeightPixels);	
	}
	
	private int initialRow, initialCol, currentRow, currentCol, lastRow, lastCol;
	
	public void render(int x, int y, int width, int height) {
		//Gdx.gl.glEnable(GL10.GL_BLEND);
		
		initialRow = getBlockRow(y);
		initialRow = (initialRow > 0) ? initialRow: 0;
		initialCol = Math.max(getBlockCol(x), 0);
		lastRow = Math.min(getBlockRow(y + height),mapHeightBlocks-1);
		lastCol = Math.min(getBlockCol(x + width),mapWidthBlocks-1);
		
		cache.begin();
		for(currentRow = initialRow; currentRow <= lastRow; currentRow++){
			for(currentCol = initialCol; currentCol <= lastCol; currentCol++){
				cache.draw(normalCacheId[currentRow][currentCol]);
			}
		}
		cache.end();
	}
	
	private int getBlockRow(int y){
		return y/(blockHeightTiles*map.tileHeight);
	}
	
	private int getBlockCol(int x){
		return x/(blockWidthTiles*map.tileWidth);
	}
	
	public Matrix4 getProjectionMatrix(){
		return cache.getProjectionMatrix();
	}
	
	public Matrix4 getTransformMatrix(){
		return cache.getTransformMatrix();
	}
	
	public int getMapHeightPixels() {
		return mapHeightPixels;
	}

	public int getMapWidthPixels() {
		return mapWidthPixels;
	}

	int getRow(int worldY){
		if(worldY < 0) return 0;
		if(worldY > mapHeightPixels) return map.tileHeight - 1;
		return worldY/map.tileHeight;
	}
	
	int getCol(int worldX){
		if(worldX < 0) return 0;
		if(worldX > mapWidthPixels) return map.tileWidth - 1;
		return worldX/map.tileWidth;
	}
	
	void dispose() {
		cache.dispose();
	}
}
