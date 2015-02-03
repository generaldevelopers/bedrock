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

package com.gendevs.bedrock.appengine.filter;

import com.gendevs.bedrock.appengine.database.model.Token;
import com.gendevs.bedrock.appengine.json.CommonJsonBuilder;
import com.gendevs.bedrock.appengine.repository.RepositoryFactory;
import com.gendevs.bedrock.appengine.response.ServerResponse;
import com.gendevs.bedrock.appengine.utils.GDLogger;
import com.gendevs.bedrock.appengine.utils.AppConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AuthFilter implements Filter {

	private FilterConfig filterConfig;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
		httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, DELETE, PUT");
		httpResponse.setHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept, access_token");

        String accessToken = getAccessToken(httpRequest);
        //String userId = getUserId(getAccessToken(httpRequest));
        //request.setAttribute(AppConstants.USER_ID_KEY, userId);

        if(httpRequest.getRequestURI().startsWith("/rest/api-docs")){
            GDLogger.logWarning("AuthFilter", "API docs URL so performing request");
            filterChain.doFilter(request, response);
            return;
        }else if (isExemptedUrl(httpRequest)) {
            GDLogger.logWarning("AuthFilter", "Exempted URL so performing request");
            filterChain.doFilter(request, response);
            return;
        } else if (httpRequest.getParameter("api_key") != null) {
            //from swagger
            if(httpRequest.getParameter("api_key").equals(AppConstants.API_KEY_SWAGGER)){
                GDLogger.logWarning("AuthFilter", "From Swagger documentation. Perform request");
                filterChain.doFilter(request, response);
            } else {
                GDLogger.logWarning("AuthFilter", "Not a valid swagger API key");
                getAccessDeniedResponse(httpResponse, "Not a valid swagger API key", Status.UNAUTHORIZED.getStatusCode(),
                        response.getWriter());
            }
            return;
        } else if (accessToken == null) {
            GDLogger.logWarning("AuthFilter", "Access token not specified in header");
            getAccessDeniedResponse(httpResponse, "Access token not specified in header", Status.UNAUTHORIZED.getStatusCode(),
                    response.getWriter());
            return;
        } else if (!validateAccessToken(accessToken)) {
            GDLogger.logWarning("AuthFilter", "Not a valid access token");
            getAccessDeniedResponse(httpResponse, "Not a valid access token", Status.UNAUTHORIZED.getStatusCode(),
                    response.getWriter());
            return;
        }

        String userId = getUserId(getAccessToken(httpRequest));
        request.setAttribute(AppConstants.USER_ID_KEY, userId);

        filterChain.doFilter(request, response);

		return;
	}

	private void getAccessDeniedResponse(HttpServletResponse httpResponse, String message, int statusCode, PrintWriter printWriter) {
		printWriter.println(CommonJsonBuilder.getJsonForEntity(new ServerResponse<Object>(false, message, statusCode,
                null)));
		printWriter.close();
		
	}

	private boolean isExemptedUrl(HttpServletRequest requestCasted) {


		List<String> postExemptedList = new ArrayList<String>();
		List<String> getExemptedList = new ArrayList<String>();
		
		String url = requestCasted.getRequestURI();

		switch(requestCasted.getMethod()) {
			
			case "GET" :
				getExemptedList.add("/rest/test/mail");
				getExemptedList.add("/rest/test/create-dummy");
                getExemptedList.add("/rest/test/appengine");
                getExemptedList.add("/rest/forgot-password/request");
				getExemptedList.add("/rest/forgot-password/verify");

			for (String string : getExemptedList) {
				if (string.equalsIgnoreCase(requestCasted.getRequestURI()))
					return true;
			}
			break;
			
			case "POST" :
				postExemptedList.add("/rest/users/token");
				postExemptedList.add("/rest/organisations/sign-up");
				postExemptedList.add("/rest/users/sign-up");
				postExemptedList.add("/rest/forgot-password/reset");
				
			for (String string : postExemptedList) {
				if (string.equalsIgnoreCase(requestCasted.getRequestURI()))
					return true;
			}
			break;
		}
		return false;

	}

	private String getAccessToken(HttpServletRequest request) {
		return request.getHeader("access_token");
	}

	private boolean validateAccessToken(String accessToken) {
		return getUserId(accessToken) != null ? true : false;
	}

	private String getUserId(String accessToken) {
		return (new Token().getAuthUserId(accessToken));
	}

	public void init(FilterConfig arg0) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

}
