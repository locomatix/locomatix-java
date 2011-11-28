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

package com.locomatix.internal;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import com.locomatix.LocomatixServiceException;

/**
 * An internal class for parsing Locomatix error responses.
 *
 */
public class ErrorResponseHandler extends ResponseHandler<LocomatixServiceException> {

	
	@Override
	public LocomatixServiceException handle(HttpResponse response) throws Exception {
		int statusCode = response.getStatusLine().getStatusCode();
		
		JSONObject jsonResponse = parseResponse(response);
		
		String status = jsonResponse.getString(ResponseKeys.STATUS);
		String responseType = jsonResponse.getString(ResponseKeys.RESPONSETYPE);
		
		LocomatixServiceException lse = new LocomatixServiceException(status);
		lse.setStatusCode(statusCode);
		lse.setResponseType(responseType);
		
		return lse;
	}

}
