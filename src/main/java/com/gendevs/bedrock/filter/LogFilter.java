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

package com.gendevs.bedrock.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.gendevs.bedrock.utils.GDLogger;

public class LogFilter implements Filter{

    private FilterConfig filterConfig;

    public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
    	//new HttpServletRequest(request); 
    	HttpServletRequest requestCasted = (HttpServletRequest) request;
    	String contentType = requestCasted.getContentType();
    	GDLogger.logInfo("LogFilter", "RequestURI " + requestCasted.getRequestURI());
    	GDLogger.logInfo("LogFilter","QueryString "+requestCasted.getQueryString());
    	GDLogger.logInfo("LogFilter","ContentType " + contentType);
    	
    	/*if(contentType != null && (contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_TYPE.toString()) ||
    			contentType.contains(MediaType.APPLICATION_JSON.toString()))) 
    		GDLogger.logInfo("LogFilter","Body "+getBody(requestCasted));
    	*/
    	filterChain.doFilter(request, response);		
	}

	public void init(FilterConfig arg0) throws ServletException {
        this.filterConfig = filterConfig;
		
	}
	
	public void destroy() {
		//System generated method
	}

}
