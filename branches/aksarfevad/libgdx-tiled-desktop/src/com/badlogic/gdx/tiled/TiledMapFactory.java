/*
 * Copyright 2010 David Fraska (dfraska@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.badlogic.gdx.tiled;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Base64Coder;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class TiledMapFactory extends DefaultHandler{
	
	private TiledMap map;
	
	private int state;
	private int currentLayer;
	private int currentTileSet;
	
	//define states
	private static final int LOADING = 0;
	private static final int DATA = 1;
	private static final int DONE = 2;
	
	private int firstgid, tileWidth, tileHeight, margin, spacing;
	private String dataString, encoding, compression;
	private byte[] data;
	
	//TODO: add object loading
	
	public TiledMapFactory(){
	}
	
	public TiledMap createMap(String filename, String basePath, FileType type){
		init();
		
		map = new TiledMap(filename, type, basePath);
		
		System.out.println("Loading tilemap from " + filename);
		FileHandle handle = Gdx.files.getFileHandle(filename, FileType.Internal);
		
		SAXParser parser = null;
		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(new InputSource(handle.read()), this);
		} catch (ParserConfigurationException e) {
			throw new GdxRuntimeException("Error Parsing TMX file - Parser Configuration Exception: " + e.getMessage());
		} catch (SAXException e) {
			throw new GdxRuntimeException("Error Parsing TMX file - SAX Exception: " + e.getMessage());
		} catch (IOException e) {
			throw new GdxRuntimeException("Error Parsing TMX file - IOException: " + e.getMessage());
		}
		
		return map;
	}
	
	private void init() {
		state = LOADING;
		currentLayer = 0;
		currentTileSet = 0;
	}

	static int parseIntWithDefault(String string, int defaultValue){
		try{
			return Integer.parseInt(string);
		}
		catch (NumberFormatException e){
			return defaultValue;
		}
	}
	
	@Override
	public void startDocument() {
		System.out.println("Start document");
	}
	
	@Override
	public void startElement(String uri, String name, String qName, Attributes attr) {
		System.out.println("Start element: " + qName);
		
		if("layer".equals(qName)){
			String layerName = attr.getValue("name");
			int layerWidth = parseIntWithDefault(attr.getValue("width"), 0);
			int layerHeight = parseIntWithDefault(attr.getValue("height"), 0);
			
			
			
			map.layer.add(new TiledLayer(layerName, layerWidth, layerHeight));
			return;
		}
		
		if("data".equals(qName)){	
			encoding = attr.getValue("encoding");
			compression = attr.getValue("compression");
			dataString = ""; //clear the string for new data
			state = DATA;
			return;
		}
				
		if("tileset".equals(qName)){
			firstgid = parseIntWithDefault(attr.getValue("firstgid"), 1);
			tileWidth = parseIntWithDefault(attr.getValue("tilewidth"), 0);
			tileHeight = parseIntWithDefault(attr.getValue("tileheight"), 0);
			spacing = parseIntWithDefault(attr.getValue("spacing"), 0);
			margin = parseIntWithDefault(attr.getValue("margin"), 0);
			return;
		}
		
		if("image".equals(qName)){
			map.addTileSet(attr.getValue("source"), tileWidth, tileHeight, firstgid, spacing, margin);
			return;
		}
		
		if("map".equals(qName)){
			if(!"orthogonal".equals(attr.getValue("orientation"))){ // orientation != orthogonal
				throw new GdxRuntimeException("Only orthogonal maps are currently supported.");
			}
			map.mapWidth = parseIntWithDefault(attr.getValue("width"), 0);
			map.mapHeight = parseIntWithDefault(attr.getValue("height"), 0);
			map.mapTileWidth = parseIntWithDefault(attr.getValue("tilewidth"), 0);
			map.mapTileHeight = parseIntWithDefault(attr.getValue("tileheight"), 0);
			return;
		}
	}
	
	//TODO: no checking is done to make sure that an element has actually started.
	//Currently this may cause strange results if the XML file is malformed
	@Override
	public void endElement(String uri, String name, String qName) {
		System.out.println("End element: " + qName);
				
		if("data".equals(qName)){
    		if(dataString == null | "".equals(dataString)) return;
    		
			//decode the data
			if("base64".equals(encoding)){ //encoding != base64
				data = Base64Coder.decode(dataString.trim());
			} else {
				throw new GdxRuntimeException("Unsupported encoding, only base64 supported");
			}
			
			//uncompress the data
			if("gzip".equals(compression)){
				unGZip();
			} else {
				throw new GdxRuntimeException("Unsupported compression, only gzip supported");
			}
			
			state = LOADING;
			return;
		}
		
		if("layer".equals(qName)){
			System.out.println("Added Layer: " + currentLayer);
			currentLayer++;
			return;
		}
		
		if("tileset".equals(qName)){
			System.out.println("Added TileSet");
			currentTileSet++;
			return;
		}
		
		if("image".equals(qName)){
			return;
		}
	}
	
	private void unGZip(){
		GZIPInputStream GZIS = null;
		try {
			GZIS = new GZIPInputStream(new ByteArrayInputStream(data), data.length);
		} catch (IOException e) {
			throw new GdxRuntimeException("Error Reading TMX Layer Data - IOException: " + e.getMessage());
		} finally {
			//Read the GZIS data into an array, 4 bytes = 1 GID
			byte[] readTemp = new byte[4];
			//see http://sourceforge.net/apps/mediawiki/tiled/index.php?title=Examining_the_map_format
			for(int row = 0; row < map.layer.get(currentLayer).height; row++){
				for(int col = 0; col < map.layer.get(currentLayer).width; col++){
					try {
						GZIS.read(readTemp, 0, 4);
						map.layer.get(currentLayer).map[row][col] = readTemp[0] |  readTemp[1] << 8 | readTemp[2] << 16 | readTemp[3] << 24;
						System.out.print(map.layer.get(currentLayer).map[row][col]);
					} catch (IOException e) {
						throw new GdxRuntimeException("Error Reading TMX Layer Data - IOException: " + e.getMessage());
					}
				}
				System.out.println();
			}
		}
	} 
	
	@Override
	public void endDocument() {
		System.out.println("End document");
		state = DONE;
	}
	
	@Override
    public void characters (char ch[], int start, int length)
    {
    	switch(state){
    	case DATA:
    		dataString = dataString.concat(String.copyValueOf(ch, start, length));
    	break;
    	default:
    	break;
    	}
    }
}
