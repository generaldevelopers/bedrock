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

import com.gendevs.bedrock.appengine.database.model.FailedLoginAttempt;
import com.gendevs.bedrock.appengine.database.model.User;
import com.gendevs.bedrock.appengine.repository.RepositoryFactory;
import com.gendevs.bedrock.appengine.response.ResponseMessage;
import com.gendevs.bedrock.appengine.database.bean.ChangePasswordBean;
import com.gendevs.bedrock.appengine.database.bean.UserBean;
import com.gendevs.bedrock.appengine.database.model.Organisation;
import com.gendevs.bedrock.appengine.database.model.Token;
import com.gendevs.bedrock.appengine.database.type.UserType;
import com.gendevs.bedrock.appengine.response.ServerResponse;
import com.gendevs.bedrock.appengine.response.TokenResponse;
import com.gendevs.bedrock.appengine.utils.AppConstants;
import com.gendevs.bedrock.appengine.utils.EncryptDecrypt;
import com.wordnik.swagger.annotations.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path("/users")
@Api(value = "users", description = "Users Service to add delete edit and fetch users")

public class UserService extends BaseService {

	@GET
	@Path("/{username}")
	@ApiOperation(value = "Get UserBean", notes = "Fetches User details of a given username", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 401, message = "InValid Username"), @ApiResponse(code = 401, message = "InValid User or Not Allowed to Access"), @ApiResponse(code = 200, message = "OK") })
	public Response getUserBean2(@Context HttpServletRequest hh,@ApiParam(value = "username to fetch the details", required = true) @PathParam("username") String username) {

		String userId = hh.getAttribute(AppConstants.USER_ID_KEY).toString();
		User user = RepositoryFactory.getInstance().getUserRepository().findByUsername(username);
		if (user == null)
			return Response.status(Status.UNAUTHORIZED).entity(new ResponseMessage("InValid Username")).build();

		UserBean userBean = UserBean.getBean(user);
		userBean.password = null;
		return Response.status(Status.ACCEPTED).entity(userBean).build();
	}

	@POST
	@ApiOperation(value = "Create New User", notes = "Creates a user and displays its contents", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 400, message = "User Exists with username"),
			@ApiResponse(code = 201, message = "CREATED") })
	public Response createNewUser(@Context HttpServletRequest hh, @ApiParam(value = "User details needed to create a new user", required = true) UserBean bean) {

		String userId = hh.getAttribute(AppConstants.USER_ID_KEY).toString();

		if (bean == null)
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("Wrong Data Format!")).build();

		if (RepositoryFactory.getInstance().getUserRepository().findByUsername(bean.username) != null)
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("User Exists with username")).build();

		User loggedInUser = RepositoryFactory.getInstance().getUserRepository().findOne(userId);
		Organisation organisation = RepositoryFactory.getInstance().getOrganisationRepository().findOne(loggedInUser.organisationId);
		
		bean.subscription = organisation.subscription;
		bean.organisationId = organisation.id;
		bean.password = EncryptDecrypt.getSha2Hash(bean.password);
		
		User user = RepositoryFactory.getInstance().getUserRepository().save(UserBean.getModel(bean));
		return Response.status(201).entity(UserBean.getBean(user)).build();
	}

	@POST
	@Path("/sign-up")
	@ApiOperation(value = "SignUp New User", notes = "Creates a user and displays its contents", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 400, message = "User Exists with username"),
		@ApiResponse(code = 400, message = "Invalid Username"),
			@ApiResponse(code = 201, message = "CREATED") })
	public Response createNewUserSignUp(@ApiParam(value = "User details needed to create a new user", required = true) UserBean bean) {

		if (bean == null)
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("Wrong Data Format!")).build();

		if (RepositoryFactory.getInstance().getUserRepository().findByUsername(bean.username) != null)
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("User Exists with username")).build();

		if(!this.validate(bean.username))
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("Invalid Username")).build();
		
		Organisation organisation = new Organisation();
		if (bean.organisationId != null) {
			organisation = RepositoryFactory.getInstance().getOrganisationRepository().findOne(bean.organisationId);
			bean.subscription = organisation.subscription;
		}
		
		if (bean.subscription == null)
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("Mention subscription = FREE/REGULAR/PREMIUM ")).build();
		
		bean.password = EncryptDecrypt.getSha2Hash(bean.password);

		User user = RepositoryFactory.getInstance().getUserRepository().save(UserBean.getModel(bean));
		return Response.status(201).entity(UserBean.getBean(user)).build();
	}
	
	@POST
	@Path("/token")
	@ApiOperation(value = "Fetch an access token", notes = "Creates an access token in server side and returns it as a response", response = TokenResponse.class)
	@ApiResponses({ @ApiResponse(code = 406, message = "User Blocked"),
			@ApiResponse(code = 406, message = "Provide Username and Password"),
			@ApiResponse(code = 401, message = "UnAuthorised User"),
			@ApiResponse(code = 200, message = "Fetched successful token"), })
	public Response getAuthToken(@ApiParam(value = "Username and Password to authenticate with", required = true) UserBean userBean) {

		if (userBean == null)
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("Wrong Data Format!")).build();

		FailedLoginAttempt fla = new FailedLoginAttempt();
		Token token = new Token();

		if (fla.isBlocked(userBean.username))
			return Response.status(406).entity("User blocked").build();
		if (!new User().isAuthorized(userBean)) {
			fla.incrementFailedAttempt(userBean.username);
			if (fla.isBlocked(userBean.username))
				return Response.status(406).entity(new ResponseMessage("User blocked")).build();
			else
				return Response.status(401).entity(new ResponseMessage("UnAuthorised User")).build();
		} else {
			User user = RepositoryFactory.getInstance().getUserRepository()
					.findByUsername(userBean.username);
			if (user.type.equalsIgnoreCase(UserType.INACTIVE)) {
				user.type = UserType.ACTIVE;
				RepositoryFactory.getInstance().getUserRepository().save(user);
			}
			fla.removeBlock(userBean.username);
			//token.blockAuthOtherTokens(userBean.username);
			TokenResponse response = new TokenResponse(token.createAuthToken(userBean.username), UserBean.getBean(user));
			return Response.status(200).entity(response).build();
		}
	}

	@POST
	@Path("/change-password")
	@ApiOperation(value = "Change User Password", notes = "Changes User Password and Updates it", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 406, message = "Username and Passwords are not provided"),
			@ApiResponse(code = 401, message = "Invalid username or Not Allowed to Access"),
			@ApiResponse(code = 400, message = "Unable to change password. Ensure old password is correct"),
			@ApiResponse(code = 200, message = "Succesfully changed password") })
	public Response changePassword(@Context HttpServletRequest hh, @ApiParam(value = "Details to change the password for the username", required = true) ChangePasswordBean ub) {

		String userId = hh.getAttribute(AppConstants.USER_ID_KEY).toString();

		if (ub == null)
			return Response.status(406).entity(new ResponseMessage("Username and Passwords are not provided")).build();

		User user = RepositoryFactory.getInstance().getUserRepository().findByUsername(ub.username);

		if (!user.changePassword(ub))
			return Response.status(Status.BAD_REQUEST)
					.entity(new ResponseMessage("Unable to change password. Ensure old password is correct")).build();
		else
			return Response.status(200).entity(new ResponseMessage("Succesfully changed password")).build();
	}

	@GET
	@ApiOperation(value = "Fetch all Users", notes = "Displays all users present in the database with their details", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "OK") })
	public Response getAllUsers(@Context HttpServletRequest hh) {

		List<User> users = RepositoryFactory.getInstance().getUserRepository().findByisDeleted(false);
		if ( users.isEmpty())
		return Response.noContent().build();
		GenericEntity<List<UserBean>> entity = new GenericEntity<List<UserBean>>(UserBean.getBeans(users)) {
		};

		return Response.status(200).entity(entity).build();
	}


	@PUT
	@Path("/{id}")
	@ApiOperation(value = "Edit User", notes = "Edits and Updates user details", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 401, message = "InValid userId or Not Allowed to Access"),
			@ApiResponse(code = 202, message = "ACCEPTED") })
	public Response editUser(@Context HttpServletRequest hh, @ApiParam(value = "UserId of the User to update", required = true) @PathParam("id") String userId,
			@ApiParam(value = "User details to edit", required = true) UserBean userBean) {
		String ownerId = hh.getAttribute(AppConstants.USER_ID_KEY).toString();

		if (userBean == null)
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("Wrong Data Format!")).build();

		User user = RepositoryFactory.getInstance().getUserRepository().findByIdAndIsDeleted(userId, false);

		userBean.id = userId;
		userBean.password = user.password;
		user = RepositoryFactory.getInstance().getUserRepository().save(UserBean.getModel(userBean));

		return Response.status(Status.ACCEPTED).entity(UserBean.getBean(user)).build();

	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Delete User", notes = "Deletes User by the given userId", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 401, message = "InValid id or Not Allowed to Access"),
			@ApiResponse(code = 200, message = "Successfully Deleted!") })
	public Response deleteUser(@Context HttpServletRequest hh, 
			@ApiParam(value = "UserId of the user for deletion", required = true) @PathParam("id") String id) {
		String userId = hh.getAttribute(AppConstants.USER_ID_KEY).toString();

		User user = RepositoryFactory.getInstance().getUserRepository().findOne(id);
		user.isDeleted = true;
		RepositoryFactory.getInstance().getUserRepository().save(user);

		return Response.status(Status.OK).entity(new ResponseMessage("Successfully Deleted!")).build();

	}
}
