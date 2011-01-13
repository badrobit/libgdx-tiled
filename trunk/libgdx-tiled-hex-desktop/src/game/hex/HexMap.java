package game.hex;

public class HexMap
{
	private
	Hex[][] Hexes;
	public Hex[][] getHexes()
	{
		return Hexes;		
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
	HexMap.StartPosition Start;
	public HexMap.StartPosition getStart()
	{
		return Start;
	}
	
	private
	HexOrientation Orientation;
	public HexOrientation getOrientation()
	{
		return Orientation;
	}
	public HexMap(int rows, int cols, float side, HexMap.StartPosition start, HexOrientation orientation)
	{
		Hexes = new Hex[rows][cols];
		final float h = HexMath.getH(side, orientation);
		final float r = HexMath.getR(side, orientation);
		final float hexwidth = HexMath.getWidth(side, orientation);
		final float hexheight = HexMath.getHeight(side, orientation);
		
		switch(orientation)
		{
			case FLAT:
			{
				Width = (cols * (side + (2 * h)) - ((cols - 1) * h));
				Height = (rows * (r * 2)) + r;
			}
			case POINT:
			{
				Height = rows * (r * 2) + r;
			}
		}
		for (int y = 0; y < rows; y++)
		{
			for (int x = 0; x < cols; x++)
			{
				boolean inTopRow = (y == 0);
				boolean inBotRow = (y == rows - 1);
				boolean inLeftCol = (x == 0);
				boolean inRightCol = (x == cols - 1);
				boolean isTopLeft = (inTopRow && inLeftCol);
				boolean isTopRight = (inTopRow && inRightCol);
				boolean isBotLeft = (inBotRow && inLeftCol);
				boolean isBotRight = (inBotRow && inRightCol);
				
				if (isTopLeft)
				{
					switch(orientation)
					{
						case FLAT:
						{
							switch(start)
							{
								case INNER:
								{
									//TODO: Implement Inner
									break;
								}
								case OUTER:
								{
									
									Hexes[y][x] = new Hex(0 + h, Height, side, orientation);
									break;
								}
							}
							break;
						}
						case POINT:
						{
							switch(start)
							{
								case INNER:
								{
									//TODO: Implement Inner
									break;
								}
								case OUTER:
								{									
									Hexes[y][x] = new Hex(0 + r, Height, side, orientation);
									break;
								}
							}
							break;
						}
					}
				}
				else
				{
					if (inLeftCol)
					{
						switch(orientation)
						{
							case FLAT:
							{
								switch(start)
								{
									case INNER:
									{
										//TODO: Implement Inner
										break;
									}
									case OUTER:
									{
		                                Hexes[y][x] = new Hex(Hexes[y - 1][x].getPoints()[4], side, orientation);
										break;
									}
								}
								break;
							}
							case POINT:
							{
								switch(start)
								{
									case INNER:
									{
										//TODO: Implement Inner
										break;
									}
									case OUTER:
									{
		                                if (y % 2 == 0)
		                                {
		                                    Hexes[y][x] = new Hex(Hexes[y - 1][x].getPoints()[4], side, orientation);
		                                }
		                                else
		                                {
		                                    Hexes[y][x] = new Hex(Hexes[y - 1][x].getPoints()[2], side, orientation);
		                                }
										break;
									}
								}
							}
						}
					}
					else
					{
						switch(orientation)
						{
							case FLAT:
							{
								switch(start)
								{
									case INNER:
									{
										//TODO: Implement Inner
										break;
									}
									case OUTER:
									{
		                                if (x % 2 == 0)
		                                {
		                                    Hexes[y][x] = new Hex(Hexes[y][x - 1].getPoints()[1].x + h, Hexes[y][x - 1].getPoints()[1].y + r, side, orientation);
		                                }
		                                else
		                                {
		                                    Hexes[y][x] = new Hex(Hexes[y][x - 1].getPoints()[2],side, orientation);
		                                }		                                
										break;
									}
								}
								break;
							}
							case POINT:
							{
								switch(start)
								{
									case INNER:
									{
										//TODO: Implement Inner
										break;
									}
									case OUTER:
									{
		                                Hexes[y][x] = new Hex(Hexes[y][x - 1].getPoints()[1].x + r, Hexes[y][x - 1].getPoints()[1].y + h, side, orientation); 
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	public enum StartPosition
	{
		INNER
		,
		OUTER
	}
}
