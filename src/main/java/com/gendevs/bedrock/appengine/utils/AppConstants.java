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

import com.gendevs.bedrock.appengine.service.storage.StorageConstants;

public class AppConstants {
	public static int TIME_OUT_DAYS = 1; 
	public static int INVITE_USERS_TIME_OUT_DAYS = 30;
	public static int LIFE_TIME = 60000;
	
	public static int BLOCK_TIME_MILLIS = 60000;//1 * 60* 1000;
	public static int MAX_FAIL_ATTEMPTS = 3;
	public static String USER_ID_KEY = "USER_ID_KEY";
	public static String ORGANISATION_ID_KEY = "ORGANISATION_ID_KEY";

	public static String API_KEY_SWAGGER = "yellowbuffalo";
	
	public static String BASE_URL = "http://bedrock-core.appspot.com";
	public static String DEFAULT_USER_IMAGE_URL = "/img/blank-profile.png";
	public static String DEFAULT_WINDOWS_APP_IMAGE_URL = "/img/windows.png";
	public static String DEFAULT_IOS_APP_IMAGE_URL = "/img/ios.png";
	public static String DEFAULT_ANDROID_APP_IMAGE_URL = "/img/android.png";
	public static String DEFAULT_WEB_PORTAL_APP_IMAGE_URL = "/img/web_portal.png";

	public static String API_KEY_SWAGGER_SUPER_ADMIN = "super_admin_api_key";
	public static String API_KEY_SWAGGER_ADMIN = "admin_api_key";
	public static String API_KEY_SWAGGER_USER = "user_api_key";
	
	public static String URL_TO_SLICE = "https://s3-us-west-2.amazonaws.com/"+ StorageConstants.BUCKET_NAME;
	
	public static String APP_EMAILID = "anyemail@gmail.com";
	public static int SHORTEN_SIZE = 150;
}
