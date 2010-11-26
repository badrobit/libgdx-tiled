
package com.dfraska.myrpg;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.dfraska.myrpg.MyRPG;

public class MyRPGAndroid extends AndroidApplication {
	@Override public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize(new MyRPG(), false);		
	}
}
