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

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.TextureRegion;

public class TiledMapTileSet
{
	private
	String Name;
	public String getName()
	{
		return Name;		
	}
	public void setName(String value)
	{
		Name = value;		
	}
	
	private
	int FirstGid;
	public int getFirstGid()
	{
		return FirstGid;	
	}
	public void setFirstGid(int value)
	{
		FirstGid = value;	
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
	int TileMargin;
	public int getTileMargin()
	{
		return TileMargin;		
	}
	public void setTileMargin(int value)
	{
		TileMargin = value;		
	}
	
	private
	int TileSpacing;
	public int getTileSpacing()
	{
		return TileSpacing;		
	}
	public void setTileSpacing(int value)
	{
		TileSpacing = value;		
	}
	
	private
	String ImageSource;
	public String getImageSource()
	{
		return ImageSource;		
	}
	public void setImageSource(String value)
	{
		ImageSource = value;		
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
	
	public TiledMapTileSet()
	{
		Tiles = new HashMap<Integer, TiledMapTile>();
	}
	
	private
	boolean Loaded;
	
	private
	boolean Loading;
	
	public void loadTileSet(TiledMap map, FileType type)
	{
		if (!Loading && !Loaded)
		{
			Loading = true;
			Texture texture =
			Gdx.graphics.newTexture
			(
				Gdx.files.getFileHandle(getImageSource(), type),
				TextureFilter.Nearest, TextureFilter.Nearest,
				TextureWrap.ClampToEdge, TextureWrap.ClampToEdge
			);
			map.getTextures().add(texture);
			
			final int width = texture.getHeight();
			final int height = texture.getHeight();
			
			final int tilesX = (width - 2 * getTileMargin()) / (getTileWidth() + getTileSpacing());
			final int tilesY = (height - 2 * getTileMargin()) / (getTileHeight() + getTileSpacing());

			int id = getFirstGid();			
			int x = getTileMargin();
			int y = getTileMargin();
						
			for(int tileY = 0; tileY < tilesY; tileY++)
			{
				for(int tileX = 0; tileX < tilesX; tileX++)
				{
					TiledMapTile tile = getTiles().get(id);
					if (tile == null)
					{
						tile = new TiledMapTile();
						tile.setGlobalId(id);
					}
					id++;
					
					tile.setTextureRegion
					(
						new TextureRegion
						(
							texture
							,
							x
							,
							y
							,
							getTileWidth()
							,
							getTileHeight()
						)
					);
					x += getTileWidth() + getTileSpacing();
					if(x >= texture.getWidth())
					{
						x = getTileMargin();
						y += getTileHeight() + getTileSpacing();
					}
					map.getTiles().put(tile.getGlobalId(), tile);
				}				
			}
			getTiles().clear();
			Loaded = true;
		}
		Loading = false;
	}
}
