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

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.gendevs.bedrock.database.bean.ChangePasswordBean;
import com.gendevs.bedrock.database.model.Token;
import com.gendevs.bedrock.database.model.User;
import com.gendevs.bedrock.repository.RepositoryFactory;
import com.gendevs.bedrock.response.ResponseMessage;


@Path("/forgot-password")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class ForgotPasswordService extends BaseService{
	
	@GET
	@Path("/request")
	public Response request(@QueryParam("username") String username) {
		//UserBean bean = UserDao.getInstance().getSimpleBean(username);
		User user = RepositoryFactory.getInstance().getUserRepository().findByUsername(username);
		
		if (user == null)
			return Response.status(Status.BAD_REQUEST).entity(new ResponseMessage("invalid username/email_id")).build();

		String accessToken = new Token().createForgotPasswordToken(username);

        //TODO: send emails in new HTML format
		/*try {

			Message message = new MimeMessage(new EmailSession().getSession());
			message.setFrom(new InternetAddress(AppConstants.APP_EMAILID));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(username));
			message.setSubject("Reset Bedrock Password");
			message.setText("You have requested for a new password, Click on the Link to Enter the new password."
					+ "<server base url>/portal/resetPassword?token="+accessToken);

			Transport.send(message);
 
			System.out.println("Email Sent!");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}*/
		return Response.status(Status.OK).entity(new ResponseMessage("An email has been sent to the registered user")).build();
	}

	@GET
	@Path("/verify")
	public Response verifyToken(@QueryParam("token") String tokenString) {
		return new Token().getForgotPasswordUserId(tokenString) == null ?
			     Response.status(Status.FORBIDDEN).entity(new ResponseMessage("Invalid Token")).build():
						 Response.status(Status.OK).entity(new ResponseMessage("Valid Token")).build();
	}
	
	@POST
	@Path("/reset")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response reset(@FormParam("password") String password, @QueryParam("token") String tokenString) {
		String userId = new Token().getForgotPasswordUserId(tokenString);
		
		if(userId == null)
			return Response.status(Status.FORBIDDEN).entity(new ResponseMessage("Invalid Token")).build();
		
		ChangePasswordBean ub = new ChangePasswordBean();
		User user = RepositoryFactory.getInstance().getUserRepository().findOne(userId);
		ub.username = user.username;
		ub.oldPassword = user.password;
		ub.newPassword = password;
		
		if (new User().changePassword(ub)){
			new Token().clearForgotPasswordToken(tokenString);
			return Response.status(Status.OK).entity(new ResponseMessage("Succesfully changed password")).build();
		} else{
			return Response.status(Status.NOT_MODIFIED).entity(new ResponseMessage("Unable to change password. Please try again later")).build();
		}
			
	}

}