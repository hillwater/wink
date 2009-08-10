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

package org.apache.wink.itest.lifecycles;

import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("jaxrs/tests/lifecycles")
public class MyResource {

    public static AtomicInteger constructorCounter   = new AtomicInteger(0);
    public static AtomicInteger invokedMethod        = new AtomicInteger(0);
    public static AtomicInteger invokedCounterMethod = new AtomicInteger(0);

    public MyResource() {
        constructorCounter.incrementAndGet();
    }

    @POST
    @Produces("text/plain")
    public String getString(String echo) {
        invokedMethod.getAndIncrement();
        return echo;
    }

    @GET
    @Produces("text/plain")
    public String getCounters() {
        invokedCounterMethod.getAndIncrement();
        return MyMessageBodyReaderAndWriter.constructorCounter.get() + ":"
            + MyMessageBodyReaderAndWriter.readFromCounter.get()
            + ":"
            + MyMessageBodyReaderAndWriter.writeToCounter.get()
            + ":"
            + constructorCounter.get()
            + ":"
            + invokedMethod.get()
            + ":"
            + invokedCounterMethod.get();
    }

    @DELETE
    public void resetMethodCounters() {
        invokedMethod.set(0);
        invokedCounterMethod.set(0);
        constructorCounter.set(0);
        MyMessageBodyReaderAndWriter.readFromCounter.set(0);
        MyMessageBodyReaderAndWriter.writeToCounter.set(0);
    }

}
