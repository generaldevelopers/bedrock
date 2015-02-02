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

package com.gendevs.bedrock.appengine.database.model;

import com.gendevs.bedrock.appengine.repository.RepositoryFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "organisations")
public class Organisation {

	@Indexed
	@Id
	public String id;
	@Indexed
	public String name;
	public int status; // see OrganisationStatusType
	public String DBAName;
	public String icon;
	public String email;
	public String phone;
    public String country;
    
    public String subscription; //see SubscriptionType
    
	public boolean isDeleted;

	public String imageUrl;
	public String imageUrlLogo;
	
	public static List<User> getUsers(String organisationId) {
		return RepositoryFactory.getInstance().getUserRepository().findByOrganisationIdAndIsDeleted(organisationId, false);
	}
	
	public static int getUserCount(String organisationId) {
		return getUsers(organisationId).size();
	}

}
