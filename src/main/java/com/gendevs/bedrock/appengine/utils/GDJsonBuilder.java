/*
 * Copyright 2015 General Developers. www.gendevs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gendevs.bedrock.appengine.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GDJsonBuilder {

	private static Gson getGson() {
		return new GsonBuilder().setPrettyPrinting().create();
	}

	public static <T> T getEntityForJson(String json, Class<T> entity) {
		if (json == null)
			return null;

		try {
			return getGson().fromJson(json, entity);
		} catch (Exception e) {
			GDLogger.logError(GDJsonBuilder.class.toString(), e.getMessage());
		}
		return null;
	}

	public static String getJsonForEntity(Object entity) {
		try {
			return getGson().toJson(entity);
		} catch (Exception e) {
		}
		return null;
	}
}
