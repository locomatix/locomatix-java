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

import org.apache.http.HttpResponse;

public interface HttpParser {

  /** Returns the content type. */
  //String getContentType();

  /**
   * Parses the given HTTP response into a new instance of the the given data class of key/value
   * pairs.
   * <p>
   * How the parsing is performed is not restricted by this interface, and is instead defined by the
   * concrete implementation. Implementations should check
   * {@link HttpResponse#isSuccessStatusCode()} to know whether they are parsing a success or error
   * response.
   */
  <T> T parse(HttpResponse response, Class<T> dataClass) throws IOException;
}
