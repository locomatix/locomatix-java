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

import java.util.Iterator;
import java.util.List;

import com.locomatix.util.Lists;

class LQLQueryBuilder {

    private List<String> selectClause;
    private String fromClause;
    private String whereClause;
    private String with;
    //private List<String> oids;
    
    
    public LQLQueryBuilder select(String ...fields) {
        if (null == fields)
            throw new IllegalArgumentException("Null Select Clause");
        selectClause = Lists.newArrayList(fields);
        return this;
    }
    
    public LQLQueryBuilder from(String from) {
        this.fromClause = from;
        return this;
    }
    
    
    public LQLQueryBuilder where(String clause) {
        if (null != clause) {
            this.whereClause = clause;
        }
        return this;
    }
    
    public LQLQueryBuilder with(String id) {
        this.with = id;
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
        
        if (null != with) {
            //String.format("select location.* from %s with oid \"%s\"", feedId, objectId);
            lql.append(" WITH OID ").append('"').append(with).append('"');
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



