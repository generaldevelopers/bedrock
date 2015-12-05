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

package com.gendevs.bedrock.database.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.gendevs.bedrock.database.model.Organisation;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="Organisation")

@XmlRootElement
public class OrganisationBean {
	
	public String id;
	public String name;
	public int status; //see OrganisationStatusType
    	@ApiModelProperty( value = "DBAName of the Organisation/ Unique", required = true )
	
	public String DBAName;
	public String icon;
    public String email;
    public String phone;
    public String country;
    
    public String subscription; //see SubscriptionType
	
	public UserBean userBean;
	public int crashCount;
	public int userCount;
	public int appCount;
	public int imageCount;
	public int videoCount;
	
	public int maxCrashes;
	public int maxUsers;
	public int maxApps;
	public int maxImages;
	public int maxVideos;
	
	public boolean isDeleted;
	
	public String imageUrl;
	public String imageUrlLogo;

	public static Organisation getModel(OrganisationBean bean) {
	
		Organisation model = new Organisation();
		model.id = bean.id;
		model.name = bean.name;
		model.status = bean.status;
		model.DBAName = bean.DBAName;
		model.icon = bean.icon;
		model.email = bean.email;
		model.phone = bean.phone;
		model.country = bean.country;
		
		model.subscription = bean.subscription;
		
		model.isDeleted = bean.isDeleted;
		model.imageUrl = bean.imageUrl;
		
		return model;
	}

	public static OrganisationBean getBean(Organisation model) {
		
		OrganisationBean bean = new OrganisationBean();
		bean.id = model.id;
		bean.name = model.name;
		bean.status = model.status;
		bean.DBAName = model.DBAName;
		bean.icon = model.icon;
		bean.email = model.email;
		bean.phone = model.phone;
		bean.country = model.country;
		
		bean.userCount = Organisation.getUserCount(model.id);

		bean.subscription = model.subscription;

		bean.isDeleted = model.isDeleted;
		bean.imageUrl = model.imageUrl;
		
		return bean;
	}
	
	public static List<OrganisationBean> getBeans(List<Organisation> model) {

		List<OrganisationBean> beans = new ArrayList<OrganisationBean>();
		for (Organisation data : model) {
			beans.add(getBean(data));
		}
		return beans;
	}
}
