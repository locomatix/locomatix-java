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

/**
 * Specifies constants that define the Locomatix trigger conditions.  
 * 
 * Ingress - In only.
 * 
 * Egress - Out only.
 * 
 * IngressAndEgress - In and Out.
 * 
 */
public enum TriggerCondition {
	
	// alert only when something enters a region
	Ingress("Ingress"), 
	
	 // alert only when something exits a region
	Egress("Egress"),
	
	// when when something enters or exits a region
	IngressAndEgress("IngressAndEgress"); 
	
	private final String condition;
	
	private TriggerCondition(String condition) {
		this.condition = condition;
	}
	
	@Override
	public String toString() {
		return condition;
	}
	
	public static TriggerCondition fromValue(String value) {
		if (value == null || "".equals(value)) 
			throw new IllegalArgumentException("Value cannot be null or empty!");
		else if("Ingress".equals(value))
			return Ingress;
		else if("Egress".equals(value))
			return Egress;
		else if("IngressAndEgress".equals(value)) 
			return IngressAndEgress;
		else
			throw new IllegalArgumentException("Cannot create TriggerCondition enum from " 
					+ value + " value!");
	}
}
