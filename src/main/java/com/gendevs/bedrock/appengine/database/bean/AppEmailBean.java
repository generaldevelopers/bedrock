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

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "Email")
@XmlRootElement
public class AppEmailBean {

	public String name;
	@ApiModelProperty(value = "email of the user", required = true)
	public String email;
	
	public AppEmailBean() {
		
	}
	
	public AppEmailBean(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public static List<AppEmailBean> getUniqueEmails(List<AppEmailBean> appEmailBeans){
		List<AppEmailBean> uniqueEmailBeans = new ArrayList<AppEmailBean>();
		List<String> emails = new ArrayList<String>();
		
		for (AppEmailBean appEmailBean : appEmailBeans){
			if(!emails.contains(appEmailBean.email)){
				emails.add(appEmailBean.email);
				uniqueEmailBeans.add(appEmailBean);
			}
		}
		return uniqueEmailBeans;
	}
}
