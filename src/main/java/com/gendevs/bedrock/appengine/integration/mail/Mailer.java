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

import com.gendevs.bedrock.appengine.utils.AppConstants;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

public class Mailer {
	private JavaMailSenderImpl mailSender;
	private VelocityEngine velocityEngine;

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
	public void sendMail(Mail mail, Map<String, Object> model)
			throws MessagingException {

		//TODO: Deepak. not the perfect way to pull resources from the below code
		//but accordng to http://velocity.apache.org/engine/releases/velocity-1.7/developer-guide.html#resourceloaders
		//File resource handelers needs more config for which we don't have enough time.
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false,"utf-8");
		
		helper.setSubject(mail.getSubject());
		helper.setFrom(AppConstants.APP_EMAILID);
		
		for (MailReceiver mailReceiver : mail.getReceivers()) {
			model.put("_receiverFirstName", mailReceiver.firstName);
			model.put("_receiverLastName", mailReceiver.lastName);
			model.put("_receiverEmail", mailReceiver.email);
			model.put("_receiverImageUrl", mailReceiver.imageUrl);
			
			String mailBody = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "com/gendevs/bedrock/appengine/integration/mail/templates/" + mail.getTemplateName(), "UTF-8", model);
			mimeMessage.setContent(mailBody, mail.getContentType());
			
			helper.setTo(mailReceiver.email);
			mailSender.send(mimeMessage);
		}
	}
}