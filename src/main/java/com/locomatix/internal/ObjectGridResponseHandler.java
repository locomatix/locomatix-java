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
import org.json.JSONArray;
import org.json.JSONObject;

import com.locomatix.LocomatixResponse;
import com.locomatix.model.Grid;

public class ObjectGridResponseHandler extends ResponseHandler<LocomatixResponse<Grid>> {

	private final int horizontalSlices;
	private final int verticalSlices;
	
	public ObjectGridResponseHandler(int horizontalSlices, int verticalSlices) {
		this.horizontalSlices = horizontalSlices;
		this.verticalSlices = verticalSlices;
	}
	
	
	@Override
	public LocomatixResponse<Grid> handle(HttpResponse response)
			throws Exception {
		
		int status = response.getStatusLine().getStatusCode();
		assert(200 == status): "Invalid HTTP Status Code: " + status;
		
		JSONObject jsonResponse = parseResponse(response);
		
		JSONObject result = jsonResponse.getJSONObject(ResponseKeys.RESULT);
		JSONArray objectGrid = result.getJSONArray(ResponseKeys.OBJECTGRID);
		
		assert(objectGrid.length() == horizontalSlices * verticalSlices);
		
		int[] grid = new int[horizontalSlices * verticalSlices];
		for(int i=0; i<objectGrid.length(); ++i) {
			grid[i] = objectGrid.getInt(i);
		}
		
		LocomatixResponse<Grid> lxresponse = new LocomatixResponse<Grid>(); 
		lxresponse.setResult(new Grid(grid, horizontalSlices, verticalSlices));
		setResponseMetadata(lxresponse, jsonResponse);
		
		return lxresponse;
	}

}
