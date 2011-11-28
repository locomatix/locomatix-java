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


public class LocomatixResponse<T> {

	// The result contained by this response
	private T result;
	
	private ResponseMetadata responseMetadata = new ResponseMetadata();
	
	public LocomatixResponse() {}
	
	public LocomatixResponse(T result) {
		setResult(result);
	}
	
	public LocomatixResponse(T result, ResponseMetadata metadata) {
	    setResult(result);
	}
	
	public T getResult() {
		return result;
	}
	
	public void setResult(T result) {
		this.result = result;
	}
	
	public void setResponseMetadata(ResponseMetadata responseMetadata) {
	    if (null != responseMetadata) {
	        this.responseMetadata = responseMetadata;
	    }
	}
	
	public ResponseMetadata getResponseMetadata() {
		return responseMetadata;
	}
	
}
