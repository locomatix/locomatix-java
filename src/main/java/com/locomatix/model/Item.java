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

import java.util.Map;

import com.locomatix.util.Maps;

public class Item {

  public static final class Builder {
    private Map<Class<?>, Object> objects = Maps.newHashMap();
    
    public <T> Builder setObject(Class<T> type, T instance) {
      if (type == null)
        throw new NullPointerException("Type is null");
      objects.put(type, type.cast(instance));
      return this;
    }
    
    public Item build() {
      return new Item(this);
    }
  }
  
  
  private final Map<Class<?>, Object> objects = Maps.newHashMap();
  
  private Item(Builder builder) {
    this.objects.putAll(builder.objects);
  }
  
  public <T> T get(Class<T> type) {
    return type.cast(objects.get(type));
  }
  
}
