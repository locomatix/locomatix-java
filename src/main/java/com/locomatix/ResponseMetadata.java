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

import java.util.HashMap;
import java.util.Map;

public class ResponseMetadata {

  private static final String EXECUTION_TIME = "ExecutionTime";
    
	protected final Map<String, String> metadata = new HashMap<String, String>();
	
	public static ResponseMetadata of(String... keyValuePairs) {
	  ResponseMetadata metadata = new ResponseMetadata();
	  int length = keyValuePairs.length;
	  if (1 == length % 2) {
	     throw new IllegalArgumentException("missing value for last key: " + keyValuePairs[length - 1]);
	  }
	  for (int i=0; i<length; i+=2) {
	    String key = keyValuePairs[i];
	    String value = keyValuePairs[i+1];
	    metadata.metadata.put(key, value);
    }
	    
    return metadata;
	}
	
	public ResponseMetadata() {}
	
	public ResponseMetadata(Map<String, String> metadata) {
    if (null != metadata) {
	    this.metadata.putAll(metadata);
	  }
	}
	
	public int getExecutionTime() {
	  String s = metadata.get(EXECUTION_TIME);
	  if (null == s) return -1;
	  return Integer.valueOf(s);
	}
	
	
	@Override
	public String toString() {
	  if (null == metadata) return "{}";
	  return metadata.toString();
	}
	
}
