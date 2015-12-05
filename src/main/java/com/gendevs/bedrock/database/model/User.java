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

package com.gendevs.bedrock.database.model;


import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.gendevs.bedrock.database.bean.ChangePasswordBean;
import com.gendevs.bedrock.database.bean.UserBean;
import com.gendevs.bedrock.database.type.UserRoleType;
import com.gendevs.bedrock.repository.RepositoryFactory;

@Document(collection = "users")
public class User
{
	@Id public String id;
	
    public String username;
    public String password;
    public String organisationId;
    public String firstName;
    public String lastName;
    public String email;
    public String phone;
    public String designation;
    public String subscription; //see SubscriptionType
    public String role;
    public String type;
    public Date validTill;
    
	public boolean isDeleted;
	public String imageUrl;

	public boolean isAuthorized(UserBean ub) {

		User user = RepositoryFactory.getInstance().getUserRepository().findByUsernameAndPassword(ub.username, ub.password);
		if(user == null)
			return false;
		return true;
	}
	
	public boolean changePassword(ChangePasswordBean changePasswordBean){
		
		User user = RepositoryFactory.getInstance().getUserRepository().findByUsername(changePasswordBean.username);
        if (user == null)
			return false;
       
        if(user.password.equalsIgnoreCase(changePasswordBean.oldPassword)){
        		//Old password matches with database password
        		user.password = changePasswordBean.newPassword;
        		RepositoryFactory.getInstance().getUserRepository().save(user);

        		return true;
        } else {
        		//Passwords doesn't match return error
        		return false;
        }
	}
	
	public static boolean isValidUser(String userId) {
		User user = RepositoryFactory.getInstance().getUserRepository().findOne(userId);
		if (user == null || user.isDeleted )
			return false;
		return true;
		//return RepositoryFactory.getInstance().getUserRepository().findOne(userId) != null ? true : false;
	}
	
	public static boolean isValidOwner(String ownerId, String userId) {
		User owner = RepositoryFactory.getInstance().getUserRepository().findOne(ownerId);
		User user = RepositoryFactory.getInstance().getUserRepository().findOne(userId);
		
		if(owner.role.equalsIgnoreCase(UserRoleType.ADMIN) && user.role.equalsIgnoreCase(UserRoleType.USER) && user.organisationId.equals(owner.organisationId))
			return true;
		
		return false;
	}

}
