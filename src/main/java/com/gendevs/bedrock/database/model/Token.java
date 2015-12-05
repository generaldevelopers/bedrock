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

package com.gendevs.bedrock.database.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.gendevs.bedrock.database.type.TokenType;
import com.gendevs.bedrock.repository.RepositoryFactory;
import com.gendevs.bedrock.utils.AppConstants;
import com.gendevs.bedrock.utils.UniqueIdGenerator;

@Document(collection = "tokens")
public class Token {
	@Id
	public String token;
	public String userId;
	public Date validTill;
	public String type;
	public String password;
	public boolean isBlocked;
	public boolean isDeleted;

	public String getAuthUserId(String accessToken) {

		return getUserId(accessToken, TokenType.ACCESS_TOKEN);
	}

	public String getForgotPasswordUserId(String accessToken) {

		return getUserId(accessToken, TokenType.FORGOT_PASSWORD);
	}

	private String getUserId(String accessToken, String tokenType) {
		Date currentTime = new Date();

		Token fetchedToken = RepositoryFactory.getInstance().getTokenRepository()
				.findByTokenAndType(accessToken, tokenType);

		if (fetchedToken != null && fetchedToken.isBlocked == false
				&& fetchedToken.validTill.compareTo(currentTime) > 0)// validTillDate is after currentTime
			return fetchedToken.userId;
		else
			return null;
	}

	public boolean blockAuthOtherTokens(String username) {
		return blockOtherTokens(username, TokenType.ACCESS_TOKEN);
	}

	public boolean blockForgorPasswordOtherTokens(String username) {
		return blockOtherTokens(username, TokenType.FORGOT_PASSWORD);
	}

	private boolean blockOtherTokens(String username, String tokenType) {
		User user = RepositoryFactory.getInstance().getUserRepository().findByUsername(username);
		List<Token> tokens = RepositoryFactory.getInstance().getTokenRepository()
				.findByUserIdAndType(user.id, tokenType);

		for (Token record : tokens) {
			record.isBlocked = true;
			RepositoryFactory.getInstance().getTokenRepository().save(record);
		}
		return true;
	}

	public boolean clearForgotPasswordToken(String token) {
		return clearToken(token, TokenType.FORGOT_PASSWORD);
	}

	public boolean clearAuthToken(String token) {
		return clearToken(token, TokenType.ACCESS_TOKEN);
	}

	public boolean clearToken(String token, String tokenType) {
		Token fetchedToken = RepositoryFactory.getInstance().getTokenRepository().findByTokenAndType(token, tokenType);

		if (fetchedToken != null) {
			fetchedToken.isBlocked = true;
			RepositoryFactory.getInstance().getTokenRepository().save(fetchedToken);
			return true;
		}
		return false;
	}

	public String createAuthToken(String username) {

		return createToken(username, TokenType.ACCESS_TOKEN);
	}

	public String createForgotPasswordToken(String username) {

		return createToken(username, TokenType.FORGOT_PASSWORD);
	}

	private String createToken(String username, String tokenType) {
		Token token = new Token();
		//token.token = UniqueIdGenerator.getInstance().getAuthToken();
		token.userId = RepositoryFactory.getInstance().getUserRepository().findByUsername(username).id;
		token.type = tokenType;
		token.validTill = UniqueIdGenerator.getInstance().getTimeAfterDays(AppConstants.TIME_OUT_DAYS);
		token.isBlocked = false;

		RepositoryFactory.getInstance().getTokenRepository().save(token);

		return token.token;
	}

}
