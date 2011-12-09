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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class LQLQueryBuilder {

  private static final String APOSTROPHE = "'";
  
  private List<String> selectClause;
  private String fromClause;
  private String whereClause;
  private Set<String> oids = new HashSet<String>();
    
    
  public LQLQueryBuilder select(String ...fields) {
    if (null == fields)
      throw new IllegalArgumentException("Null Select Clause");
      selectClause = Arrays.asList(fields);
      return this;
  }
    
  public LQLQueryBuilder from(String from) {
    this.fromClause = from;
    return this;
  }
    
    
  public LQLQueryBuilder where(String clause) {
    this.whereClause = clause;
    return this;
  }
    
  public LQLQueryBuilder with(String oid, String ...oids) {
    this.oids.add(oid);
    for(String id : oids)
      this.oids.add(id);
    return this;
  }
    
    
  public String toLql() {
    StringBuilder lql = new StringBuilder();
    if (null != selectClause && !selectClause.isEmpty()) {
      lql.append("SELECT ");
      Iterator<String> iter = selectClause.iterator();
      lql.append(iter.next());
      while (iter.hasNext()) {
        lql.append(", ").append(iter.next());
      }
      lql.append(" ");
    }
        
    lql.append("FROM ").append(fromClause);
        
    if (!oids.isEmpty()) {
      lql.append(" WITH OID ");
      Iterator<String> oidIter = oids.iterator();
      String firstOid = oidIter.next();
      // is there only one object id?
      if (oids.size() == 1) {
        lql.append(APOSTROPHE).append(firstOid).append(APOSTROPHE);
      } else {
        lql.append("IN (").append(APOSTROPHE).append(firstOid).append(APOSTROPHE);
        while (oidIter.hasNext()) {
          lql.append(", ").append(APOSTROPHE).append(oidIter.next()).append(APOSTROPHE);
        }
        lql.append(")");
      }
    }
        
    if (null != whereClause) {
      lql.append(" WHERE ").append(whereClause);
    }

    return lql.toString();
  }

   
  public String toString() {
    return toLql();
  }
    
}



