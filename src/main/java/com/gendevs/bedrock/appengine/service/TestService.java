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

package com.gendevs.bedrock.appengine.service;

import com.gendevs.bedrock.appengine.database.model.Account;
import com.gendevs.bedrock.appengine.database.dummydata.DummyData;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Date;

import static com.googlecode.objectify.ObjectifyService.ofy;

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

    static {
        ObjectifyService.register(Account.class);
    }

    @GET
	@Path("/appengine")
	public Response testAppEngine(){
		Account account = new Account();
		account.name = "acc name";
        account.id = String.valueOf(new Date().getTime());
		ofy().save().entity(account).now();    // async without the now()

		Result<Account> result = ofy().load().key(Key.create(Account.class, account.id));  // Result is async
		Account fetched1 = result.now();    // Materialize the async value
		System.out.println(fetched1);


		// More likely this is what you will type
		Account fetched2 = ofy().load().type(Account.class).id(account.id).now();
		System.out.println(fetched2);

		// Or you can issue a query
		Account fetched3 = ofy().load().type(Account.class).filter("name", "acc name").first().now();

		System.out.println(fetched3);
		// Change some data and write it
		account.city = "Hyderabad";
		ofy().save().entity(account).now();    // async without the now()

		// Delete it
		//ofy().delete().entity(account).now();    // async without the now()


		return Response.ok().entity("success").build();
	}

}
