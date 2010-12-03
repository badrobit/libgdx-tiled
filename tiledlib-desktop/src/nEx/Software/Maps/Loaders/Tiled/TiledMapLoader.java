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

package nEx.Software.Maps.Loaders.Tiled;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nEx.Software.Maps.Loaders.Tiled.Objects.TiledMap;
import nEx.Software.Maps.Loaders.Tiled.Objects.TiledMapTileLayer;
import nEx.Software.Maps.Loaders.Tiled.Objects.TiledMapTileSet;
import nEx.Software.Maps.Loaders.Tiled.SafeValues.SafeValues;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;

public class TiledMapLoader
{
	public static TiledMap loadTiledMap (InputStream in)
	{
		try
		{			
            final SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            final SAXParser sp = spf.newSAXParser();
            final XMLReader xr = sp.getXMLReader();
            
            final TiledMapParser parser = new TiledMapParser();
            xr.setContentHandler(parser);

            xr.parse(new InputSource(new BufferedInputStream(in)));
            return parser.getMap();
	    }
		catch (final Exception e)
		{
			e.printStackTrace();
	    }
		return null;
	}
	
	public static class TiledMapParser extends DefaultHandler
	{
		private boolean inMap;
		private boolean inProperties;
		private boolean inProperty;
		private boolean inTileSet;
		private boolean inImage;
		private boolean inLayer;
		private boolean inData;
		private boolean inTile;
		private boolean inObjectGroup;
		private boolean inObject;
		
		private int current;
		
		private final StringBuilder mStringBuilder = new StringBuilder();
		private final ArrayList<Object> ListOfAnything = new ArrayList<Object>();
		
		private TiledMap TiledMap;
		private TiledMapTileSet TiledMapTileSet;
		private TiledMapTileLayer TiledMapTileLayer;
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
		{
			if (localName.equals("map"))
			{
				inMap = true;
				TiledMap = new TiledMap();				
				TiledMap.setVersion(attributes.getValue("version"));
				TiledMap.setOrientation(attributes.getValue("orientation"));
				TiledMap.setWidth(SafeValues.safeInt(attributes.getValue("width"), 0));
				TiledMap.setHeight(SafeValues.safeInt(attributes.getValue("height"), 0));
				TiledMap.setTileWidth(SafeValues.safeInt(attributes.getValue("tilewidth"), 0));
				TiledMap.setTileHeight(SafeValues.safeInt(attributes.getValue("tileheight"), 0));
			}
			else
			if (localName.equals("properties"))
			{
				inProperties = true;
			}
			else
			if (localName.equals("property"))
			{
				inProperty = true;

				if (inTile)
				{
					Gdx.app.log("TiledMapParser", "Got Tile Property, but we haven't loaded all of our tiles yet. smh");
				}
				else
				if (inLayer)
				{
					
				}
				else
				if (inObject)
				{

				}
				else
				if (inObjectGroup)
				{
					
				}
				else
				if (inMap)
				{
					if (TiledMap != null)
					{
						TiledMap.getProperties().put(attributes.getValue("name"), attributes.getValue("value"));
					}
				}
			}
			else
			if (localName.equals("tileset"))
			{
				inTileSet = true;
				if(attributes.getValue("source") == null)
				{
					TiledMapTileSet = new TiledMapTileSet();
					TiledMapTileSet.setName(attributes.getValue("name"));
					TiledMapTileSet.setFirstGid(SafeValues.safeInt(attributes.getValue("firstgid"), 1));
					TiledMapTileSet.setTileWidth(SafeValues.safeInt(attributes.getValue("tilewidth"), 32));
					TiledMapTileSet.setTileHeight(SafeValues.safeInt(attributes.getValue("tileheight"), 32));
					TiledMapTileSet.setTileMargin(SafeValues.safeInt(attributes.getValue("margin"), 0));
					TiledMapTileSet.setTileSpacing(SafeValues.safeInt(attributes.getValue("margin"), 0));
				}
				else
				{
					System.out.println("external tileset");
				}
			}
			else
			if (localName.equals("image"))
			{			
				inImage = true;
				if (inTileSet && TiledMapTileSet != null)
				{
					TiledMapTileSet.setImageSource(attributes.getValue("source"));
				}
				else
				{
					Gdx.app.log("TiledMapParser", "Got Image, but no TileSet...");
				}
			}		
			else
			if (localName.equals("layer"))
			{
				inLayer = true;
				TiledMapTileLayer = new TiledMapTileLayer();
				TiledMapTileLayer.setName(attributes.getValue("name"));
				TiledMapTileLayer.setWidth(SafeValues.safeInt(attributes.getValue("width"), 1));
				TiledMapTileLayer.setHeight(SafeValues.safeInt(attributes.getValue("height"), 1));
				TiledMapTileLayer.lockSize();
				current = 0;
			}
			else
			if (localName.equals("data"))
			{
				inData = true;
			}
			else
			if (localName.equals("tile"))
			{
				inTile = true;
				if (inTileSet)
				{
					
				}
				else
				if (inData)
				{					
					// Only happens when TMX file is stored as plain XML
					if (TiledMapTileLayer != null)
					{						
						final int tileX = (current % TiledMapTileLayer.getWidth());
						final int tileY = (current / TiledMapTileLayer.getWidth());

						TiledMapTileLayer.getTiles()[tileX][tileY]
						=
						TiledMap.getTiles().get
						(
							SafeValues.safeInt
							(
								attributes.getValue("width")
								,
								0
							)
						);
						current++;
					}
				}
			}
			else
			if (localName.equals("objectgroup"))
			{
				inObjectGroup = true;
			}		
			else
			if (localName.equals("object"))
			{
				inObject = true;
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (localName.equals("map"))
			{
				inMap = false;
			}
			else
			if (localName.equals("properties"))
			{
				inProperties = false;
			}
			else
			if (localName.equals("property"))
			{
				inProperty = false;
			}
			else
			if (localName.equals("tileset"))
			{
				inTileSet = false;
				if (TiledMapTileSet != null)
				{
					TiledMapTileSet.loadTileSet(TiledMap, FileType.Internal);
					TiledMapTileSet = null;
				}
			}
			else
			if (localName.equals("image"))
			{
				inImage = false;
			}		
			else
			if (localName.equals("layer"))
			{
				inLayer = false;
				if(TiledMapTileLayer != null)
				{
					Gdx.app.log("TiledMapParser", TiledMapTileLayer.getName());
					TiledMap.getLayers().add(TiledMapTileLayer);
				}
			}
			else
			if (localName.equals("data"))
			{
				inData = false;
			}
			else
			if (localName.equals("tile"))
			{
				inTile = false;
			}		
			else
			if (localName.equals("objectgroup"))
			{
				inObjectGroup = false;
			}		
			else
			if (localName.equals("object"))
			{
				inObject = false;
			}		
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException
		{
			mStringBuilder.append(ch, start, length);
		}
		
		public TiledMap getMap()
		{
			return TiledMap;
		}
	}
}
