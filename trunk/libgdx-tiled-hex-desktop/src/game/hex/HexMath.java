package game.hex;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.MathUtils;

public class HexMath
{
	
	/**
	 * Returns the h value of a hex with the given side and orientation.
	 * 
	 * @param side
	 * @param orientation
	 * @return
	 */
	public static float getH(float side, HexOrientation orientation)
	{
		return MathUtils.sinDeg(30) * side;
	}
	public static float getR(float side, HexOrientation orientation)
	{
		return MathUtils.cosDeg(30) * side;		
	}	
	public static float getWidth(float side, HexOrientation orientation)
	{
		float result = 0;
		switch(orientation)
		{
			case FLAT:
			{
				result = side + (2 * getH(side, orientation));
				break;
			}
			case POINT:
			{
				result = 2 * getR(side, orientation);
				break;
			}
		}
		
		return result;		
	}
	public static float getHeight(float side, HexOrientation orientation)
	{
		float result = 0;
		switch(orientation)
		{
			case FLAT:
			{
				result = 2 * getR(side, orientation);
				break;
			}
			case POINT:
			{
				result = side + (2 * getH(side, orientation));
				break;
			}
		}		
		return result;
	}
	
	public static Vector2[] getPoints(float x, float y, float side, HexOrientation orientation)
	{
		Vector2[] result = new Vector2[6];
		final float h = getH(side, orientation);
		final float r = getR(side, orientation);
		
		switch(orientation)
		{
			case FLAT:
			{
				result[0] = new Vector2(x, y);
				result[1] = new Vector2(x + side, y);
				result[2] = new Vector2(x + side + h, y + r);
				result[3] = new Vector2(x + side, y + r + r);
				result[4] = new Vector2(x, y + r + r);
				result[5] = new Vector2(x - h, y + r );
				break;
			}
			case POINT:
			{
                result[0] = new Vector2(x, y);
                result[1] = new Vector2(x + r, y + h);
                result[2] = new Vector2(x + r, y + side + h);
                result[3] = new Vector2(x, y + side + h + h);
                result[4] = new Vector2(x - r, y + side + h);
                result[5] = new Vector2(x - r, y + h);
				break;
			}
		}
		return result;
		
	}
	public static Vector2[] getPoints2(float x, float y, float side, HexOrientation orientation)
	{
		Vector2[] result = new Vector2[6];
		final float h = getH(side, orientation);
		final float r = getR(side, orientation);
		
		switch(orientation)
		{
			case FLAT:
			{
				result[0] = new Vector2(x, y);
				result[1] = new Vector2(x + side, y);
				result[2] = new Vector2(x + side + h, y - r);
				result[3] = new Vector2(x + side, y - r - r);
				result[4] = new Vector2(x, y - r - r);
				result[5] = new Vector2(x - h, y - r );
				break;
			}
			case POINT:
			{
                result[0] = new Vector2(x, y);
                result[1] = new Vector2(x + r, y - h);
                result[2] = new Vector2(x + r, y - side - h);
                result[3] = new Vector2(x, y - side - h - h);
                result[4] = new Vector2(x - r, y - side - h);
                result[5] = new Vector2(x - r, y - h);
				break;
			}
		}
		return result;		
	}
}
