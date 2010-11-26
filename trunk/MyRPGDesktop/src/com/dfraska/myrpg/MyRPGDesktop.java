
package com.dfraska.myrpg;

import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.dfraska.myrpg.MyRPG;

public class MyRPGDesktop {
	public static void main (String[] argv) {
		new JoglApplication(new MyRPG(), "Hello World", 480, 320, false);		
	}
}
