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

package com.gendevs.bedrock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

public interface PagingAndSortingRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
	Iterable<T> findAll(Sort sort);
	Page<T> findAll(Pageable pageable);
}

/*
Accessing the second page of User by a page size of 20 you could simply do something like this:

PagingAndSortingRepository<User, Long> repository = // get access to a bean
Page<User> users = repository.findAll(new PageRequest(1, 20));

// Create & Initialize a Sort Object
		Sort sort = new Sort(Direction.ASC,"employeeName");
		// Create & Initialize a Page Object
		page = this.service.getEmployeeRepository().findAll(new PageRequest(0, 3,sort));
		// Fetch the employees from the page object
		this.employees = page.getContent();
*/
