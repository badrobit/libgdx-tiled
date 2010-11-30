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

//holds map layer information
public class TiledLayer {
	private String name;

	private int width, height;
	int[][] map;

	TiledLayer(String name, int width, int height){
		this.name = name;
		this.width = width;
		this.height = height;
		map = new int[height][width];
	}
	
	/**
     * @return Layer name
     */
	public String getName() {
		return name;
	}
	
	/**
     * @return Layer width in number of tiles
     */
	public int getWidth() {
		return width;
	}
	
	/**
     * @return Layer height in number of tiles
     */
	public int getHeight() {
		return height;
	}
	
	@Override
	public String toString() {
		return new String("name \"" + name + "\" size " + width + "x" + height);
	}
}
