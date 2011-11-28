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

package com.locomatix.http;

import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;

import com.locomatix.ClientConfiguration;
import com.locomatix.HttpMethod;
import com.locomatix.LocomatixException;
import com.locomatix.LocomatixResponse;
import com.locomatix.LocomatixServiceException;
import com.locomatix.internal.ResponseHandler;

/**
 * An internal class used to send requests to Locomatix and handle the results.
 * 
 */
public class HttpClient {

	private static final String DEFAULT_ENCODING = "UTF-8";
	
	// Internal client for sending HTTP requests 
    private org.apache.http.client.HttpClient client = new DefaultHttpClient();
    
    private final ClientConfiguration config;
	
    public HttpClient(ClientConfiguration clientConfiguration) {
    	this.config = clientConfiguration;
    	client.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, DEFAULT_ENCODING);
    	if(config.trustAllCertificates()) {
    		configureTrustAllCertificatesClient();
    	}
    }
    
    
    private void configureTrustAllCertificatesClient() {
    	try {
    		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    		trustStore.load(null, null);
    		
    		
    		SSLContext sslcontext = SSLContext.getInstance("TLS");
    		sslcontext.init(null, new TrustManager[] { createAllowAllTrustManager() }, null);
    		//SSLSocketFactory sf = new TrustAllSSLSocketFactory(trustStore);
    		//sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    		SSLSocketFactory sf = new SSLSocketFactory(sslcontext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    		
    		ClientConnectionManager ccm = client.getConnectionManager();
    		SchemeRegistry sr = ccm.getSchemeRegistry();
    		
    		Scheme scheme = new Scheme(config.getProtocol().toString(), config.getPort(), sf);
    		sr.register(scheme);
    		
    		client = new DefaultHttpClient(ccm, client.getParams());
    	} catch(Exception ex) {
    		throw new IllegalArgumentException(ex);
    	}
    }
    
    
    public <T> LocomatixResponse<T> execute(HttpRequest httpRequest, 
    		ResponseHandler<LocomatixResponse<T>> responseHandler, 
            ResponseHandler<LocomatixServiceException> errorResponseHandler) 
            throws LocomatixException, LocomatixServiceException {
    	
    	try {
    		
    		HttpUriRequest httpUriRequest = createHttpUriRequestFromRequest(httpRequest);
        	
    		HttpResponse httpResponse = client.execute(httpUriRequest);
    		
    		// was the request successful?
    		if(!successfulResponse(httpResponse)) {
    			LocomatixServiceException lse = errorResponseHandler.handle(httpResponse);
    			throw lse;
    		}
    		
    		// we got back a 200 status code, so the request was successful.
    		return responseHandler.handle(httpResponse);
    		
    	} catch(ClientProtocolException cpe) {
    		throw new LocomatixException("Unable to execute HTTP request: " + cpe.getMessage(), cpe);
    	} catch(ParseException pe) {
    		throw new LocomatixException("Unable to execute HTTP request: " + pe.getMessage(), pe);
    	} catch(IOException ioe) {
    		throw new LocomatixException("Unable to execute HTTP request: " + ioe.getMessage(), ioe);
    	} catch(LocomatixServiceException lse) {
    		throw lse;
    	} catch(Exception ex) {
    		throw new LocomatixException(ex.getMessage(), ex);
    	} 
    	
    }
    
    
    public void shutdown() {
    	client.getConnectionManager().shutdown();
    }
    
    
    /**
     * Returns true is the request to Locomatix was successful, and false otherwise.
     * 
     * @param response The HttpResponse from Locomatix.
     * 
     * @return true if the request to Locomatix was successful and false otherwise.
     */
    private boolean successfulResponse(HttpResponse response) {
    	if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode())
    		return true;
    	else
    		return false;
    }
    
    
    /**
     * Creates a HttpUriRequest method based on the HttpRequest.
     * 
     * @param httpRequest the object to be convert into a HttpUriRequest.
     * 
     * @return A HttpUriRequest object with any parameters, headers, etc, from the original request
     * 
     * @throws ParseException
     * 
     * @throws IOException
     */
    private HttpUriRequest createHttpUriRequestFromRequest(HttpRequest httpRequest) throws ParseException, IOException {
    	
    	StringBuilder uri = new StringBuilder(httpRequest.getEndpoint().toString());
    	uri.append(httpRequest.getPath());
    	
    	List<NameValuePair> parameters = null;
		// are there parameters with this request?
		if(httpRequest.getParameters().size() > 0) {
			parameters = new ArrayList<NameValuePair>(httpRequest.getParameters());
		}
		
		HttpUriRequest httpUriRequest = null;
		if(HttpMethod.GET == httpRequest.getMethod()) {
			if(null != parameters) {
				uri.append(createQueryString(parameters));
			}
			HttpGet httpGet = new HttpGet(uri.toString());
			httpUriRequest = httpGet;
		} else if(HttpMethod.DELETE == httpRequest.getMethod()) {
			if(null != parameters) {
				uri.append(createQueryString(parameters));
			}
			HttpDelete httpDelete = new HttpDelete(uri.toString());
			httpUriRequest = httpDelete;
		} else if(HttpMethod.POST == httpRequest.getMethod()) {
			HttpPost httpPost = new HttpPost(uri.toString());
			if(null != parameters) {
				httpPost.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			}
			httpUriRequest = httpPost;
		} else if(HttpMethod.PUT == httpRequest.getMethod()) {
			HttpPut httpPut = new HttpPut(uri.toString());
			if(null != parameters) {
				httpPut.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			}
			httpUriRequest = httpPut;
		} else {
			throw new LocomatixException("Unknown HTTP method name: " + httpRequest.getMethod());
		}
		
		// all the headers from the request
		for (Entry<String, String> entry : httpRequest.getHeaders().entrySet()) {
			httpUriRequest.addHeader(entry.getKey(), entry.getValue());
		}
		
		return httpUriRequest;
	}
	
    
    private String createQueryString(List<NameValuePair> parameters) {
    	return "?" + URLEncodedUtils.format(parameters, "UTF-8");
    }
    
    
    private TrustManager createAllowAllTrustManager() {
    	TrustManager allowAllTrustManager = new X509TrustManager() {

    	    @Override
    	    public void checkClientTrusted(
    	            X509Certificate[] chain,
    	            String authType) throws CertificateException {
    	        // Oh, I am easy!
    	    }

    	    @Override
    	    public void checkServerTrusted(
    	            X509Certificate[] chain,
    	            String authType) throws CertificateException {
    	        // Oh, I am easy!
    	    }

    	    @Override
    	    public X509Certificate[] getAcceptedIssuers() {
    	        return null;
    	    }
    	    
    	};
    	return allowAllTrustManager;
    }
    
}
