package game;

import game.hex.Hex;
import game.hex.HexMap;
import game.hex.HexOrientation;
import game.hex.HexTestRenderer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Sprite;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

public class Game implements ApplicationListener
{
	public static long TICK_TIME = (long) ((1.0f / 30) * 1000 * 1000000); // 30 simulations per second
	
	HexMap Map;
	Sprite Sprite;
	Texture Texture;
	SpriteBatch SpriteBatch;
	OrthographicCamera Camera;

	private
	long fpsTime;
	
	private
	long startTime;
	
	private
	int tick = 0;
	
	private
	float percent;
	
	@Override
	public void create()
	{
		Gdx.input.setInputProcessor(Processor);
		
		Camera
		=
		new OrthographicCamera();
		Camera.setViewport(480.0f, 320.0f);
		Camera.getPosition().set(240.0f, 160.0f, 0.0f);
		
		Camera.update();

		Texture
		=
		Gdx.graphics.newTexture
		(
			Gdx.files.getFileHandle
			(
				"data/hex.png"
				,
				FileType.Internal
			)
			,
			TextureFilter.MipMap
			,
			TextureFilter.Linear
			,
			TextureWrap.ClampToEdge
			,
			TextureWrap.ClampToEdge
		);
		Sprite = new Sprite(Texture);		
		SpriteBatch = new SpriteBatch();
		Sprite.setRotation(0);
		Map = new HexMap(12, 12, 28, HexMap.StartPosition.OUTER, HexOrientation.FLAT);
		Camera.getPosition().set(Map.getWidth() / 2.0f, Map.getHeight() / 2.0f, 0.0f);
		
		startTime = System.nanoTime();
	}
	@Override
	public void dispose()
	{
	}
	@Override
	public void pause()
	{
	}
	@Override
	public void resume()
	{		
	}
	
	public long accumulator = 0;
	
	@Override
	public void render()
	{
		Camera.setMatrices();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		final long newTime = System.nanoTime();
		final float deltaTime = newTime - startTime;
		
		startTime = newTime;
		accumulator += deltaTime;
		
		while (accumulator>=TICK_TIME)
		{
			// Step World
			accumulator -= TICK_TIME;
            tick++;
		}
		
//		Use the SpriteBatch to render textured tiles
		SpriteBatch.setProjectionMatrix(Camera.getCombinedMatrix());
		SpriteBatch.begin();

		Hex[][] Hexes = Map.getHexes();
		for (int y = 0; y < Hexes.length; y++)
		{
			Hex[] Row = Hexes[y];			
			for (int x = 0; x < Row.length; x++)
			{
				Hex Hex = Row[x];
				if (Hex == null) continue;
				Sprite.setPosition(Hex.getPositionX(), Hex.getPositionY());
				Sprite.setColor(Hex.getColor());
				Sprite.draw(SpriteBatch);
			}
		}
		
		SpriteBatch.end();
		
//		Use the ImmediateModeRenderer with filled hexes
//		HexTestRenderer.RenderHexMapFilled(Map);

//		Use the ImmediateModeRenderer with just lines
//		HexTestRenderer.RenderHexMapLines(Map);

	}
	@Override
	public void resize(int w, int h)
	{
		Camera.setViewport(w, h);
	}

	InputProcessor Processor = new InputProcessor()
	{
		@Override
		public boolean keyUp(int keycode)
		{
			return false;
		}
		@Override
		public boolean keyDown(int keycode)
		{
			return false;
		}
		@Override
		public boolean keyTyped(char character)
		{
			return false;
		}
		@Override
		public boolean touchUp(int x, int y, int pointer)
		{
			System.out.println(tick + "," + percent);			
			return false;
		}
		@Override
		public boolean touchDown(int x, int y, int pointer)
		{
			return false;
		}
		@Override
		public boolean touchDragged(int x, int y, int pointer)
		{
			return false;
		}		
	};
}
