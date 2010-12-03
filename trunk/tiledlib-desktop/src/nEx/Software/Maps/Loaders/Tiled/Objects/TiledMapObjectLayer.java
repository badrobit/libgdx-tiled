package nEx.Software.Maps.Loaders.Tiled.Objects;

public class TiledMapObjectLayer extends TiledMapLayer {
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
