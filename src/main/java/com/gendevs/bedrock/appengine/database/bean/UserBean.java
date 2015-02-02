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

package com.gendevs.bedrock.appengine.database.bean;

import com.gendevs.bedrock.appengine.database.model.User;
import com.gendevs.bedrock.appengine.database.type.UserType;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
@ApiModel(value="User")

@XmlRootElement
public class UserBean {
	
    public String id;    
    @ApiModelProperty( value = "Username to login/ Unique", required = true )
    public String username;    
    @ApiModelProperty( value = "Password to login/ Unique", required = true )
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
    public String validTill;
    
	public boolean isDeleted;
	public String imageUrl;
	
	public UserBean(){}
	
	public UserBean(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public static User getModel(UserBean bean) {
	
		User model = new User();
		model.id = bean.id;
		model.username = bean.username;
		model.password = bean.password;
		model.organisationId = bean.organisationId;
		model.firstName = bean.firstName;
		model.lastName = bean.lastName;
		model.email = bean.email;
		model.phone = bean.phone;
		model.designation = bean.designation;
		model.subscription = bean.subscription;
		
		model.isDeleted = bean.isDeleted;
		model.imageUrl = bean.imageUrl;
		
		model.role = bean.role;
		
		if (bean.type == null)
			model.type = UserType.ACTIVE;
		else
			model.type = bean.type;
		
		if(bean.validTill != null) {
			model.validTill = DatatypeConverter.parseDateTime(bean.validTill).getTime();
		}
		return model;
	}

	public static UserBean getBean(User model) {
		
		UserBean bean = new UserBean();
		bean.id = model.id;
		bean.username = model.username;
		bean.firstName = model.firstName;
		bean.organisationId = model.organisationId;
		bean.lastName = model.lastName;
		bean.email = model.email;
		bean.phone = model.phone;
		bean.designation = model.designation;
		bean.subscription = model.subscription;
		
		bean.isDeleted = model.isDeleted;
		bean.imageUrl = model.imageUrl;
		
		bean.role = model.role;
		bean.type = model.type;
		if (model.validTill != null) {		
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(model.validTill); 
	        bean.validTill = DatatypeConverter.printDateTime(cal);
		}
		
		return bean;
	}
	
	public static List<UserBean> getBeans(List<User> model) {

		List<UserBean> beans = new ArrayList<UserBean>();
		for (User data : model) {
			beans.add(getBean(data));
		}
		return beans;
	}
}
