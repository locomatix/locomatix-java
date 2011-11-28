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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.locomatix.PageableRequest;

public class ResultSet {

  public static final class Builder {
    private long count;
    private double sum = Double.NaN;
    private double min = Double.NaN;
    private double max = Double.NaN;
    private String nextToken;
    private List<Item> items = new ArrayList<Item>();
    private PageableRequest pageableRequest;
    
    public Builder count(long count) {
      this.count = count;
      return this;
    }
    
    public Builder sum(double sum) {
      this.sum = sum;
      return this;
    }
    
    public Builder min(double min) {
      this.min = min;
      return this;
    }
    
    public Builder max(double max) {
      this.max = max;
      return this;
    }
    
    public Builder setNextToken(String nextToken) {
      this.nextToken = nextToken;
      return this;
    }
    
    public Builder setItems(List<Item> items) {
      if (null != items) {
        this.items.addAll(items);
      }
      return this;
    }
    
    public Builder pageableRequest(PageableRequest pageableRequest) {
      this.pageableRequest = pageableRequest;
      return this;
    }
    
    public ResultSet build() {
      return new ResultSet(this);
    }
    
  }
 
  private long count;
  private double sum;
  private double min;
  private double max;
  
  private List<Item> items = Collections.emptyList();
  private String nextToken;
  
  private Iterator<Item> iterator;
  private PageableRequest pageableRequest;
  
  private ResultSet(Builder builder) {
    this.count = builder.count;
    this.sum = builder.sum;
    this.min = builder.min;
    this.max = builder.max;
    this.nextToken = builder.nextToken;
    this.items = builder.items;
    iterator = this.items.iterator();
    this.pageableRequest = builder.pageableRequest;
  }
  
  private void update(ResultSet rs) {
    this.count = rs.count;
    this.sum = rs.sum;
    this.min = rs.min;
    this.max = rs.max;
    this.nextToken = rs.nextToken;
    this.items = rs.items;
    iterator = this.items.iterator();
  }
  
  
  public boolean hasNext() {
    if (null == iterator) 
      return false;
    
    if (null == nextToken)
      return iterator.hasNext();
    
    // is the batch done?
    if (!iterator.hasNext()) {
      if (null == pageableRequest)
        return false;
      pageableRequest.withNextToken(getNextToken());
      ResultSet rs = pageableRequest.execute();
      update(rs);
    }
    
    return null == iterator ? false : iterator.hasNext();
  }
  
  
  public Item next() {
    return iterator.next();
  }
  
  public long count() {
    return count;
  }
  
  public double sum() {
    return sum;
  }
  
  public double min() {
    return min;
  }
  
  public double max() {
    return max;
  }
  
  public List<Item> getItems() {
    return items;
  }
  
  public String getNextToken() {
    return nextToken;
  }
  
}

