/*
 * Copyright (C) 2010 Justin Shapcott (support@nexsoftware.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package
nEx.Software.Maps.Loaders.Tiled.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;

public class TiledMap
{
	private
	String Version;
	public String getVersion()
	{
		return Version;		
	}
	public void setVersion(String value)
	{
		Version = value;		
	}
	
	private
	String Orientation;
	public String getOrientation()
	{
		return Orientation;		
	}
	public void setOrientation(String value)
	{
		Orientation = value;		
	}
	
	private
	int Width;
	public int getWidth()
	{
		return Width;		
	}
	public void setWidth(int value)
	{
		Width = value;		
	}
	
	private
	int Height;
	public int getHeight()
	{
		return Height;		
	}
	public void setHeight(int value)
	{
		Height = value;		
	}
	
	private
	int TileWidth;
	public int getTileWidth()
	{
		return TileWidth;		
	}
	public void setTileWidth(int value)
	{
		TileWidth = value;		
	}
	
	private
	int TileHeight;
	public int getTileHeight()
	{
		return TileHeight;		
	}
	public void setTileHeight(int value)
	{
		TileHeight = value;		
	}
	
	private
	Map<String, String> Properties
	=
	new HashMap<String, String>();
	public Map<String, String> getProperties()
	{
		return Properties;
	}
	
	private
	ArrayList<Texture> Textures;
	public ArrayList<Texture> getTextures()
	{
		return Textures;		
	}
	public void setTextures(ArrayList<Texture> value)
	{
		Textures = value;
	}

	private
	Map<Integer, TiledMapTile> Tiles;
	public Map<Integer, TiledMapTile> getTiles()
	{
		return Tiles;	
	}
	public void setTiles(Map<Integer, TiledMapTile> value)
	{
		Tiles = value;	
	}
	
	private
	List<TiledMapLayer> Layers;
	public List<TiledMapLayer> getLayers()
	{
		return Layers;	
	}
	public void setLayers(List<TiledMapLayer> value)
	{
		Layers = value;	
	}	
	
	public TiledMap()
	{
		Textures = new ArrayList<Texture>();
		Layers = new LinkedList<TiledMapLayer>();
		Properties = new HashMap<String, String>();	
		Tiles = new HashMap<Integer, TiledMapTile>();
	}
}
