package nEx.Software.Maps.Loaders.Tiled.Objects;

import java.util.HashMap;
import java.util.Map;

public class TiledMapObject
{
	private
	String Name;
	public String getName()
	{
		return Name;		
	}
	public void setName(String value)
	{
		Name = value;		
	}
	
	private
	String Type;
	public String getType()
	{
		return Type;		
	}
	public void setType(String value)
	{
		Type = value;		
	}
	
	private
	int PosX;
	public int getPosX()
	{
		return PosX;		
	}
	public void setPosX(int value)
	{
		PosX = value;		
	}
	
	private
	int PosY;
	public int getPosY()
	{
		return PosY;		
	}
	public void setPosY(int value)
	{
		PosY = value;		
	}
	
	private
	int Width;
	public int getWidth()
	{
		return Width;		
	}
	public void setWidth(int value)
	{
		Width = value;		
	}
	
	private
	int Height;
	public int getHeight()
	{
		return Height;		
	}
	public void setHeight(int value)
	{
		Height = value;		
	}
	
	private
	Map<String, String> Properties;
	public Map<String, String> getProperties()
	{
		return Properties;
	}
	
	public TiledMapObject()
	{
		Properties = new HashMap<String, String>();
	}
}
