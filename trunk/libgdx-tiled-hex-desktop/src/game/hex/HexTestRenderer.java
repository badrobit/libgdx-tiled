package game.hex;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.Vector2;

public class HexTestRenderer
{
	private static
	ImmediateModeRenderer
	Renderer = new ImmediateModeRenderer();
	public static void RenderHexMapFilled(HexMap map)
	{
		Hex[][] Hexes = map.getHexes();
		for (int y = 0; y < Hexes.length; y++)
		{
			Hex[] Row = Hexes[y];
			for (int x = 0; x < Row.length; x++)
			{
				Hex Hex = Row[x];
				if (Hex == null) continue;
				Color Color = Hex.getColor();
				Vector2[] HexPoints = Hex.getPoints();
				Renderer.begin(GL10.GL_TRIANGLE_FAN);
				for (int p = 0; p < HexPoints.length; p++)
				{
					if (p < HexPoints.length - 1)
					{
						Renderer.color(Color.r, Color.g, Color.b, Color.a);						
						Renderer.vertex(HexPoints[p].x, HexPoints[p].y, 0.0f);
						Renderer.color(Color.r, Color.g, Color.b, Color.a);
						Renderer.vertex(HexPoints[p + 1].x, HexPoints[p + 1].y, 0.0f);
					}
					else
					{
						Renderer.color(Color.r, Color.g, Color.b, Color.a);
						Renderer.vertex(HexPoints[p].x, HexPoints[p].y, 0.0f);
						Renderer.color(Color.r, Color.g, Color.b, Color.a);
						Renderer.vertex(HexPoints[0].x, HexPoints[0].y, 0.0f);
					}
				}
				Renderer.end();
			}
		}	
	}
	public static void RenderHexMapLines(HexMap map)
	{
		Renderer.begin(GL10.GL_LINES);
		
		Hex[][] Hexes = map.getHexes();
		for (int y = 0; y < Hexes.length; y++)
		{
			Hex[] Row = Hexes[y];
			for (int x = 0; x < Row.length; x++)
			{
				Hex Hex = Row[x];
				if (Hex == null) continue;
				Color Color = Hex.getColor();
				Vector2[] HexPoints = Hex.getPoints();
				for (int p = 0; p < HexPoints.length; p++)
				{
					if (p < HexPoints.length - 1)
					{
						Renderer.color(Color.r, Color.g, Color.b, Color.a);
						Renderer.vertex(HexPoints[p].x, HexPoints[p].y, 0.0f);
						Renderer.color(Color.r, Color.g, Color.b, Color.a);
						Renderer.vertex(HexPoints[p + 1].x, HexPoints[p + 1].y, 0.0f);
					}
					else
					{
						Renderer.color(Color.r, Color.g, Color.b, Color.a);
						Renderer.vertex(HexPoints[p].x, HexPoints[p].y, 0.0f);
						Renderer.color(Color.r, Color.g, Color.b, Color.a);
						Renderer.vertex(HexPoints[0].x, HexPoints[0].y, 0.0f);
					}
				}
			}
		}
		
		Renderer.end();
		
	}
	public static void RenderHex(Vector2[] HexPoints)
	{
		Renderer.begin(GL10.GL_LINES);
		for (int p = 0; p < HexPoints.length; p++)
		{
			if (p < HexPoints.length - 1)
			{
				Renderer.color(1.0f, 1.0f, 1.0f, 1.0f);
				Renderer.vertex(HexPoints[p].x, HexPoints[p].y, 0.0f);
				Renderer.color(1.0f, 1.0f, 1.0f, 1.0f);
				Renderer.vertex(HexPoints[p + 1].x, HexPoints[p + 1].y, 0.0f);
			}
			else
			{
				Renderer.color(1.0f, 1.0f, 1.0f, 1.0f);
				Renderer.vertex(HexPoints[p].x, HexPoints[p].y, 0.0f);
				Renderer.color(1.0f, 1.0f, 1.0f, 1.0f);
				Renderer.vertex(HexPoints[0].x, HexPoints[0].y, 0.0f);
			}
		}
		Renderer.end();
	}	

}
