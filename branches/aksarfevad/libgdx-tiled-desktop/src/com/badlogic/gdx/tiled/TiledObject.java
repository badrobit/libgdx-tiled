package com.badlogic.gdx.tiled;

import java.util.HashMap;

public class TiledObject {
	public String name, type;
	public int x, y, width, height;
	public HashMap<String, String> properties = new HashMap<String, String>();
	
	TiledObject(){
	}
	
	TiledObject(String name, String type, int x, int y, int width, int height){
		this.name = name;
		this.type = type;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
