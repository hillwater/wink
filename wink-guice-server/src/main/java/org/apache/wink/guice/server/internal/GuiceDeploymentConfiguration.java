/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

package org.apache.wink.guice.server.internal;

import org.apache.wink.common.internal.lifecycle.LifecycleManagersRegistry;
import org.apache.wink.guice.server.internal.lifecycle.GuiceInjectorLifeCycleManager;
import org.apache.wink.guice.server.internal.lifecycle.WinkGuiceModule;
import org.apache.wink.server.handlers.Handler;
import org.apache.wink.server.internal.DeploymentConfiguration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class GuiceDeploymentConfiguration extends DeploymentConfiguration {

    @SuppressWarnings("unchecked")
    public GuiceDeploymentConfiguration() {
        LifecycleManagersRegistry lifecycleManagersRegistry = new LifecycleManagersRegistry();
        setOfFactoryRegistry(lifecycleManagersRegistry);
        injector = Guice.createInjector(createModules());
        lifecycleManagersRegistry.addFactoryFactory(new GuiceInjectorLifeCycleManager(injector));
    }

    public Module[] createModules() {
        return new Module[] {new WinkGuiceModule()};
    }
    
	private final Injector injector;
	@Override
	protected <T extends Handler> T createHandler(Class<T> cls) {
		try {
			return injector.getInstance(cls);
		} catch (Throwable th) {
			return super.createHandler(cls);
		}
	}
}
