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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.locomatix.util.AbstractMultimap;

public class NameValues extends AbstractMultimap<String, String> {

  private static final NameValues emptyNameValues;
  
  static {
    Map<String, Collection<String>> emptyMap = 
      Collections.unmodifiableMap(new HashMap<String, Collection<String>>());
    emptyNameValues = new NameValues(Collections.unmodifiableMap(emptyMap));
  }
  
  public static NameValues emptyNameValues() {
    return emptyNameValues;
  }
  
  
  public static NameValues create() {
    return new NameValues();
  }
  
  private NameValues() {
    super(new  HashMap<String, Collection<String>>());
  }
  
  private NameValues(Map<String, Collection<String>> map) {
    super(map);
  }
  
  
  @Override
  public String getFirst(String key) {
    Collection<String> collection = get(key);
    if (null != collection) {
        List<String> list = (List<String>) collection;
        return list.get(0);
    }
    return null;
  }

  @Override 
  public List<String> get(String key) {
    return (List<String>) super.get(key);
  }

  @Override
  protected Collection<String> createCollection() {
    return new ArrayList<String>();
  }

}
