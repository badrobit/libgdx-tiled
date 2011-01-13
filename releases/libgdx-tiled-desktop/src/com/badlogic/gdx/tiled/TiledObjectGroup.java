package com.badlogic.gdx.tiled;

import java.util.ArrayList;
import java.util.HashMap;

/** Contains a Tiled map object group/layer */
public class TiledObjectGroup {
	public int width, height;
	public String name;
	
	/** Contains the object group properties with a key of the property name. */
	public HashMap<String,String> properties = new HashMap<String,String>();
	
	public ArrayList<TiledObject> objects = new ArrayList<TiledObject>();
}
