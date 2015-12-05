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

package com.gendevs.bedrock.service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.gendevs.bedrock.database.dummydata.DummyData;

@Path("/test")
public class TestService extends BaseService{

	@GET
	@Path("/create-dummy")
	public Response createDummy() throws IOException, ParseException {
		try {
			new DummyData().createDummyData();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Response.ok("dummy data created").build();
	}
}
