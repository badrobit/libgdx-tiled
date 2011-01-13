package app;

import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.math.Vector2;

import game.Game;
import game.hex.HexMap;
import game.hex.HexMath;
import game.hex.HexOrientation;

public class App
{
	public static void main(String[] args)
	{
		new JoglApplication(new Game(), "Hex", 480, 320, false);
	}
}
