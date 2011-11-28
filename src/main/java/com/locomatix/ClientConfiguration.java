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

/**
 * Client configuration options such as protocol, host, and port.
 *
 */
public class ClientConfiguration {
	
	// the protocol for this configuration
	private Protocol protocol = Protocol.HTTPS;
	
	// the host for this configuration
	private String host;
	
	// the port for this configuration
	private int port = 443;
	
	private boolean trustAllCertificates = false;
	
	private ClientConfiguration() {}
	
	public ClientConfiguration(String host) {
	  this.host = host;
	}
	
	public ClientConfiguration(String host, int port) {
		this(host);
		this.port = port;
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public Protocol getProtocol() {
		return protocol;
	}
	
	public boolean trustAllCertificates() {
		return trustAllCertificates;
	}
	
	public ClientConfiguration setTrustAllCertificates(boolean trustAllCertificates) {
		this.trustAllCertificates = trustAllCertificates;
		return this;
	}
	
	static ClientConfiguration newInstance(ClientConfiguration config) {
		ClientConfiguration newConfig = new ClientConfiguration();
		newConfig.host = config.host;
		newConfig.port = config.port;
		newConfig.protocol = config.protocol;
		newConfig.trustAllCertificates = config.trustAllCertificates;
		return newConfig;
	}
	
}
