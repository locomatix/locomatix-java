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
package com.locomatix.util.concurrent;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import com.locomatix.ClientConfiguration;
import com.locomatix.Locomatix;
import com.locomatix.auth.LocomatixCredentials;

public class LocomatixThreadFactory implements java.util.concurrent.ThreadFactory {

  
  public ThreadPoolExecutor.CallerRunsPolicy newCallerRunsPolicy() {
    return new CallerRunsPolicy(credentials, clientConfiguration);
  }
  
  
  public static final class CallerRunsPolicy extends ThreadPoolExecutor.CallerRunsPolicy {
    
    private final LocomatixCredentials credentials;
    private ClientConfiguration clientConfiguration;
    
    public CallerRunsPolicy(LocomatixCredentials credentials, ClientConfiguration clientConfiguration) {
      super();
      this.credentials = credentials;
      this.clientConfiguration = clientConfiguration;
    }
    
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
      boolean threadHasClient = LocomatixThreadFactory.hasClient();
      try {
        // does this thread already has a client?
        if (!threadHasClient) {
          LocomatixThreadFactory.createClientIfNull(credentials, clientConfiguration);
        }
        super.rejectedExecution(r, e);
      } finally {
        if (!threadHasClient) {
          LocomatixThreadFactory.shutdownClient();
        }
      }
    }
    
  }
  
  private final LocomatixCredentials credentials;
  
  private final ClientConfiguration clientConfiguration;
  
  public LocomatixThreadFactory(LocomatixCredentials credentials, 
      ClientConfiguration clientConfiguration) {
    if (null == credentials)
      throw new IllegalArgumentException("Credentials are null");
    if (null == clientConfiguration)
      throw new IllegalArgumentException("ClientConfiguration is null");
    this.credentials = credentials;
    this.clientConfiguration = clientConfiguration;
  }
  
  
  @Override
  public Thread newThread(Runnable runnable) {
    return new LXThread(runnable, credentials, clientConfiguration);
  }

  private static final ThreadLocal<Locomatix> clientPool = new ThreadLocal<Locomatix>();
  
  
  
  private static boolean hasClient() {
    return null != clientPool.get();
  }
  
  
  private static void createClientIfNull(LocomatixCredentials c, ClientConfiguration config) {
    if (null == clientPool.get()) {
      Locomatix locomatix = new Locomatix(c, config);
      clientPool.set(locomatix);
    }
  }
  
  private static void shutdownClient() {
    Locomatix locomatix = clientPool.get();
    // does this thread have a client?
    if (null != locomatix) {
      locomatix.shutdown();
    }
    clientPool.set(null);
  }
  
  public static final Locomatix getClient() {
    return clientPool.get();
  }
  
  private static final class LXThread extends Thread {
    private static volatile boolean debugLifecycle = false;
    private static final AtomicInteger created = new AtomicInteger();
    
    private final LocomatixCredentials credentials;
    private final ClientConfiguration clientConfiguration;
    
    private LXThread(Runnable runnable, 
        LocomatixCredentials credentials, ClientConfiguration clientConfiguration) {
      super(runnable, "lxthread-" + created.incrementAndGet());
      this.credentials = credentials;
      this.clientConfiguration = clientConfiguration;
    }
    
    @Override
    public void run() {
      // Copy the debug flag to ensure consistent value throughput.
      boolean debug = debugLifecycle;
      if (debug) System.out.println("Created " + getName());
      // set up the client for this thread
      LocomatixThreadFactory.createClientIfNull(credentials, clientConfiguration);
      try {
        super.run();
      } finally {
        if (debug) System.out.println("Exiting " + getName());
        // shutdown the client
        LocomatixThreadFactory.shutdownClient();
      }
    }
    
  }
  
}
