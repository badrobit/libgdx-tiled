package com.badlogic.gdx.tiled;

import java.util.ArrayList;

import com.badlogic.gdx.Files.FileType;

public class TiledMap {
	public ArrayList<TiledLayer> layer;
	public ArrayList<TileSet> tileSet;
	
	public String filename;
	public String basePath;
	public FileType type;
	public int mapWidth, mapHeight, mapTileWidth, mapTileHeight;
	
	TiledMap(String filename, FileType type, String basePath){
		this.filename = filename;
		this.basePath = basePath;
		this.type = type;
		
		layer = new ArrayList<TiledLayer>();
		tileSet = new ArrayList<TileSet>();
	}
	
	void addTileSet(String name, int firstgid, int tileWidth, int tileHeight, int spacing, int margin){
		tileSet.add(new TileSet(basePath + name, type, firstgid, tileWidth, tileHeight, spacing, margin));
	}
}
