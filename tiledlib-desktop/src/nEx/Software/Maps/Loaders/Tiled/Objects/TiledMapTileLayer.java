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

package nEx.Software.Maps.Loaders.Tiled.Objects;

public class TiledMapTileLayer extends TiledMapLayer
{
	private
	TiledMapTile[][] Tiles;
	public TiledMapTile[][] getTiles()
	{
		return Tiles;		
	}
	public void setTiles(TiledMapTile[][] value)
	{
		Tiles = value;
	}
	
	private boolean locked;
	
	public TiledMapTileLayer()
	{
		super();		
	}
	public TiledMapTileLayer(int width, int height)
	{
		super();
		locked = true;
		setWidth(width);
		setHeight(height);
		Tiles = new TiledMapTile[width][height];		
	}

	public void lockSize()
	{
		if (!locked)
		{
			if (getWidth() > 0 && getHeight() > 0)
			{
				Tiles = new TiledMapTile[getWidth()][getHeight()];
			}			
		}
	}
}
