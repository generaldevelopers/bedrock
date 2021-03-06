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

public class MailReceiver {
	
	public MailReceiver(String firstName, String lastName, String email, String imageUrl) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.imageUrl = imageUrl;
	}
	
	public String email;
	public String firstName;
	public String lastName;
	public String imageUrl;
}
