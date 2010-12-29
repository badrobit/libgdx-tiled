package com.badlogic.gdx.tiled;

import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.TextureAtlas;
import com.badlogic.gdx.graphics.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.IntMap;

public class TileAtlas {
	private IntMap<AtlasRegion> regionsMap;
	
	public TileAtlas(TiledMap map, FileHandle packFile, FileHandle imagesDir){
		TextureAtlas textureAtlas = new TextureAtlas(packFile, imagesDir, false);
		List<AtlasRegion> atlasRegions = (List<AtlasRegion>) textureAtlas.findRegions(map.tmxFile.nameWithoutExtension());
		regionsMap = new IntMap<AtlasRegion>(atlasRegions.size());
		for(int i = 0; i < atlasRegions.size(); i++){
			regionsMap.put(atlasRegions.get(i).index, atlasRegions.get(i));
		}
	}

	public AtlasRegion getRegion(int index){
		return regionsMap.get(index);
	}
}
