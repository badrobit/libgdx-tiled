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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.SpriteCache;
import com.badlogic.gdx.graphics.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

public class TiledLayerSpriteCache {
	private SpriteCache cache;
	private int normalCacheId[][];//, blendedCacheId[][];
	//will need 2 cache Ids for each area chunk (block), one for normal and one for blended
	//Ideally, blocks should be just large enough so that at most 4 are visible at a time
	
	private int tilesPerBlockRow, tilesPerBlockCol;
	private int blocksPerLayerRow, blocksPerLayerCol;
	
	private TiledLayer layer;
	private TileSet tileSet;
	
	private int layerWidthPixels, layerHeightPixels;
	
    /**
     * Draws a Tiled layer using a Sprite Cache
     * @param layer The layer to be drawn
     * @param tileSet The tile set used to draw this layer - note only one tile set per Tiled layer
     * @param blockWidth The width of each block to be drawn, in number of tiles: should fill half the screen width
     * @param blockHeight The width of each block to be drawn, in number of tiles: should fill half the screen height
     */
	public TiledLayerSpriteCache(TiledLayer layer, TileSet tileSet, int blockWidth, int blockHeight) {
		//TODO: add a constructor that takes a ShaderProgram for OpenGL ES 2
		this.layer = layer;
		this.tileSet = tileSet;
		tilesPerBlockRow = blockHeight;
		tilesPerBlockCol = blockWidth;
		
		layerHeightPixels = layer.getHeight() * tileSet.getTileHeight();
		layerWidthPixels = layer.getWidth() * tileSet.getTileWidth();
		
		blocksPerLayerCol = (int) Math.ceil((float)layer.getWidth()/(float)blockWidth);
		blocksPerLayerRow = (int) Math.ceil((float)layer.getHeight()/(float)blockHeight);
		
		normalCacheId = new int[blocksPerLayerRow][blocksPerLayerCol];
		
		cache = new SpriteCache(layer.getHeight()*layer.getWidth() ,false);
		//FIXME: Don't really need a cache that holds all tiles,
		//really only need room for all VISIBLE tiles.
		//If using compiled TMX format, compute this during that phase
		//otherwise, count the number of non-zero tiles
		
		for(int row = 0; row < blocksPerLayerRow; row++){
			for(int col = 0; col < blocksPerLayerCol; col++){
				normalCacheId[row][col] = addBlock(row, col);
			}
		}
		
	}
	
	private int addBlock(int blockRow, int blockCol){
		int tile;
		cache.beginCache();
		float u, u2, v, v2;
		
		int tileRow = blockRow*tilesPerBlockRow;
		int tileCol = blockCol*tilesPerBlockCol;
		
		float x = tileCol*tileSet.getTileWidth();
		float y = tileRow*tileSet.getTileHeight();
		
		for(int row = 0; row < tilesPerBlockRow && tileRow < (layer.getHeight() - 1); row++){
			for(int col = 0; col < tilesPerBlockCol && tileCol < (layer.getWidth() - 1); col++){
				tile = layer.map[layer.getHeight() - tileRow - 1][tileCol];
				if(tile != 0){
					TextureRegion region = tileSet.getRegion(tile);
					u = (float)region.x/(float)tileSet.getTexture().getWidth();
					v2 = (float)region.y/(float)tileSet.getTexture().getHeight();
					u2 = (float)(region.x + region.width)/(float)tileSet.getTexture().getWidth();
					v = (float)(region.y + region.height)/(float)tileSet.getTexture().getHeight();
					cache.add(tileSet.getTexture(), x, y, tileSet.getTileWidth(), tileSet.getTileHeight(), u, v, u2, v2, Color.WHITE.toFloatBits());
				}
				x += tileSet.getTileWidth();
				tileCol++;
			}
			y += tileSet.getTileHeight();
			tileCol = blockCol*tilesPerBlockCol;
			x = tileCol*tileSet.getTileWidth();
			tileRow++;
		}
		
		return cache.endCache();
	}
	
	//TODO: add render funtions that accept a shaderprogram for GLES 2 rendering
	
	//This function should not be used most of the time. Use render(int x, int y, int width, int height) instead.
	int renderRow, renderCol;
	public void render() {
		cache.begin();
		for(renderRow = 0; renderRow < blocksPerLayerRow; renderRow++){
			for(renderCol = 0; renderCol < blocksPerLayerCol; renderCol++){
				cache.draw(normalCacheId[renderRow][renderCol]);
			}
		}
		//TODO: cache.draw(cacheID); for each row, col 
		cache.end();	
	}
	
	//private int firstRow, firstCol, lastRow, lastCol;
	
	public void render(int x, int y, int width, int height) {
		//TODO: only render visible blocks
		cache.begin();
		for(renderRow = 0; renderRow < blocksPerLayerRow; renderRow++){
			for(renderCol = 0; renderCol < blocksPerLayerCol; renderCol++){
				cache.draw(normalCacheId[renderRow][renderCol]);
			}
		}
		cache.end();
	}
	
	public Matrix4 getProjectionMatrix(){
		return cache.getProjectionMatrix();
	}
	
	public Matrix4 getTransformMatrix(){
		return cache.getTransformMatrix();
	}
	
	public int getLayerHeightPixels() {
		return layerHeightPixels;
	}

	public int getLayerWidthPixels() {
		return layerWidthPixels;
	}

	int getRow(int worldY){
		if(worldY < 0) return 0;
		if(worldY > layerHeightPixels) return tileSet.getTileHeight() - 1;
		return worldY/tileSet.getTileHeight();
	}
	
	int getCol(int worldX){
		if(worldX < 0) return 0;
		if(worldX > layerWidthPixels) return tileSet.getTileWidth() - 1;
		return worldX/tileSet.getTileWidth();
	}
	
	void dispose() {
		cache.dispose();
	}
}
