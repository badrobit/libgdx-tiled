package com.badlogic.gdx.tiled;


//holds map layer information
public class TiledLayer {
	String name;
	int width, height;
	int[][] map;

	TiledLayer(String name, int width, int height){
		this.width = width;
		this.height = height;
		map = new int[height][width];
	}
	
	@Override
	public String toString() {
		return new String("name \"" + name + "\" size " + width + "x" + height);
	}
}
