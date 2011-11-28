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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.locomatix.LocomatixResponse;
import com.locomatix.ResponseMetadata;

/**
 * An internal base class for handling Locomatix server responses.
 *
 * @param <T>
 */
public abstract class ResponseHandler<T> {
	
  private static volatile boolean debugResponses = false;
	private static final Logger Log = Logger.getAnonymousLogger();
	
	static {
	  //Log.get
	}
	
	public abstract T handle(HttpResponse response) throws Exception;
	
	/**
	 * Create a JSONObject from the HttpResponse
	 * 
	 * @param response HttpResponse
	 * @return a JSONObject
	 * @throws IOException
	 * @throws JSONException
	 */
	protected JSONObject parseResponse(HttpResponse response) throws IOException, JSONException {
		Log.setLevel(Level.ALL);
	  try {
	    if (debugResponses) {
	      String responseAsString = EntityUtils.toString(response.getEntity());
			  Log.info(responseAsString);
			  return new JSONObject(new JSONTokener(responseAsString));
	    } else {
	      Reader r = new InputStreamReader(response.getEntity().getContent());
	      return new JSONObject(new JSONTokener(r));
	    }
		} finally {
			// make sure to release the connection
			if(null != response.getEntity()) {
			    EntityUtils.consume(response.getEntity());
			}
		}
	}
	
	
	void setResponseMetadata(LocomatixResponse<? extends Object> response, 
	        JSONObject jsonResponse) throws JSONException {
	    if (jsonResponse.has(ResponseKeys.EXECUTIONTIME)) {
	        int executionTime = jsonResponse.getInt(ResponseKeys.EXECUTIONTIME);
	        ResponseMetadata md = 
	            ResponseMetadata.of(ResponseKeys.EXECUTIONTIME, String.valueOf(executionTime));
	        response.setResponseMetadata(md);
	    }
	}
	
	
	static <T> LocomatixResponse<T> newLocomatixResponse() {
	  return new LocomatixResponse<T>();
	}
}
