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

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;

public class TiledMap {
	public ArrayList<TiledLayer> layers;
	public ArrayList<TileSet> tileSets;
	
	public final FileHandle tmxFile, baseDir;
	public String orientation;
	public int width, height, tileWidth, tileHeight;
	
	TiledMap(FileHandle tmxFile, FileHandle baseDir){
		this.tmxFile = tmxFile;
		this.baseDir = baseDir;
		
		layers = new ArrayList<TiledLayer>();
		tileSets = new ArrayList<TileSet>();
	}
	
	public void addTileSet(String imageName, int firstgid, int tileWidth, int tileHeight, int spacing, int margin) throws IOException{
		tileSets.add(new TileSet(imageName, firstgid, tileWidth, tileHeight, spacing, margin));
	}
}
