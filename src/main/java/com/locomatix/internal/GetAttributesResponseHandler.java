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
import com.locomatix.model.LXObject;

public class GetAttributesResponseHandler extends ResponseHandler<LocomatixResponse<LXObject>> {

	@Override
	public LocomatixResponse<LXObject> handle(HttpResponse response) throws Exception {
		int status = response.getStatusLine().getStatusCode();
		assert(200 == status): "Invalid HTTP Status Code: " + status;
		
		JSONObject jsonResponse = parseResponse(response);
		 
		JSONObject object = 
			jsonResponse.getJSONObject(ResponseKeys.RESULT).getJSONObject(ResponseKeys.OBJECT);
		
		LocomatixResponse<LXObject> lxresponse = ResponseHandler.newLocomatixResponse();
		lxresponse.setResult(JsonParser.parseObject(object, LXObject.class));
		setResponseMetadata(lxresponse, jsonResponse);
		
		return lxresponse;
	}

}
