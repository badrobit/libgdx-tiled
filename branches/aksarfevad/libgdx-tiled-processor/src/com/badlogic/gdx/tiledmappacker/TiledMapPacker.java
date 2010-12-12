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

package com.badlogic.gdx.tiledmappacker;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.imagepacker.TexturePacker;
import com.badlogic.gdx.imagepacker.TexturePacker.Settings;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.tiled.TileSet;
import com.badlogic.gdx.tiled.TiledLayer;
import com.badlogic.gdx.tiled.TiledLoader;
import com.badlogic.gdx.tiled.TiledMap;

public class TiledMapPacker {
	
	private TexturePacker packer;
	private TiledLoader loader;
	private TiledMap map;
	
	private File outputDir;
	private FileHandle tmxFileHandle;
	private FileHandle imageDirHandle;
	
	TiledMapPacker(){
		loader = new TiledLoader();
	}
	
	public void processMap(File tmxFile, File imageDir, File outputDir, Settings settings) throws IOException{
		this.outputDir = outputDir;
		
		tmxFileHandle = Gdx.files.absolute(tmxFile.getAbsolutePath());
		imageDirHandle = Gdx.files.absolute(imageDir.getAbsolutePath());
		Gdx.files.absolute(outputDir.getAbsolutePath());
		
		map = loader.createMap(tmxFileHandle, imageDirHandle);
		
		packMap(map, settings);
	}
	
	private void packMap(TiledMap map, Settings settings) throws IOException{
		packer = new TexturePacker(settings);
		
		BufferedImage tile;
		Vector2 tileLocation;
		TileSetLayout packerTileSet;
		Graphics g;
		
		ArrayList<Integer> tilesOnMap = getTilesOnMap(map);
		
		for(int i = 0; i < tilesOnMap.size(); i++){
			packerTileSet = getTileSetLayout(tilesOnMap.get(i));
			tileLocation = packerTileSet.getLocation(tilesOnMap.get(i));
			tile = new BufferedImage(packerTileSet.tileSet.tileWidth, packerTileSet.tileSet.tileHeight, BufferedImage.TYPE_4BYTE_ABGR);
			
			g = tile.createGraphics();
			g.drawImage(packerTileSet.image, 0, 0, packerTileSet.tileSet.tileWidth, packerTileSet.tileSet.tileHeight, (int)tileLocation.x, (int)tileLocation.y, (int)tileLocation.x + packerTileSet.tileSet.tileWidth, (int)tileLocation.y + packerTileSet.tileSet.tileHeight, null);
			
			packer.addImage(tile, map.tmxFile.nameWithoutExtension() + "_" + tilesOnMap.get(i));
		}
		
		packer.process(outputDir, new File(outputDir, "packfile"), tmxFileHandle.nameWithoutExtension());
	}
	
	private TileSetLayout getTileSetLayout(int tileNum) throws IOException{
		int firstgid = 0;
		int lastgid;
		
		for(TileSet set: map.tileSets){
			TileSetLayout layout = new TileSetLayout(set, imageDirHandle);
			firstgid = set.firstgid;
			lastgid = firstgid + layout.numTiles - 1;
			if(tileNum >= firstgid && tileNum <= lastgid){
				return layout;
			}
		}
	
		return null;
	}
	
	private ArrayList<Integer> getTilesOnMap(TiledMap map){
		ArrayList<Integer> tileList = new ArrayList<Integer>();
		
		for(TiledLayer layer: map.layers){
			for(int row = 0; row < layer.height; row++){
				for(int col = 0; col < layer.width; col++){
					if(!tileList.contains(layer.tile[row][col])){
							if(layer.tile[row][col] != 0) tileList.add(layer.tile[row][col]);
					}
				}
			}
		}
		
		return tileList;
	}
	
	public static void main( String[] args ){
		File tmxFile, baseDir, outputDir;
		
		Settings settings = new Settings();
		settings.padding = 1;
		
		//Create a new JoglApplication so that Gdx stuff works properly
		new JoglApplication(new ApplicationListener() {
			@Override public void create(){} @Override public void dispose(){}
			@Override public void pause(){}	@Override public void render(){}
			@Override public void resize(int width, int height){}
			@Override public void resume(){}}, "", 0, 0, false);
		
		TiledMapPacker packer = new TiledMapPacker();
		
		if (args.length != 3) {
			System.out.println("Usage: TMXFILE BASEDIR OUTPUTDIR");
			return;
		}
		
		tmxFile = new File(args[0]);
		baseDir = new File(args[1]);
		outputDir = new File(args[2]);
		
		if(!baseDir.exists()){
			System.out.println("Base directory does not exist");
			return;
		}
		if(!tmxFile.exists()){
			System.out.println("TMX file does not exist");
			return;
		}
		
		try {
			packer.processMap(tmxFile, baseDir, outputDir, settings);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return;
		}
		
		System.exit(0);
	}
}
