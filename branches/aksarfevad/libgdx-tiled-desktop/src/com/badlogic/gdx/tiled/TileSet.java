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

//stores the tile set and associated regions
//would be better if this could load multiple files and combine them to form one large texture
public class TileSet {
	public final int firstgid;
	public final int tileWidth, tileHeight;
	public final int spacing;
	public final int margin;
	public final String imageName;
	
	protected TileSet(String imageName, int tileWidth, int tileHeight, int firstgid, int spacing, int margin) throws IOException{
		this.imageName = imageName;
		this.firstgid = firstgid;
		this.tileHeight = tileHeight;
		this.tileWidth = tileWidth;
		this.spacing = spacing;
		this.margin = margin;
		
		//TODO: get blending from tmx file
	}
}
