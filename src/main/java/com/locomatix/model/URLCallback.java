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
package com.locomatix.model;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * 
 *
 */
public class URLCallback extends Callback {

	private static final String CALLBACKURL = "callbackurl";
	
	// the URL for posting notifications
	private final URL url;
	
	/**
	 * Constructs a new URLCallback with
	 * @param url
	 */
	public URLCallback(URL url) {
	  if (null == url)
	    throw new IllegalArgumentException("URL is null");
		this.url = url;
		properties.put(Callback.CALLBACKTYPE, "url");
		properties.put(CALLBACKURL, url.toString());
	}
	
	/**
	 * Returns the URL to use for posting notifications.
	 * 
	 * @return The URL to use for posting notifications.
	 */
	public URL getURL() {
		return url;
	}
	
	@Override
	public CallbackType getCallbackType() {
		return CallbackType.URL;
	}

	@Override
	public String toString() {
	  StringBuilder sb = new StringBuilder("{");
	  sb.append("URL: ").append(url);
	  sb.append("}");
	  return sb.toString();
	}
	
	
	public static URLCallback create(String url) throws IllegalArgumentException {
	  try {
	    URL newURL = new URL(url);
	    return new URLCallback(newURL);
	  } catch(MalformedURLException me) {
	    throw new IllegalArgumentException(me);
	  }
	}
	
}
