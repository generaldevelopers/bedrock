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
import com.gendevs.bedrock.appengine.utils.AppConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "failed_login_attempts")
public class FailedLoginAttempt {

    @Id
    public String username;
    public Date timeEntered;
    public int numberOfTimes;
	
	public boolean isBlocked(String username){
		
		FailedLoginAttempt  fla = RepositoryFactory.getInstance().getFailedLoginAttemptRepository().findByUsername(username);
	
		if(fla == null)
			return false;
		long curtime = new Date().getTime();
		System.out.println("time left: "+(AppConstants.BLOCK_TIME_MILLIS - (curtime - fla.timeEntered.getTime()))/1000);
		if( curtime - fla.timeEntered.getTime() < AppConstants.BLOCK_TIME_MILLIS
				&& fla.numberOfTimes >= AppConstants.MAX_FAIL_ATTEMPTS){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean incrementFailedAttempt(String username){
		
		FailedLoginAttempt  fla = RepositoryFactory.getInstance().getFailedLoginAttemptRepository().findByUsername(username);

		if(fla == null){
			fla = new FailedLoginAttempt();
			fla.timeEntered = new Date();
			fla.username = username;
		}
		
		fla.numberOfTimes++;
		RepositoryFactory.getInstance().getFailedLoginAttemptRepository().save(fla);
		
		return true;
	}

	public boolean removeBlock(String username){
		
		FailedLoginAttempt  fla = RepositoryFactory.getInstance().getFailedLoginAttemptRepository().findByUsername(username);
		
		if(fla == null)
			return false;
		
		RepositoryFactory.getInstance().getFailedLoginAttemptRepository().delete(fla);
		
		return true;
	}

}
