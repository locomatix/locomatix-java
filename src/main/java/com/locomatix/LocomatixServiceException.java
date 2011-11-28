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
package com.locomatix;

/**
 * Represents an error response returned by the Locomatix service. 
 * Receiving an exception of this type indicates that
 * the caller's request was correctly transmitted to the service, but for some
 * reason, the service was not able to process it, and returned an error
 * response instead.
 * 
 */
public class LocomatixServiceException extends LocomatixException {
	private static final long serialVersionUID = 1L;
	
	private String responseType;
	
	private int statusCode;
	
	public LocomatixServiceException(String message) {
		super(message);
	}
	
	public LocomatixServiceException(String message, String responseType, int statusCode) {
		this(message);
		this.responseType = responseType;
		this.statusCode = statusCode;
	}
	
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	
	public String getResponseType() {
		return responseType;
	}
	
}
