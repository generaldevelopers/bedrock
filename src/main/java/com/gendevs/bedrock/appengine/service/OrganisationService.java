
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

import com.gendevs.bedrock.appengine.database.bean.OrganisationBean;
import com.gendevs.bedrock.appengine.database.bean.UserBean;
import com.gendevs.bedrock.appengine.database.model.User;
import com.gendevs.bedrock.appengine.database.type.UserRoleType;
import com.gendevs.bedrock.appengine.repository.RepositoryFactory;
import com.gendevs.bedrock.appengine.response.ResponseMessage;
import com.gendevs.bedrock.appengine.response.ServerResponse;
import com.gendevs.bedrock.appengine.utils.EncryptDecrypt;
import com.gendevs.bedrock.appengine.database.model.Organisation;
import com.gendevs.bedrock.appengine.utils.AppConstants;
import com.wordnik.swagger.annotations.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;


@Path("/organisations")
@Api(value = "organisations", description = "Organisation Service to add delete edit and fetch organisations")

public class OrganisationService extends BaseService {

	@GET
	@Path("/{id}")	
	@ApiOperation(value = "Get OrganistaionBean", notes = "Fetches Organisation details of a given organisationId", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 403, message = "Wrong organisationId or User not allowed"),  
		@ApiResponse(code = 200, message = "OK") })
	public Response getOrganisationBean(@Context HttpServletRequest hh, @ApiParam(value = "organisationId to fetch the details", required = true) @PathParam("id") String organisationId) {
		String userId = hh.getAttribute(AppConstants.USER_ID_KEY).toString();


		Organisation organisation = RepositoryFactory.getInstance().getOrganisationRepository().findByIdAndIsDeleted(organisationId, false);
		OrganisationBean organisationBean = OrganisationBean.getBean(organisation);

		return Response.status(Status.OK).entity(organisationBean).build();
	}
	
	@POST
	@ApiOperation(value = "Create New Organisation", notes = "Creates a organisation and displays its contents", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 201, message = "CREATED") })
	public Response createNewOrganisation(@Context HttpServletRequest hh, @ApiParam(value = "Organisation details needed to create a new organisation", required = true) OrganisationBean bean) {

		if (bean == null)
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("Wrong Data Format!")).build();
		
		if (bean.subscription == null)
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("Input subscriptionType = FREE/REGULAR/PREMIUM ")).build();
			
		Organisation organisation = RepositoryFactory.getInstance().getOrganisationRepository().save(OrganisationBean.getModel(bean));
		bean = OrganisationBean.getBean(organisation);
		return Response.status(200).entity(bean).build();
		
	}

	@POST
	@Path("/sign-up")
	@ApiOperation(value = "Create New Organisation", notes = "Creates a organisation and displays its contents", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 201, message = "CREATED") })
	public Response createNewOrganisationSignUp(@ApiParam(value = "Organisation details needed to create a new organisation", required = true) OrganisationBean bean) {
		
		if (bean == null)
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("Wrong Data Format!")).build();

		if (bean.subscription == null)
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("Input subscriptionType = FREE/REGULAR/PREMIUM ")).build();
	
		if (RepositoryFactory.getInstance().getUserRepository().findByUsername(bean.userBean.username) != null)
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("User Exists with username")).build();

		if(!this.validate(bean.userBean.username))
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("Invalid Username")).build();

		Organisation organisation = RepositoryFactory.getInstance().getOrganisationRepository().save(OrganisationBean.getModel(bean));

		bean.userBean.organisationId = organisation.id;
		bean.userBean.subscription = organisation.subscription;
		bean.userBean.role = UserRoleType.ADMIN;
		bean.userBean.password = EncryptDecrypt.getSha2Hash(bean.userBean.password);

		RepositoryFactory.getInstance().getUserRepository().save(UserBean.getModel(bean.userBean));
		
		bean = OrganisationBean.getBean(organisation);
		
		return Response.status(200).entity(bean).build();	
	}
	
	@GET
	@ApiOperation(value = "Fetch all Organisations", notes = "Displays all organisations present in the database with their details", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "OK") })
	public Response getAllOrganisations(@Context HttpServletRequest hh) {

		List<Organisation> organisations = RepositoryFactory.getInstance().getOrganisationRepository().findByisDeleted(false);
		if ( organisations.isEmpty())
		return Response.noContent().build();
		GenericEntity<List<OrganisationBean>> entity = new GenericEntity<List<OrganisationBean>>(OrganisationBean.getBeans(organisations)) {};

	    return Response.status(200).entity(entity).build();
	}

	@PUT
	@Path("/{id}")
	@ApiOperation(value = "Edit Organisation", notes = "Edits and Updates organisation details", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 401, message = "InValid organisationId or Not Allowed to Access"),
			@ApiResponse(code = 202, message = "ACCEPTED") })
	public Response editOrganisation(@Context HttpServletRequest hh, @ApiParam(value = "OrganisationId of the Organisation to update", required = true)  @PathParam("id") String organisationId, 
			@ApiParam(value = "Organisation details to edit", required = true) OrganisationBean organisationBean) {
		String userId = hh.getAttribute(AppConstants.USER_ID_KEY).toString();

		if (organisationBean == null)
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("Wrong Data Format!")).build();

		Organisation organisation = RepositoryFactory.getInstance().getOrganisationRepository().findByIdAndIsDeleted(organisationId, false);

		organisation = RepositoryFactory.getInstance().getOrganisationRepository().save(OrganisationBean.getModel(organisationBean));

		return Response.status(Status.ACCEPTED).entity(OrganisationBean.getBean(organisation)).build();

	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Delete Organisation", notes = "Deletes Organisation by the given organisationId", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 401, message = "InValid id or Not Allowed to Access"),
			@ApiResponse(code = 200, message = "Successfully Deleted!") })
	public Response deleteOrganisation(@Context HttpServletRequest hh, 
			@ApiParam(value = "OrganisationId of the organisation for deletion", required = true) @PathParam("id") String organisationId) {
		String userId = hh.getAttribute(AppConstants.USER_ID_KEY).toString();

		Organisation organisation = RepositoryFactory.getInstance().getOrganisationRepository().findByIdAndIsDeleted(organisationId, false);
		organisation.isDeleted = true;
		RepositoryFactory.getInstance().getOrganisationRepository().save(organisation);

		return Response.status(Status.OK).entity(new ResponseMessage("Successfully Deleted!")).build();

	}
	
	@GET
	@Path("/{id}/users")
	@ApiOperation(value = "Fetch all Organisations users", notes = "Displays all users of the organisation present in the database with their details", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 401, message = "InValid organisationId or Not Allowed to Access"), @ApiResponse(code = 200, message = "OK") })
	public Response getUsersForOrganisation(@Context HttpServletRequest hh,
			@ApiParam(value = "users' OrganisationId of the users to fetch", required = true) @PathParam("id") String organisationId) {

		String userId = hh.getAttribute(AppConstants.USER_ID_KEY).toString();

		List<User> users = RepositoryFactory.getInstance().getUserRepository().findByOrganisationIdAndIsDeleted(organisationId, false);
		if ( users.isEmpty())
		return Response.noContent().build();
		GenericEntity<List<UserBean>> entity = new GenericEntity<List<UserBean>>(UserBean.getBeans(users)) {
		};

		return Response.status(200).entity(entity).build();
	}
	
}