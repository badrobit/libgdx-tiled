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
	private String dataString;
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
			//encoding="base64" compression="gzip"
			if(!"base64".equals(attr.getValue("encoding"))){ //encoding != base64
				throw new GdxRuntimeException("Only base64 encoding is supported");
			}
			if(!"gzip".equals(attr.getValue("compression"))){ //compression != gzip
				throw new GdxRuntimeException("Only gzip compression is supported");
			}
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
    		//TODO: instead of uncompressing here, do so in endElement.
    		//All characters between data beginning and ending should be
    		//concatenated, then trimmed immediately before uncompressing.
    		//This will clean up code and allow for the entire map not coming in one
    		//call to characters, better fitting the SAX spec.
    		
    		GZIPInputStream GZIS = null;
    		dataString = String.copyValueOf(ch, start, length).trim();
    		System.out.println("Data: \"" + dataString + "\"");
    		
    		if(dataString == null | "".equals(dataString)) break;
    		
    		//decode the data as Base64
    		data = Base64Coder.decode(dataString);
    		try {
    			//Unzip the data
				GZIS = new GZIPInputStream(new ByteArrayInputStream(data), data.length);
			} catch (IOException e) {
				throw new GdxRuntimeException("Error Reading TMX Layer Data - IOException: " + e.getMessage());
				
			} finally {
				//Read the GZIS data into an array, 4 bytes = 1 GID
				byte[] readTemp = new byte[4];
				//see http://sourceforge.net/apps/mediawiki/tiled/index.php?title=Examining_the_map_format
				for(int row = 0; row < map.layer.get(currentLayer).getHeight(); row++){
					for(int col = 0; col < map.layer.get(currentLayer).getWidth(); col++){
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
    	break;
    	default:
    	break;
    	}
    }
}
