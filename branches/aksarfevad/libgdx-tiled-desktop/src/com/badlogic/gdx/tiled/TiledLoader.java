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
import java.util.LinkedList;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Base64Coder;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class TiledLoader extends DefaultHandler{
	
	private TiledMap map;
	
	private int state;
	private TiledLayer currentLayer;
	private TileSet currentTileSet;
	private TiledObjectGroup currentObjectGroup;
	private TiledObject currentObject;
	private int currentTile;
	
	//define states
	private static final int INIT = 0;
	private static final int DATA = 1;
	private static final int DONE = 2;
	
	LinkedList<String> currentBranch = new LinkedList<String>();
	
	private int firstgid, tileWidth, tileHeight, margin, spacing;
	private String dataString, encoding, compression;
	private byte[] data;
	
	public TiledLoader(){
	}
	
	public TiledMap createMap(FileHandle tmxFile, FileHandle baseDir){
		state = INIT;
		
		map = new TiledMap();
		map.tmxFile = tmxFile;
		map.baseDir = baseDir;
		
		SAXParser parser = null;
		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(new InputSource(tmxFile.read()), this);
		} catch (ParserConfigurationException e) {
			throw new GdxRuntimeException("Error Parsing TMX file - Parser Configuration Exception: " + e.getMessage());
		} catch (SAXException e) {
			throw new GdxRuntimeException("Error Parsing TMX file - SAX Exception: " + e.getMessage());
		} catch (IOException e) {
			throw new GdxRuntimeException("Error Parsing TMX file - IOException: " + e.getMessage());
		}
		
		return map;
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

	}
	
	@Override
	public void startElement(String uri, String name, String qName, Attributes attr) {
		currentBranch.push(qName);
		
		if("layer".equals(qName)){
			String layerName = attr.getValue("name");
			int layerWidth = parseIntWithDefault(attr.getValue("width"), 0);
			int layerHeight = parseIntWithDefault(attr.getValue("height"), 0);
			
			currentLayer = new TiledLayer(layerName, layerWidth, layerHeight);
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
		
		if("objectgroup".equals(qName)){
			currentObjectGroup = new TiledObjectGroup();
			currentObjectGroup.name = attr.getValue("name");
			currentObjectGroup.height = parseIntWithDefault(attr.getValue("height"), 0);
			currentObjectGroup.width = parseIntWithDefault(attr.getValue("width"), 0);
			return;
		}
		
		if("object".equals(qName)){
			currentObject = new TiledObject();
			currentObject.name = attr.getValue("name");
			currentObject.type = attr.getValue("type");
			currentObject.x = parseIntWithDefault(attr.getValue("x"), 0);
			currentObject.y = parseIntWithDefault(attr.getValue("y"), 0);
			currentObject.width = parseIntWithDefault(attr.getValue("width"), 0);
			currentObject.height = parseIntWithDefault(attr.getValue("height"), 0);
			return;
		}
		
		if("image".equals(qName)){
			try {
				currentTileSet = new TileSet(attr.getValue("source"), tileWidth, tileHeight, firstgid, spacing, margin);
			} catch (IOException e) {
				throw new RuntimeException("Error Parsing TMX file: Image " + attr.getValue("source") + " could not be read");
			}
			return;
		}
		
		if("map".equals(qName)){
			map.orientation = attr.getValue("orientation");
			map.width = parseIntWithDefault(attr.getValue("width"), 0);
			map.height = parseIntWithDefault(attr.getValue("height"), 0);
			map.tileWidth = parseIntWithDefault(attr.getValue("tilewidth"), 0);
			map.tileHeight = parseIntWithDefault(attr.getValue("tileheight"), 0);
			return;
		}
		
		if("tile".equals(qName)){
			currentTile = parseIntWithDefault(attr.getValue("id"), 0);
			return;
		}
		
		if("property".equals(qName)){
			String parentType = currentBranch.get(2);
			putProperty(parentType, attr.getValue("name"), attr.getValue("value"));
			return;
		}
		
		if("properties".equals(qName)){
			return;
		}
		
		System.out.println("Start element for " + qName + " unhandled");
	}
	
	private void putProperty(String parentType, String name, String value){
		if("tile".equals(parentType)){
			map.addTileProperty(currentTile, name, value);
			return;
		}
		
		if("map".equals(parentType)){
			map.properties.put(name, value);
			return;
		}
		
		if("layer".equals(parentType)){
			currentLayer.properties.put(name, value);
			return;
		}
		
		if("objectgroup".equals(parentType)){
			currentObjectGroup.properties.put(name, value);
			return;
		}
		
		if("object".equals(parentType)){
			currentObject.properties.put(name, value);
			return;
		}
		
		System.out.println("Property for " + parentType + " unhandled");
	}
	
	//FIXME: no checking is done to make sure that an element has actually started.
	//Currently this may cause strange results if the XML file is malformed
	@Override
	public void endElement(String uri, String name, String qName) {
		currentBranch.pop();
		
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
			
			state = INIT;
			return;
		}
		
		if("layer".equals(qName)){
			map.layers.add(currentLayer);
			currentLayer = null;
			return;
		}
		
		if("tileset".equals(qName)){
			map.tileSets.add(currentTileSet);
			currentTileSet = null;
			return;
		}
		
		if("objectgroup".equals(qName)){
			map.objectGroups.add(currentObjectGroup);
			currentObjectGroup = null;
			return;
		}
		
		if("object".equals(qName)){
			currentObjectGroup.addObject(currentObject);
			currentObject = null;
			return;
		}
		
		if("image".equals(qName)){
			return;
		}
		
		if("map".equals(qName)){
			return;
		}
		
		if("tile".equals(qName)){
			return;
		}
		
		if("property".equals(qName)){
			return;
		}
		
		if("properties".equals(qName)){
			return;
		}
		
		System.out.println("End element for " + qName + " unhandled");
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
			for(int row = 0; row < currentLayer.height; row++){
				for(int col = 0; col < currentLayer.width; col++){
					try {
						GZIS.read(readTemp, 0, 4);
						currentLayer.tile[row][col] = readTemp[0] |  readTemp[1] << 8 | readTemp[2] << 16 | readTemp[3] << 24;
					} catch (IOException e) {
						throw new GdxRuntimeException("Error Reading TMX Layer Data - IOException: " + e.getMessage());
					}
				}
			}
		}
	} 
	
	@Override
	public void endDocument() {
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
