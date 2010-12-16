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

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.SpriteCache;
import com.badlogic.gdx.graphics.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.MathUtils;

public class TiledLayerSpriteCache {
	private SpriteCache cache;
	private int normalCacheId[][], blendedCacheId[][];
	//TODO: will need 2 cache Ids for each area chunk (block), one for normal and one for blended
	
	private TiledMap map;
	private TileAtlas atlas;
	
	private int pixelsPerMapX, pixelsPerMapY;
	private int blocksPerMapY, blocksPerMapX;
	private int tilesPerBlockY, tilesPerBlockX;
	
	private ArrayList<Integer> blendedTiles;
	
    /**
     * Draws a Tiled layer using a Sprite Cache
     * @param layers The layer to be drawn
     * @param tileSets The tile set used to draw this layer - note only one tile set per Tiled layer
     * @param blockWidth The width of each block to be drawn, in pixels
     * @param blockHeight The width of each block to be drawn, in pixels
     */
	public TiledLayerSpriteCache(TiledMap map, TileAtlas atlas, int blockWidth, int blockHeight) {
		this(map, atlas, blockWidth, blockHeight, null);
	}
	
    /**
     * Draws a Tiled layer using a Sprite Cache
     * @param layers The layer to be drawn
     * @param tileSets The tile set used to draw this layer - note only one tile set per Tiled layer
     * @param blockWidth The width of each block to be drawn, in pixels
     * @param blockHeight The width of each block to be drawn, in pixels
     * @param shader Shader to use for OpenGL ES 2.0
     */
	public TiledLayerSpriteCache(TiledMap map, TileAtlas atlas, int blockWidth, int blockHeight, ShaderProgram shader) {
		this.map = map;
		this.atlas = atlas;
		
		if (!map.orientation.equals("orthogonal"))
			throw new GdxRuntimeException("Only orthogonal maps supported!");
		
		tilesPerBlockX = (int) Math.ceil((float)blockWidth/(float)map.tileWidth);
		tilesPerBlockY = (int) Math.ceil((float)blockHeight/(float)map.tileHeight);
		
		pixelsPerMapX = map.width * map.tileWidth;
		pixelsPerMapY = map.height * map.tileHeight;
		
		blocksPerMapX = (int) Math.ceil((float)map.width/(float)tilesPerBlockX);
		blocksPerMapY = (int) Math.ceil((float)map.height/(float)tilesPerBlockY);
		
		normalCacheId = new int[blocksPerMapY][blocksPerMapX];
		blendedCacheId = new int[blocksPerMapY][blocksPerMapX];
		
		String blendedTilesString = map.properties.get("blended tiles");
		if(blendedTilesString != null){
			blendedTiles = createFromCSV(blendedTilesString);
		}
		
		int maxCacheSize = 0;
		for(int i = 0; i < map.layers.size(); i++){
			maxCacheSize += map.layers.get(i).height * map.layers.get(i).width;
		}
		
		if(shader == null)
			cache = new SpriteCache(maxCacheSize, false);
		else
			cache = new SpriteCache(maxCacheSize, shader, false);
		//TODO: Don't really need a cache that holds all tiles,
		//really only need room for all VISIBLE tiles.
		//Should compute this during compiling
		
		for(int row = 0; row < blocksPerMapY; row++){
			for(int col = 0; col < blocksPerMapX; col++){
				normalCacheId[row][col] = addBlock(row, col, false);
				blendedCacheId[row][col] = addBlock(row, col, true);
			}
		}
	}
	
	private ArrayList<Integer> createFromCSV(String values) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		StringTokenizer st = new StringTokenizer(values,",");
		while (st.hasMoreTokens())
		{
			list.add(Integer.valueOf(st.nextToken()));
		}
		return list;
	}

	private int addBlock(int blockRow, int blockCol, boolean blended){
		int tile;
		AtlasRegion region;
		cache.beginCache();
		
		int tileRow = blockRow*tilesPerBlockY;
		int tileCol = blockCol*tilesPerBlockX;
		
		float x = tileCol*map.tileWidth;
		float y = (tileRow+1)*map.tileHeight;
		
		for(int row = 0; row < tilesPerBlockY && tileRow < map.height; row++){
			for(int col = 0; col < tilesPerBlockX && tileCol < map.width; col++){
				for(int i = 0; i < map.layers.size(); i++){
					tile = map.layers.get(i).tile[map.layers.get(i).height - tileRow - 1][tileCol];
					if(tile != 0){
						if(blended == blendedTiles.contains(tile)){
							region = atlas.getRegion(tile);
							cache.add(region, x, y);
						}
					}
				}
				x += map.tileWidth;
				tileCol++;
			}
			y += map.tileHeight;
			tileCol = blockCol*tilesPerBlockX;
			x = tileCol*map.tileWidth;
			tileRow++;
		}
		
		return cache.endCache();
	}
	
	//This function should not be used most of the time. Use render(int x, int y, int width, int height) instead.
	public void render() {
		render(0,0,pixelsPerMapX,pixelsPerMapY);	
	}
	
	private int initialRow, initialCol, currentRow, currentCol, lastRow, lastCol;
	
	public void render(int x, int y, int width, int height) {
		if(x > pixelsPerMapX || y > pixelsPerMapY) return;
		initialRow = getBlockRow(y);
		initialRow = (initialRow > 0) ? initialRow: 0;
		initialCol = getBlockCol(x);
		initialCol = (initialCol > 0) ? initialCol: 0;
		lastRow = getBlockRow(y + height);
		lastRow = (lastRow < blocksPerMapY) ? lastRow: blocksPerMapY-1;
		lastCol = getBlockCol(x + width);
		lastCol = (lastCol < blocksPerMapX) ? lastCol: blocksPerMapX-1;
		
		cache.begin();
		for(currentRow = initialRow; currentRow <= lastRow; currentRow++){
			for(currentCol = initialCol; currentCol <= lastCol; currentCol++){
				Gdx.gl.glDisable(GL10.GL_BLEND);
				cache.draw(normalCacheId[currentRow][currentCol]);
				Gdx.gl.glEnable(GL10.GL_BLEND);
				cache.draw(blendedCacheId[currentRow][currentCol]);
			}
		}
		cache.end();
		Gdx.gl.glDisable(GL10.GL_BLEND);
		
	}
	
	public int getInitialRow() {
		return initialRow;
	}

	public int getInitialCol() {
		return initialCol;
	}

	public int getLastRow() {
		return lastRow;
	}

	public int getLastCol() {
		return lastCol;
	}

	private int getBlockRow(int y){
		return y/(tilesPerBlockY*map.tileHeight);
	}
	
	private int getBlockCol(int x){
		return x/(tilesPerBlockX*map.tileWidth);
	}
	
	public Matrix4 getProjectionMatrix(){
		return cache.getProjectionMatrix();
	}
	
	public Matrix4 getTransformMatrix(){
		return cache.getTransformMatrix();
	}
	
	public int getMapHeightPixels() {
		return pixelsPerMapY;
	}

	public int getMapWidthPixels() {
		return pixelsPerMapX;
	}

	int getRow(int worldY){
		if(worldY < 0) return 0;
		if(worldY > pixelsPerMapY) return map.tileHeight - 1;
		return worldY/map.tileHeight;
	}
	
	int getCol(int worldX){
		if(worldX < 0) return 0;
		if(worldX > pixelsPerMapX) return map.tileWidth - 1;
		return worldX/map.tileWidth;
	}
	
	void dispose() {
		cache.dispose();
	}
}
