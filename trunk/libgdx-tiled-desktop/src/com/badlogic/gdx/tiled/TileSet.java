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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureRegion;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

//stores the tile set and associated regions
//would be better if this could load multiple files and combine them to form one large texture
public class TileSet {
	private String path;
	private int firstgid;
	private int tileWidth, tileHeight;
	private int spacing;
	private int margin;
	private FileType type;
	
	private Texture texture;
	private TextureRegion[] region;
	private int numRows, numCols, numTiles;
	
	TileSet(String path, FileType type, int tileWidth, int tileHeight, int firstgid, int spacing, int margin){
		this.path = path;
		this.firstgid = firstgid;
		this.tileHeight = tileHeight;
		this.tileWidth = tileWidth;
		this.type = type;
		this.spacing = spacing;
		this.margin = margin;
		
		loadTexture();
	}
	
	//loads textures and fills the texture region array
	private void loadTexture(){
		texture = Gdx.graphics.newTexture(Gdx.files.getFileHandle(path, type), TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
		
		//TODO: test texture with margins and spacing
		numRows = (texture.getHeight() - 2*margin)/(tileWidth + spacing);
		numCols = (texture.getWidth() - 2*margin)/(tileWidth + spacing);
		numTiles = numRows * numCols;
		
		region = new TextureRegion[numTiles];
		
		int x = margin, y = margin, tile = 0;
		for(int row = 0; row < numRows; row++){
			for(int col = 0; col < numCols; col++){
				region[tile] = new TextureRegion(texture, x, y, tileWidth, tileHeight);
				tile++;
				x += getTileWidth() + spacing;
				if(x >= texture.getWidth()){ //end of row
					x = margin;
					y += tileHeight + spacing;
				}
			}
		}
	}

	TextureRegion getRegion(int tile){
		return region[tile - firstgid];
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public String getPath() {
		return path;
	}

	public int getFirstgid() {
		return firstgid;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public int getSpacing() {
		return spacing;
	}

	public int getMargin() {
		return margin;
	}

	public FileType getType() {
		return type;
	}

	public Texture getTexture() {
		return texture;
	}
}
