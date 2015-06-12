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

package com.gendevs.bedrock.appengine.response;

import com.gendevs.bedrock.appengine.json.JsonInterface;

public class ServerResponse<T> implements JsonInterface<T> {

	public String message;
	public int statusCode;
	public boolean successful;
	public T data;

	public ServerResponse(boolean successful, String message, int statusCode, T data) {
		this.successful = successful;
		this.message = message;
		this.statusCode = statusCode;
		this.data = data;
	}

	public ServerResponse(T data){
		this.successful = true;
		this.message = "successful";
		this.statusCode = 200;
		this.data = data;
	}

	public ServerResponse() {
		
	}
}

