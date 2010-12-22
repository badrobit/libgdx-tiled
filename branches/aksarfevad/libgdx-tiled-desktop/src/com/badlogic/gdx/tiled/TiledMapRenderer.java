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

import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.SpriteCache;
import com.badlogic.gdx.graphics.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntArray;

public class TiledMapRenderer {
	private SpriteCache cache;
	private int normalCacheId[][][], blendedCacheId[][][];
	
	private TiledMap map;
	private TileAtlas atlas;
	
	private int pixelsPerMapX, pixelsPerMapY;
	private int blocksPerMapY, blocksPerMapX;
	private int tilesPerBlockY, tilesPerBlockX;
	
	private IntArray blendedTiles;
	private int[] allLayers;
	
    /**
     * Draws a Tiled layer using a Sprite Cache
     * @param layers The layer to be drawn
     * @param tileSets The tile set used to draw this layer - note only one tile set per Tiled layer
     * @param blockWidth The width of each block to be drawn, in pixels
     * @param blockHeight The width of each block to be drawn, in pixels
     */
	public TiledMapRenderer(TiledMap map, TileAtlas atlas, int blockWidth, int blockHeight) {
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
	public TiledMapRenderer(TiledMap map, TileAtlas atlas, int blockWidth, int blockHeight, ShaderProgram shader) {
		this.map = map;
		this.atlas = atlas;
		
		if (!map.orientation.equals("orthogonal"))
			throw new GdxRuntimeException("Only orthogonal maps supported!");
		
		//allLayers array simplifies calling render without a layer list
		allLayers = new int[map.layers.size()];
		for(int i = 0; i < map.layers.size(); i++){
			allLayers[i] = i;
		}
		
		tilesPerBlockX = (int) Math.ceil((float)blockWidth/(float)map.tileWidth);
		tilesPerBlockY = (int) Math.ceil((float)blockHeight/(float)map.tileHeight);
		
		pixelsPerMapX = map.width * map.tileWidth;
		pixelsPerMapY = map.height * map.tileHeight;
		
		blocksPerMapX = (int) Math.ceil((float)map.width/(float)tilesPerBlockX);
		blocksPerMapY = (int) Math.ceil((float)map.height/(float)tilesPerBlockY);
		
		normalCacheId = new int[map.layers.size()][blocksPerMapY][blocksPerMapX];
		blendedCacheId = new int[map.layers.size()][blocksPerMapY][blocksPerMapX];
		
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
		int row, col, layer;
		
		for(row = 0; row < blocksPerMapY; row++){
			for(col = 0; col < blocksPerMapX; col++){
				for(layer = 0; layer < map.layers.size(); layer++){
					normalCacheId[layer][row][col] = addBlock(map.layers.get(layer), row, col, false);
					blendedCacheId[layer][row][col] = addBlock(map.layers.get(layer), row, col, true);
				}
			}
		}
	}
	
	private IntArray createFromCSV(String values) {
		IntArray list = new IntArray();
		StringTokenizer st = new StringTokenizer(values,",");
		while (st.hasMoreTokens())
		{
			list.add(Integer.parseInt(st.nextToken()));
		}
		list.shrink();
		return list;
	}
	
	private int addBlock(TiledLayer layer, int blockRow, int blockCol, boolean blended){
		int tile;
		AtlasRegion region;
		cache.beginCache();
		
		int firstCol = blockCol*tilesPerBlockX;
		int firstRow = blockRow*tilesPerBlockY;
		int lastCol = Math.min(firstCol+tilesPerBlockX, map.width);
		int lastRow = Math.min(firstRow+tilesPerBlockY, map.height);
		
		int row, col;
		
		for(row = firstRow; row < lastRow; row++){
			for(col = firstCol; col < lastCol; col++){
				tile = layer.tile[row][col];
				if(tile != 0){
					if(blended == blendedTiles.contains(tile)){
						region = atlas.getRegion(tile);
						cache.add(region, col*map.tileWidth, (map.height - row)*map.tileHeight);
					}
				}
			}
		}
		
		return cache.endCache();
	}
	
	//This function should not be used most of the time. Use render(int x, int y, int width, int height) instead.
	public void render() {
		render(0,0,pixelsPerMapX,pixelsPerMapY);	
	}
	
	public void render(int x, int y, int width, int height) {
		render(x,y,width,height,allLayers);
	}
	
	private int initialRow, initialCol, currentRow, currentCol, lastRow, lastCol, currentLayer;
	
	public void render(int x, int y, int width, int height, int[] layers){
		if(x > pixelsPerMapX || y > pixelsPerMapY) return;
		initialRow = y/(tilesPerBlockY*map.tileHeight);
		initialRow = (initialRow > 0) ? initialRow: 0;	
		initialCol = x/(tilesPerBlockX*map.tileWidth);
		initialCol = (initialCol > 0) ? initialCol: 0;
		lastRow = (y + height)/(tilesPerBlockY*map.tileHeight);
		lastRow = (lastRow < blocksPerMapY) ? lastRow: blocksPerMapY-1;
		lastCol = (x + width)/(tilesPerBlockX*map.tileWidth);
		lastCol = (lastCol < blocksPerMapX) ? lastCol: blocksPerMapX-1;
		
		Gdx.gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		cache.begin();
		for(currentRow = initialRow; currentRow <= lastRow; currentRow++){
			for(currentCol = initialCol; currentCol <= lastCol; currentCol++){
				for(currentLayer = 0; currentLayer < layers.length; currentLayer++){
					Gdx.gl.glDisable(GL10.GL_BLEND);
					cache.draw(normalCacheId[layers[currentLayer]][currentRow][currentCol]);
					Gdx.gl.glEnable(GL10.GL_BLEND);
					cache.draw(blendedCacheId[layers[currentLayer]][currentRow][currentCol]);
				}
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
