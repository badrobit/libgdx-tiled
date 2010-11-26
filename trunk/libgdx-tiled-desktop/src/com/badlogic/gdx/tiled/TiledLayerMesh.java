package com.badlogic.gdx.tiled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;

public class TiledLayerMesh {
	private Mesh[] mesh;

	private float[][] vertices;
	private int maxVertices;

	private static final int NUMCOMPONENTS = 4; //sum of all vertex attribute components
	private TiledLayer layer;
	private TileSet tileSet;
	
	private int layerHeightPixels;
	private int layerWidthPixels;
	
	private int currentVertexIndex = 0;
	private int currentTileVertex;
	
	//Currently only allows for a single TileSet to be loaded per layer.
	//This limitation exists because binding a new texture for each tile is inefficient
	//and gdx only allows for one image per managed texture
	public TiledLayerMesh(TiledLayer layer, TileSet tileSet) {
		this.layer = layer;
		this.tileSet = tileSet;
		
		layerHeightPixels = layer.height * tileSet.tileHeight;
		layerWidthPixels = layer.width * tileSet.tileWidth;
		
		maxVertices = 4 * layer.width;
		
		VertexAttribute position = new VertexAttribute(
				VertexAttributes.Usage.Position, 2, null);

		VertexAttribute textureCoordinate = new VertexAttribute(
				VertexAttributes.Usage.TextureCoordinates, 2, null);
		
		vertices = new float[layer.height][maxVertices * NUMCOMPONENTS];
		mesh = new Mesh[layer.height];
		
		for(int row=0; row < layer.height; row++){
			currentVertexIndex = 0;
			mesh[row] = new Mesh(true, maxVertices, 0, position, textureCoordinate);
			fill(row);
			mesh[row].setVertices(vertices[row]);
		}
	}

	private void fill(int row) {
		int col;
		int tile;
		
		for (col = 0; col < layer.width; col++) {
			//load tiles "upside down" since origin is in bottom left for gdx, but in upper left in Tiled
			tile = layer.map[layer.height - row - 1][col];
			if(tile == 0)
				addEmptyTile(row, col);
			else
				addTile(row, col, tile);
		}
	}
	
	//adds a couple of degenerate triangles to fill the gap for empty tiles
	private void addEmptyTile(int row, int col) {
		if (col % 2 == 0) {
			vertices[row][currentVertexIndex++] = (float)col * tileSet.tileWidth;// position X
			vertices[row][currentVertexIndex++] = (float)(row + 1) * tileSet.tileHeight;// position Y
			vertices[row][currentVertexIndex++] = 0; // texture U
			vertices[row][currentVertexIndex++] = 0; // texture V
			
			vertices[row][currentVertexIndex++] = (float)col * tileSet.tileWidth;// position X
			vertices[row][currentVertexIndex++] = (float)(row + 1) * tileSet.tileHeight;// position Y
			vertices[row][currentVertexIndex++] = 0; // texture U
			vertices[row][currentVertexIndex++] = 0; // texture V
			
			vertices[row][currentVertexIndex++] = (float)(col + 1) * tileSet.tileWidth;// position X
			vertices[row][currentVertexIndex++] = (float)row * tileSet.tileHeight;// position Y
			vertices[row][currentVertexIndex++] = 0; // texture X
			vertices[row][currentVertexIndex++] = 0; // texture Y
			
			vertices[row][currentVertexIndex++] = (float)(col + 1) * tileSet.tileWidth;// position X
			vertices[row][currentVertexIndex++] = (float)row * tileSet.tileHeight;// position Y
			vertices[row][currentVertexIndex++] = 0; // texture X
			vertices[row][currentVertexIndex++] = 0; // texture Y
		} else {
			vertices[row][currentVertexIndex++] = (float)col * tileSet.tileWidth;// position X
			vertices[row][currentVertexIndex++] = (float)(row + 1) * tileSet.tileHeight;// position Y
			vertices[row][currentVertexIndex++] = 0; // texture U
			vertices[row][currentVertexIndex++] = 0; // texture V
			
			vertices[row][currentVertexIndex++] = (float)col * tileSet.tileWidth;// position X
			vertices[row][currentVertexIndex++] = (float)(row + 1) * tileSet.tileHeight;// position Y
			vertices[row][currentVertexIndex++] = 0; // texture U
			vertices[row][currentVertexIndex++] = 0; // texture V
			
			vertices[row][currentVertexIndex++] = (float)(col + 1) * tileSet.tileWidth;// position X
			vertices[row][currentVertexIndex++] = (float)row * tileSet.tileHeight;// position Y
			vertices[row][currentVertexIndex++] = 0; // texture X
			vertices[row][currentVertexIndex++] = 0; // texture Y
			
			vertices[row][currentVertexIndex++] = (float)(col + 1) * tileSet.tileWidth;// position X
			vertices[row][currentVertexIndex++] = (float)row * tileSet.tileHeight;// position Y
			vertices[row][currentVertexIndex++] = 0; // texture X
			vertices[row][currentVertexIndex++] = 0; // texture Y			
		}
	}

