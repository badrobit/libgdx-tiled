package game.hex;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Hex
{
	private
	Vector2 Position;
	public float getPositionX()
	{
		return Position.x;
	}
	public float getPositionY()
	{
		return Position.y;
	}
	
	private
	float Width;
	public float getWidth()
	{
		return Width;
	}
	private
	float Height;
	public float getHeight()
	{
		return Height;
	}
	
	private
	Color Color;
	public Color getColor()
	{
		return Color;
	}
	public void setColor(Color value)
	{
		Color = value;
	}
	
	private
	Vector2[] Points;
	public Vector2[] getPoints()
	{
		return Points;		
	}
	
	public Hex(Vector2 pos, float side, HexOrientation orientation)
	{
		Position = new Vector2();
		this.Width = HexMath.getWidth(side, orientation);
		this.Height = HexMath.getHeight(side, orientation);
		Points = HexMath.getPoints2(pos.x, pos.y, side, orientation);
		this.Color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1.0f);
		
		switch(orientation)
		{
			case FLAT:
			{
				this.Position.set(Points[5].x, Points[4].y);
				break;
			}
			case POINT:
			{
				this.Position.set(Points[4].x, Points[3].y);
				break;
			}
		}
	}
	public Hex(float x, float y, float side, HexOrientation orientation)
	{
		Position = new Vector2();
		this.Width = HexMath.getWidth(side, orientation);
		this.Height = HexMath.getHeight(side, orientation);
		Points = HexMath.getPoints2(x, y, side, orientation);
		this.Color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1.0f);
		
		switch(orientation)
		{
			case FLAT:
			{
				this.Position.set(Points[5].x, Points[4].y);
				break;
			}
			case POINT:
			{
				this.Position.set(Points[4].x, Points[3].y);
				break;
			}
		}
	}
}
