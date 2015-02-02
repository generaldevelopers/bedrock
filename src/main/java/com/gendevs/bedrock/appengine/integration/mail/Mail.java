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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mail {
	private String from;
	private List<MailReceiver> receivers = new ArrayList<MailReceiver>();
	private String subject;
	private String content;
	private String templateName;
	private String contentType;
	
	public Mail() {
		contentType = "text/html";
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public void setSubject(String mailSubject) {
		this.subject = mailSubject;
	}
	public String getSubject() {
		return subject;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public void setFrom(String mailFrom) {
		this.from = mailFrom;
	}
	public String getFrom() {
		return from;
	}
	public void addReceiver(MailReceiver mailTo) {
		this.receivers.add(mailTo);
	}
	public List<MailReceiver> getReceivers() {
		return receivers;
	}
	public void setContent(String mailContent) {
		this.content = mailContent;
	}
	public String getContent() {
		return content;
	}
	public Date getSendDate() {
		return new Date();
	}
	@Override
	public String toString() {
		StringBuilder lBuilder = new StringBuilder();
		lBuilder.append("Mail From:- ").append(getFrom());
		lBuilder.append("Mail To:- ").append(getReceivers());
		lBuilder.append("Mail Subject:- ").append(getSubject());
		lBuilder.append("Mail Send Date:- ").append(getSendDate());
		lBuilder.append("Mail Content:- ").append(getContent());
		return lBuilder.toString();
	}
}