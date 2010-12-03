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

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nEx.Software.Maps.Loaders.Tiled.Objects.TiledMap;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class TiledMapTileSetLoader
{
	public static void loadTiledMap (InputStream in)
	{
		try
		{
            final SAXParserFactory spf = SAXParserFactory.newInstance();
            final SAXParser sp = spf.newSAXParser();

            final XMLReader xr = sp.getXMLReader();
            final TiledMapTileSetParser tmxParser = new TiledMapTileSetParser();
            xr.setContentHandler(tmxParser);

            xr.parse(new InputSource(new BufferedInputStream(in)));
	    }
		catch (final Exception e)
		{
			e.printStackTrace();
	    }
	}
	
	public static class TiledMapTileSetParser extends DefaultHandler
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
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
		{
			if (qName.equals("map"))
			{
				inMap = true;
				TiledMap = new TiledMap();
			}
			else
			if (qName.equals("properties"))
			{
				inProperties = true;
			}
			else
			if (qName.equals("property"))
			{
				inProperty = true;

				if (inTile)
				{

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
			if (qName.equals("tileset"))
			{
				inTileSet = true;
				if(attributes.getValue("source") == null)
				{
					System.out.println("internal tileset");
				}
				else
				{
					System.out.println("external tileset");
				}
			}
			else
			if (qName.equals("image"))
			{			
				inImage = true;
			}		
			else
			if (qName.equals("layer"))
			{
				inLayer = true;			
			}
			else
			if (qName.equals("data"))
			{
				inData = true;
			}
			else
			if (qName.equals("tile"))
			{
				inTile = true;
				if (inTileSet)
				{
					
				}
				else
				if (inData)
				{

				}
			}
			else
			if (qName.equals("objectgroup"))
			{
				inObjectGroup = true;
			}		
			else
			if (qName.equals("object"))
			{
				inObject = true;
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (qName.equals("map"))
			{
				inMap = false;
			}
			else
			if (qName.equals("properties"))
			{
				inProperties = false;
			}
			else
			if (qName.equals("property"))
			{
				inProperty = false;
			}
			else
			if (qName.equals("tileset"))
			{
				inTileSet = false;
			}
			else
			if (qName.equals("image"))
			{
				inImage = false;
			}		
			else
			if (qName.equals("layer"))
			{
				inLayer = false;
			}
			else
			if (qName.equals("data"))
			{
				inData = false;
			}
			else
			if (qName.equals("tile"))
			{
				inTile = false;
			}		
			else
			if (qName.equals("objectgroup"))
			{
				inObjectGroup = false;
			}		
			else
			if (qName.equals("object"))
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
