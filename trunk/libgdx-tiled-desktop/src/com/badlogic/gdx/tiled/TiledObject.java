package com.badlogic.gdx.tiled;

import java.util.HashMap;

/** Contains a Tiled map object */
public class TiledObject {
	public String name, type;
	public int x, y, width, height;
	
	/** Contains the object properties with a key of the property name. */
	public HashMap<String, String> properties = new HashMap<String, String>();
}
