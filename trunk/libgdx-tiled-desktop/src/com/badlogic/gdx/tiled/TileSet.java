package com.badlogic.gdx.tiled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureRegion;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

//stores the tile set and associated regions
//would be better if this could load multiple files and combine them to form one large texture
public class TileSet {
	String path;
	int firstgid;
	int tileWidth;
	int tileHeight;
	int spacing;
	int margin;
	FileType type;
	
	Texture texture;
	TextureRegion[] region;
	private int numRows, numCols, numTiles;
	
	TileSet(String path, FileType type, int tileWidth, int tileHeight, int firstgid, int spacing, int margin){
		this.path = path;
		this.firstgid = firstgid;
		this.tileHeight = tileHeight;
		this.tileWidth = tileWidth;
		this.type = type;
		this.spacing = spacing;
		this.margin = margin;
		
		loadTexture();
	}
	
	//loads textures and fills the texture region array
	void loadTexture(){
		texture = Gdx.graphics.newTexture(Gdx.files.getFileHandle(path, type), TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
		
		//TODO: test texture with margins and spacing
		numRows = (texture.getHeight() - 2*margin)/(tileWidth + spacing);
		numCols = (texture.getWidth() - 2*margin)/(tileWidth + spacing);
		numTiles = numRows * numCols;
		
		region = new TextureRegion[numTiles];
		
		int x = margin, y = margin, tile = 0;
		for(int row = 0; row < numRows; row++){
			for(int col = 0; col < numCols; col++){
				region[tile] = new TextureRegion(texture, x, y, tileWidth, tileHeight);
				tile++;
				x += tileWidth + spacing;
				if(x >= texture.getWidth()){ //end of row
					x = margin;
					y += tileHeight + spacing;
				}
			}
		}
	}

	TextureRegion getRegion(int tile){
		return region[tile - firstgid];
	}
}
