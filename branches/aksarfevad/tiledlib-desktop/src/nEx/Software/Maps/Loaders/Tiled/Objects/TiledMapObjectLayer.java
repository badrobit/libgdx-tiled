package nEx.Software.Maps.Loaders.Tiled.Objects;

import java.util.Map;
import java.util.HashMap;

public class TiledMapObjectLayer extends TiledMapLayer
{
	private
	Map<String, TiledMapObject> Objects;
	public Map<String, TiledMapObject> getObjects()
	{
		return Objects;
	}
	
	public TiledMapObjectLayer()
	{
		Objects = new HashMap<String, TiledMapObject>();
	}
	
	private boolean locked;
	public void lockSize()
	{
		if (!locked)
		{
			if (getWidth() > 0 && getHeight() > 0)
			{
				locked = true;
			}
		}
	}
}
