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

import com.badlogic.gdx.Files.FileType;

public class TiledMap {
	public ArrayList<TiledLayer> layer;
	public ArrayList<TileSet> tileSet;
	
	public String filename;
	public String basePath;
	public FileType type;
	public int mapWidth, mapHeight, mapTileWidth, mapTileHeight;
	
	TiledMap(String filename, FileType type, String basePath){
		this.filename = filename;
		this.basePath = basePath;
		this.type = type;
		
		layer = new ArrayList<TiledLayer>();
		tileSet = new ArrayList<TileSet>();
	}
	
	void addTileSet(String name, int firstgid, int tileWidth, int tileHeight, int spacing, int margin){
		tileSet.add(new TileSet(basePath + name, type, firstgid, tileWidth, tileHeight, spacing, margin));
	}
}
