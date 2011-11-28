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

import com.locomatix.LocomatixResponse;
import com.locomatix.LocomatixServiceException;

public class ZoneKeyResponseHandler extends ResponseHandler<LocomatixResponse<Void>> {

	@Override
	public LocomatixResponse<Void> handle(HttpResponse response)
			throws Exception {
		int status = response.getStatusLine().getStatusCode();
		assert(200 == status): "Invalid HTTP Status Code: " + status;
		
		JSONObject jsonResponse = parseResponse(response);
		
		JSONObject result = jsonResponse.getJSONObject(ResponseKeys.RESULT);
		String zoneId = result.getString(ResponseKeys.ZONEID);
		String objectId = result.getString(ResponseKeys.OBJECTID);
		String feedId = result.getString(ResponseKeys.FEED);
		
		if (null == zoneId || null == objectId || null == feedId)
		  throw new LocomatixServiceException("Unexpected zone response: " + result.toString());
		
		LocomatixResponse<Void> lxresponse = ResponseHandler.newLocomatixResponse();
		setResponseMetadata(lxresponse, jsonResponse);
		
		return lxresponse;
	}

}
