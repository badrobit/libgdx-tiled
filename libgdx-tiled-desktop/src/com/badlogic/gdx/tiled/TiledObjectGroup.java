package com.badlogic.gdx.tiled;

import java.util.ArrayList;
import java.util.HashMap;

public class TiledObjectGroup {
	public int width, height;
	public String name;
	public HashMap<String,String> properties = new HashMap<String,String>();
	
	public ArrayList<TiledObject> objects = new ArrayList<TiledObject>();
}
