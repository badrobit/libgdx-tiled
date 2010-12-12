package com.badlogic.gdx.tiled;

import java.util.ArrayList;
import java.util.HashMap;

public class TiledObjectGroup {
	public int width, height;
	public String name;
	public HashMap<String,String> properties = new HashMap<String,String>();
	
	public ArrayList<TiledObject> objects = new ArrayList<TiledObject>();
	
	public void addObject(TiledObject obj){
		objects.add(obj);
	}
	
	public void addObject(String name, String type, int x, int y, int width, int height){
		TiledObject obj = new TiledObject();
		objects.add(obj);
	}
}
