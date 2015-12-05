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

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.gendevs.bedrock.database.model.User;
import com.gendevs.bedrock.repository.RepositoryFactory;
import com.gendevs.bedrock.response.ResponseMessage;
import com.gendevs.bedrock.response.ServerResponse;
import com.gendevs.bedrock.service.storage.StorageConstants;
import com.gendevs.bedrock.service.storage.StorageProvider;
import com.gendevs.bedrock.utils.AppConstants;
import com.sun.jersey.multipart.FormDataParam;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/file-upload")
@Api(value = "file-upload", description = "upload files/images with ids")

public class BaseImageService {

	private static final String APP_IMAGE_TYPE = "app";
	private static final String USER_IMAGE_TYPE = "user";

	@POST
	@Path("/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@ApiOperation(value = "Upload Image", notes = "Uploads image according to its type and id and saves it in server", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 400, message = "User Exists with username"),
			@ApiResponse(code = 201, message = "CREATED") })
	public Response uploadImage(@Context HttpServletRequest hh, 
			@ApiParam(value = "UserId/AppKey ", required = true) @PathParam("id") String id,
			@ApiParam(value = "type = user/app", required = true) @QueryParam("type") String type,
			@ApiParam(value = "image file", required = true) @FormDataParam("image") InputStream imageInputStream) {

		if (id == null || type == null || (!type.equalsIgnoreCase(APP_IMAGE_TYPE) && !type.equalsIgnoreCase(USER_IMAGE_TYPE)) || imageInputStream == null)
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("userId, type=app/user, image is mandatory!")).build();

		if(type.equalsIgnoreCase(USER_IMAGE_TYPE)) {
			User user = RepositoryFactory.getInstance().getUserRepository().findByIdAndIsDeleted(id, false);
			if (user == null)
				return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("user doesnt exist with given id")).build();
			user.imageUrl = new StorageProvider().uploadImageFile(imageInputStream, user.organisationId, user.id, type);
			RepositoryFactory.getInstance().getUserRepository().save(user);
		}
		
		return Response.status(Status.CREATED).entity(new ResponseMessage("image uploaded!")).build();
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Delete User/App profile photo", notes = "Deletes User by the given userId/ App by give appKey", response = ServerResponse.class)
	@ApiResponses({ @ApiResponse(code = 401, message = "InValid id or Not Allowed to Access"),
			@ApiResponse(code = 200, message = "Successfully Deleted!") })
	public Response deleteImage(@Context HttpServletRequest hh, 
			@ApiParam(value = "UserId of the user for deletion", required = true) @PathParam("id") String id,
			@ApiParam(value = "type = user/app", required = true) @QueryParam("type") String type) {
		String userId = hh.getAttribute(AppConstants.USER_ID_KEY).toString();
		
		switch (type) {
		
			case USER_IMAGE_TYPE : 
				User user = RepositoryFactory.getInstance().getUserRepository().findOne(id);
				if (user.imageUrl == null)
					return Response.status(Status.UNAUTHORIZED)
							.entity(new ResponseMessage("imageUrl not available!")).build();
				
				user.imageUrl = user.imageUrl.substring(AppConstants.URL_TO_SLICE.length());
				if (new StorageProvider().deleteFile(StorageConstants.BUCKET_NAME, user.imageUrl)) {
					user.imageUrl = null;
					RepositoryFactory.getInstance().getUserRepository().save(user);
					return Response.status(Status.OK).entity(new ResponseMessage("Successfully Deleted!")).build();
				}	
		}
		return Response.status(Status.OK).entity(new ResponseMessage("Not Deleted!")).build();
	}
	
}