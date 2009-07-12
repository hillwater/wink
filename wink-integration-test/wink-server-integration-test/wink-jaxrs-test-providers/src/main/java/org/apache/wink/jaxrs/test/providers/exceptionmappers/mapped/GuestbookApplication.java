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

package org.apache.wink.jaxrs.test.providers.exceptionmappers.mapped;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * The JAX-RS Application config class.
 */
public class GuestbookApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(Guestbook.class);
        classes.add(WebApplicationExceptionMapProvider.class);
        classes.add(RuntimeExceptionMappingProvider.class);
        classes.add(NullPointerExceptionMapProvider.class);
        classes.add(GuestbookErrorExceptionMappingProvider.class);
        classes.add(GuestbookExceptionMapProvider.class);
        return classes;
    }

}
