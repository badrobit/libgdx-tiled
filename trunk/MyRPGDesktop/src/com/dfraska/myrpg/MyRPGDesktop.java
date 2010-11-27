/*
 * Copyright 2010 David Fraska (dfraska@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.dfraska.myrpg;

import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.dfraska.myrpg.MyRPG;

public class MyRPGDesktop {
	public static void main (String[] argv) {
		new JoglApplication(new MyRPG(), "Hello World", 480, 320, false);		
	}
}
