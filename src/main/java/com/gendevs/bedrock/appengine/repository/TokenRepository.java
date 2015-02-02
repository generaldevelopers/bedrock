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

import com.gendevs.bedrock.appengine.database.model.Token;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {

	Token findByToken(String token);
	Token findByUserId(String userId);
	Token findOneBy(String name);
	List<Token> findByisBlocked(boolean status);
	List<Token> findByUserIdAndType(String userId, String tokenType);
	Token findByTokenAndType(String token, String tokenType);
	
	Token findByValidTillGreaterThanEqual(Date currentTime);	
}
