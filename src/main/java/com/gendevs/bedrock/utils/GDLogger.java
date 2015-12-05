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

package com.gendevs.bedrock.utils;

import java.util.logging.Logger;

public class GDLogger {
	public static void logInfo(String className, String message){
		Logger log = Logger.getLogger(className);
		log.info(message);
	}
	public static void logWarning(String className, String message){
		Logger log = Logger.getLogger(className);
		log.warning(message);
	}
	public static void logError(String className, String message){
		Logger log = Logger.getLogger(className);
		log.severe(message);
	}
}
