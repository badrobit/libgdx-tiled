package com.badlogic.gdx.tiled;

import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.TextureAtlas;
import com.badlogic.gdx.graphics.TextureAtlas.AtlasRegion;

public class TileAtlas {
	private TextureAtlas textureAtlas;
	private List<AtlasRegion> atlasRegions;
	
	public TileAtlas(TiledMap map, FileHandle packFile, FileHandle imagesDir){
		textureAtlas = new TextureAtlas(packFile, imagesDir, false);
		atlasRegions = (List<AtlasRegion>) textureAtlas.findRegions(map.tmxFile.nameWithoutExtension());
	}

	public AtlasRegion getRegion(int index){
		for(AtlasRegion region: atlasRegions){
			if(region.index == index) return region;
		}
		return null;
	}
}
