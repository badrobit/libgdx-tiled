package com.badlogic.gdx.tiledmappacker;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.tiled.TileSet;

public class TileSetLayout{

	public final BufferedImage image;
	public final Vector2[] imageTilePositions;
	public final int numRows, numCols, numTiles;
	public final TileSet tileSet;
	
	TileSetLayout(TileSet tileSet, FileHandle baseDir) throws IOException{
		this.tileSet = tileSet;
		image = ImageIO.read(baseDir.child(tileSet.imageName).read());
		
		numRows = (image.getHeight() - 2*tileSet.margin)/(tileSet.tileHeight + tileSet.spacing);
		numCols = (image.getWidth() - 2*tileSet.margin)/(tileSet.tileWidth + tileSet.spacing);
		numTiles = numRows * numCols;
		
		imageTilePositions = new Vector2[numTiles];
		
		//fill the tile regions
		int x = tileSet.margin, y = tileSet.margin, tile = 0;
		for(int row = 0; row < numRows; row++){
			for(int col = 0; col < numCols; col++){
				imageTilePositions[tile] = new Vector2(x,y);
				tile++;
				x += tileSet.tileWidth + tileSet.spacing;
				if(x >= image.getWidth()){ //end of row
					x = tileSet.margin;
					y += tileSet.tileHeight + tileSet.spacing;
				}
			}
		}
	}
	
	public Vector2 getLocation(int tile){
		return imageTilePositions[tile - tileSet.firstgid];
	}
}
