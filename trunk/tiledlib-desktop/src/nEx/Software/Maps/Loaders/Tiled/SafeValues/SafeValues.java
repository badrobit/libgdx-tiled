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

package nEx.Software.Maps.Loaders.Tiled.SafeValues;

public class SafeValues
{
	public static final int safeInt(String sourceValue, int defaultValue)
	{
		int result = defaultValue;
		try
		{
			if (sourceValue != null)
			{
				result = Integer.parseInt(sourceValue);
			}
		}
		catch(NumberFormatException e)
		{
			// Boooooo, hissssss...
		}
		return result;
	}
}
