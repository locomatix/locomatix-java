/**
 * Copyright 2011 Locomatix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.locomatix.http;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.locomatix.HttpMethod;
import com.locomatix.LocomatixRequest;

public class HttpRequest {

	private Map<String, String> headers = new HashMap<String, String>();
	private List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	private HttpMethod method;
	private URI endpoint;
	private String path;
	
	private LocomatixRequest originalRequest;
	
	
	public HttpRequest(HttpMethod method) {
		this.method = method;
	}
	
	public HttpRequest(HttpMethod method, String path) {
		this(method);
		this.path = path;
	}
	
	
	public HttpMethod getMethod() {
		return method;
	}
	
	
	public URI getEndpoint() {
		return endpoint;
	}
	
	public void setEndpoint(URI endpoint) {
		this.endpoint = endpoint;
	}
	
	public String getPath() {
		return path;
	}
	
	
	public void addHeader(String name, String value) {
		headers.put(name, value);
	}
	
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public Map<String, String> getHeaders() {
		return headers;
	}
	
	
	public void addParameter(String name, String value) {
		parameters.add(new BasicNameValuePair(name, value));
	}
	
	public void addParameters(Map<String, String> parameters) {
		for(Map.Entry<String, String> entry : parameters.entrySet()) {
			this.parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
	}
	
	
	public List<NameValuePair> getParameters() {
		return parameters;
	}
	
	
	public void setOriginalRequest(LocomatixRequest request) {
		originalRequest = request;
	}
	
	public LocomatixRequest getOriginalRequest() {
		return originalRequest;
	}
	
}
