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

package com.gendevs.bedrock.integration.mail;

import java.util.Map;

import javax.mail.MessagingException;

import com.gendevs.bedrock.repository.RepositoryFactory;

public class MailSendRunner implements Runnable {
	 
	Mail mail;
	Map<String, Object> model;
	
    public MailSendRunner(Mail mail, Map<String, Object> model) {
    	this.mail = mail;
    	this.model = model;
	}

	@Override
    public void run() {
        try {
        	doSendEmail();
        } catch (InterruptedException | MessagingException e) {
            e.printStackTrace();
        }
    }
 
    private void doSendEmail() throws InterruptedException, MessagingException {
    	RepositoryFactory.getInstance().getMailer().sendMail(mail, model);
    }

}
