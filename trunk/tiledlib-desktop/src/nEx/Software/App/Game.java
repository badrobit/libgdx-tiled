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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Sprite;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.SpriteCache;
import com.badlogic.gdx.graphics.TextureRegion;

public class Game implements ApplicationListener
{
	TiledMap map;
	SpriteBatch batch;
	long startTime = System.nanoTime();
	
	SpriteCache cache;
	
	OrthographicCamera cam;
	
	@Override
	public void create()
	{
		batch = new SpriteBatch();
		map = TiledMapLoader.loadTiledMap(Gdx.files.internal("tilemap2.tmx").read());

		cam = new OrthographicCamera();
		cam.setViewport(480, 320);
		cam.getPosition().set(map.getWidth()*32/2, map.getHeight()*32/2,0);
		
		cache = new SpriteCache(map.getLayers().size() * (map.getWidth() * map.getHeight()), false);
		if (map != null)
		{	
			int layers = map.getLayers().size();
			
			for (int index = 0; index < layers; index++)
			{
				TiledMapLayer layer
				=
				map.getLayers().get(index);
				if(layer instanceof TiledMapTileLayer)
				{
					TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
					cache.beginCache();
					for (int x = 0; x < tileLayer.getWidth(); x++)
					{
						for (int y = 0; y < tileLayer.getHeight(); y++)
						{
							TiledMapTile tile = tileLayer.getTiles()[x][y];
							if (tile != null)
							{
								cache.draw(tile.getTextureRegion(), x * 32, y * 32, Color.WHITE);
							}							
						}
					}
				}
				System.out.println(cache.endCache());
			}
		}		
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
        gl.glEnable(GL10.GL_BLEND);
        cam.update();
        
//      Uncomment this block o' crap to use SpriteBatch.
//		batch.begin();
//		if (map != null)
//		{	
//			int layers = map.getLayers().size();
//			
//			for (int index = 0; index < layers; index++)
//			{
//				TiledMapLayer layer
//				=
//				map.getLayers().get(index);
//				if(layer instanceof TiledMapTileLayer)
//				{
//					TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
//					
//					// This will not be the final approach, as it is clearly not "sustainable"
//					// Just wanted to make sure I have something loaded.
//					// Here, we have approximately 30 FPS on ADP2
//					for (int x = 0; x < 15; x++)
//					{
//						for (int y = 0; y < 10; y++)
//						{
//							TiledMapTile tile = tileLayer.getTiles()[x][y];
//							if (tile != null)
//							{
//								batch.draw(tile.getTextureRegion(), x * 32, y * 32, Color.WHITE);
//							}
//							
//						}
//					}
//				}				
//			}
//		}
//		batch.end();
		
        cache.setProjectionMatrix(cam.getCombinedMatrix());
        cache.begin();
        //For the time being, the hardware is doing all of the culling.
        //48fps on ADP2
        for (int layer = 0; layer < 4; layer++)
        {
        	cache.draw(layer);
        }
        cache.end();
        if(System.nanoTime()-startTime>=1000000000)
        {
            Gdx.app.log("TileTest", "fps: " + Gdx.graphics.getFramesPerSecond());
            startTime = System.nanoTime();
        }
        cam.getPosition().x += 5 * Gdx.graphics.getDeltaTime();
        if (cam.getPosition().x > map.getWidth() * 32)
        cam.getPosition().x=0;
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