	void addTile(int row, int col, int tile){
		for (currentTileVertex = 0; currentTileVertex < 4; currentTileVertex++) {
			if (col % 2 == 0) {
				switch (currentTileVertex) {
				case 0: // bottom left corner
					vertices[row][currentVertexIndex++] = (float)col * tileSet.tileWidth;// position X
					vertices[row][currentVertexIndex++] = (float)(row + 1) * tileSet.tileHeight;// position Y
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).x) / tileSet.texture.getWidth(); //texture U
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).y) / tileSet.texture.getHeight(); // texture V
					break;
				case 1: // top left corner
					vertices[row][currentVertexIndex++] = (float)col * tileSet.tileWidth;// position X
					vertices[row][currentVertexIndex++] = (float)row * tileSet.tileHeight;// position Y
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).x) / tileSet.texture.getWidth(); // texture U
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).y + tileSet.getRegion(tile).height) / tileSet.texture.getHeight(); // texture V
					break;
				case 2: // bottom right corner
					vertices[row][currentVertexIndex++] = (float)(col + 1) * tileSet.tileWidth;// position X
					vertices[row][currentVertexIndex++] = (float)(row + 1) * tileSet.tileHeight;// position Y
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).x + tileSet.getRegion(tile).width) / tileSet.texture.getWidth(); // texture X
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).y) / tileSet.texture.getHeight(); // texture Y
					break;
				case 3: // top right corner
					vertices[row][currentVertexIndex++] = (float)(col + 1) * tileSet.tileWidth;// position X
					vertices[row][currentVertexIndex++] = (float)row * tileSet.tileHeight;// position Y
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).x + tileSet.getRegion(tile).width) / tileSet.texture.getWidth(); // texture X
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).y + tileSet.getRegion(tile).height) / tileSet.texture.getHeight(); // texture Y
					break;
				}
			} else {
				switch (currentTileVertex) {
				case 0: // top left corner
					vertices[row][currentVertexIndex++] = (float)col * tileSet.tileWidth;// position X
					vertices[row][currentVertexIndex++] = (float)row * tileSet.tileHeight;// position Y
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).x) / tileSet.texture.getWidth(); // texture X
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).y + tileSet.getRegion(tile).height) / tileSet.texture.getHeight(); // texture Y
					break;
				case 1: // top right corner
					vertices[row][currentVertexIndex++] = (float)(col + 1) * tileSet.tileWidth;// position X
					vertices[row][currentVertexIndex++] = (float)row * tileSet.tileHeight;// position Y
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).x + tileSet.getRegion(tile).width) / tileSet.texture.getWidth(); // texture X
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).y + tileSet.getRegion(tile).height) / tileSet.texture.getHeight(); // texture Y
					break;
				case 2: // bottom left corner
					vertices[row][currentVertexIndex++] = (float)col * tileSet.tileWidth;// position X
					vertices[row][currentVertexIndex++] = (float)(row + 1) * tileSet.tileHeight;// position Y
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).x) / tileSet.texture.getWidth(); // texture X
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).y) / tileSet.texture.getHeight(); // texture Y
					break;
				case 3: // bottom right corner
					vertices[row][currentVertexIndex++] = (float)(col + 1) * tileSet.tileWidth;// position X
					vertices[row][currentVertexIndex++] = (float)(row + 1) * tileSet.tileHeight;// position Y
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).x + tileSet.getRegion(tile).width) / tileSet.texture.getWidth(); // texture X
					vertices[row][currentVertexIndex++] = (float)(tileSet.getRegion(tile).y) / tileSet.texture.getHeight(); // texture Y
					break;
				}
			}
		}
	}

	//TODO: add render funtions that accept a shaderprogram for GLES 2 rendering
	//This function should not be used most of the time. Use render(int x, int y, int width, int height) instead.
	public void render() {
		Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
		Gdx.gl10.glEnable(GL10.GL_CULL_FACE);

		tileSet.texture.bind();
		for(int row = 0; row < layer.height; row++){
			mesh[row].render(GL10.GL_TRIANGLE_STRIP);
		}
		
		Gdx.gl10.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	private int firstRow, firstCol, lastRow, lastCol;
	
	public void render(int x, int y, int width, int height) {
		Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
		Gdx.gl10.glEnable(GL10.GL_CULL_FACE);
		Gdx.gl10.glEnable(GL10.GL_BLEND);
		
		firstRow = getRow(layerHeightPixels - y);
		if(firstRow < 0) firstRow = 0;
		lastRow = getRow(layerHeightPixels - y + height) + 1;
		if(lastRow >= layer.height) lastRow = layer.height - 1;
		
		firstCol = getCol(x);
		if(firstCol < 0) firstCol = 0;
		lastCol = getCol(width) + 1;
		if(lastCol >= layer.width) lastCol = layer.width - 1;
		
		tileSet.texture.bind();
		for(int row = firstRow; row < lastRow; row++){
			mesh[row].render(GL10.GL_TRIANGLE_STRIP, firstCol * 4, (lastCol - firstCol) * 4);
		}
	}
	
	public int getLayerHeightPixels() {
		return layerHeightPixels;
	}

	public int getLayerWidthPixels() {
		return layerWidthPixels;
	}

	int getRow(int worldY){
		if(worldY < 0) return 0;
		if(worldY > layerHeightPixels) return tileSet.tileHeight - 1;
		return worldY/tileSet.tileHeight;
	}
	
	int getCol(int worldX){
		if(worldX < 0) return 0;
		if(worldX > layerWidthPixels) return tileSet.tileWidth - 1;
		return worldX/tileSet.tileWidth;
	}
	
	void dispose() {
		if (mesh != null)
			for(int row = 0; row < layer.height; row++){
				if(mesh[row] != null) mesh[row].dispose();
			}
	}
}
