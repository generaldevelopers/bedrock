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

import com.gendevs.bedrock.appengine.database.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	User findByUsername(String username);
	User findByIdAndIsDeleted(String id, boolean state);
	User findByUsernameAndPassword(String username, String password);
	List<User> findByOrganisationIdAndIsDeleted(String organisationId, boolean state);

	List<User> findByisDeleted(boolean status);
	
	User findByUsernameAndOrganisationIdAndIsDeleted(String username, String organisationId, boolean state);
}
