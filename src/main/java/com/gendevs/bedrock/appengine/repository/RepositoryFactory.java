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

package com.gendevs.bedrock.appengine.repository;

import com.gendevs.bedrock.appengine.integration.mail.Mailer;
import com.gendevs.bedrock.appengine.utils.GDLogger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class RepositoryFactory {

	private ApplicationContext context;
	private static RepositoryFactory instance;
	
	private ApplicationContext getContext(){
		if (context == null){
			context = new GenericXmlApplicationContext("SpringConfig.xml");
			GDLogger.logInfo("BaseService", "initialized SpringConfig");
		}
		return context;
	}
	public static RepositoryFactory getInstance(){
		if (instance == null){
			instance = new RepositoryFactory();
		}
		return instance;
	}

	public UserRepository getUserRepository() {
		return getContext().getBean(UserRepository.class);
	}
	
	public OrganisationRepository getOrganisationRepository() {
		return getContext().getBean(OrganisationRepository.class);
	}
	
	public TokenRepository getTokenRepository() {
		return getContext().getBean(TokenRepository.class);
	}
	
	public FailedLoginAttemptRepository getFailedLoginAttemptRepository() {
		return getContext().getBean(FailedLoginAttemptRepository.class);
	}

	public Mailer getMailer() {
		return (Mailer) getContext().getBean(Mailer.class);
	}
	

}
