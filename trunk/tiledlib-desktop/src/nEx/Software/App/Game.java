/*
 * Copyright (C) 2010 Justin Shapcott (support@nexsoftware.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nEx.Software.App;

import nEx.Software.Maps.Loaders.Tiled.TiledMapLoader;
import nEx.Software.Maps.Loaders.Tiled.Objects.TiledMap;
import nEx.Software.Maps.Loaders.Tiled.Objects.TiledMapLayer;
import nEx.Software.Maps.Loaders.Tiled.Objects.TiledMapTile;
import nEx.Software.Maps.Loaders.Tiled.Objects.TiledMapTileLayer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Sprite;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.TextureRegion;

public class Game implements ApplicationListener
{
	TiledMap map;
	SpriteBatch batch;
	long startTime = System.nanoTime();
	
	@Override
	public void create()
	{
		batch = new SpriteBatch();
		map = TiledMapLoader.loadTiledMap(Gdx.files.internal("tilemap2.tmx").read());		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	Sprite s = new Sprite();
	@Override
	public void render() {
        GL10 gl = Gdx.gl10;
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
		batch.begin();
		if (map != null)
		{	
			TiledMapLayer layer = map.getLayers().get(0);
			if(layer instanceof TiledMapTileLayer)
			{
				TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
				TiledMapTile tile = tileLayer.getTiles()[10][10];
				
				// This will not be the final approach, as it is clearly not "sustainable"
				// Just wanted to make sure I have something loaded.
				batch.draw(tile.getTextureRegion(), 0, 0, Color.WHITE);
			}
			
		}
		batch.end();
		
        if(System.nanoTime()-startTime>=1000000000)
        {
            Gdx.app.log("TileTest", "fps: " + Gdx.graphics.getFramesPerSecond());
            startTime = System.nanoTime();
        }
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
