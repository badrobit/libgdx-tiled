package com.badlogic.gdx.tiled;

import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.IntMap;

/** Contains an atlas of tiles by tile id for use with {@link TiledMapRenderer} */
public class TileAtlas {
	private IntMap<AtlasRegion> regionsMap;
	
	/** Creates a TileAtlas for use with {@link TiledMapRenderer} */
	public TileAtlas(TiledMap map, FileHandle packFile, FileHandle imagesDir){
		TextureAtlas textureAtlas = new TextureAtlas(packFile, imagesDir, false);
		List<AtlasRegion> atlasRegions = (List<AtlasRegion>) textureAtlas.findRegions(map.tmxFile.nameWithoutExtension());
		regionsMap = new IntMap<AtlasRegion>(atlasRegions.size());
		for(int i = 0; i < atlasRegions.size(); i++){
			regionsMap.put(atlasRegions.get(i).index, atlasRegions.get(i));
		}
	}

	/** Gets an {@link AtlasRegion} for a tile id
	 * @param id tile id
	 * @return the {@link AtlasRegion}
	 * */
	public AtlasRegion getRegion(int id){
		return regionsMap.get(id);
	}
}
