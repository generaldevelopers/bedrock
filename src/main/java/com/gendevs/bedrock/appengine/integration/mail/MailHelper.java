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

package com.gendevs.bedrock.appengine.integration.mail;

import com.gendevs.bedrock.appengine.database.bean.UserBean;
import com.gendevs.bedrock.appengine.utils.AppConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailHelper {

	public void sendNewAppEmail(UserBean bean, List<MailReceiver> toEmailIds){

		Map<String, Object> model = new HashMap<String, Object>();                 
		model.put("key", bean.firstName);
		model.put("name", bean.email);
		model.put("identifier", bean.designation);
		
		Mail mail = new Mail();
		for (MailReceiver anEmailId : toEmailIds) {
			mail.addReceiver(anEmailId);
		}
		
		mail.setSubject("New App created");
		mail.setTemplateName("app_created.html");
		
		mail.setFrom(AppConstants.APP_EMAILID);
		
		sendEmailInBackground(mail, model);
	}
	
	private void sendEmailInBackground(Mail mail, Map<String, Object> model) {

        //RepositoryFactory.getInstance().getMailer().sendMail(mail, model);

		//Thread mailSender = new Thread(new MailSendRunner(mail, model), "sendEmail");
		//mailSender.start();
	}

}
