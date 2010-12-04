/*
 * Copyright (C) 2010 Justin Shapcott (support@nexsoftware.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nEx.Software.Maps.Loaders.Tiled.Objects;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.TextureRegion;

public class TiledMapTile
{
	private
	int GlobalId;
	public int getGlobalId()
	{
		return GlobalId;
		
	}
	public void setGlobalId(int value)
	{
		GlobalId = value;
	}
	
	private
	TextureRegion TextureRegion;
	public TextureRegion getTextureRegion()
	{
		return TextureRegion;
	}
	public void setTextureRegion(TextureRegion value)
	{
		TextureRegion = value;
	}

	private
	Map<String, String> Properties
	=
	new HashMap<String, String>();
	public Map<String, String> getProperties()
	{
		return Properties;
	}
}
