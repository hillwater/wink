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

package org.apache.wink.itest.exceptions;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.wink.test.integration.ServerContainerAssertions;
import org.apache.wink.test.integration.ServerEnvironmentInfo;

public class ExceptionsWhileTargettingTest extends TestCase {

    public static String getBaseURI() {
        if (ServerEnvironmentInfo.isRestFilterUsed()) {
            return ServerEnvironmentInfo.getBaseURI();
        }
        return ServerEnvironmentInfo.getBaseURI() + "/exceptional";
    }

    protected HttpClient client;

    @Override
    public void setUp() {
        client = new HttpClient();
    }

    /**
     * Tests that a 404 error is thrown when no resource can be found for a
     * path.
     * 
     * @throws Exception
     */
    public void test404WhenNoResourceExists() throws Exception {
        GetMethod getMethod = new GetMethod(getBaseURI() + "/doesnotexist");
        try {
            client.executeMethod(getMethod);
            assertEquals(404, getMethod.getStatusCode());
            ServerContainerAssertions.assertExceptionBodyFromServer(404, getMethod
                .getResponseBodyAsString());
        } finally {
            getMethod.releaseConnection();
        }
    }

    /**
     * Tests that a 405 error is thrown when no subresource can be found for a
     * path.
     * 
     * @throws Exception
     */
    public void test405WhenNoMethodExistsOnExistingResource() throws Exception {
        GetMethod getMethod = new GetMethod(getBaseURI() + "/existingresource/");
        try {
            client.executeMethod(getMethod);
            assertEquals(405, getMethod.getStatusCode());
            ServerContainerAssertions.assertExceptionBodyFromServer(405, getMethod
                .getResponseBodyAsString());

        } finally {
            getMethod.releaseConnection();
        }
    }

    /**
     * Tests that a 404 error is thrown when no subresource can be found for a
     * path.
     * 
     * @throws Exception
     */
    public void test404WhenNoSubResourceExists() throws Exception {
        GetMethod getMethod = new GetMethod(getBaseURI() + "/existingresource/noexistsub");
        try {
            client.executeMethod(getMethod);
            assertEquals(404, getMethod.getStatusCode());
            ServerContainerAssertions.assertExceptionBodyFromServer(404, getMethod
                .getResponseBodyAsString());

            getMethod.setURI(new URI(getBaseURI() + "/targeting/resourcewithmethod", true));
            client.executeMethod(getMethod);
            assertEquals(200, getMethod.getStatusCode());
            assertEquals("Hello", getMethod.getResponseBodyAsString());

            getMethod.setURI(new URI(getBaseURI() + "/targeting/resourcewithmethod/noexistsub",
                                     true));
            client.executeMethod(getMethod);
            assertEquals(404, getMethod.getStatusCode());
            ServerContainerAssertions.assertExceptionBodyFromServer(404, getMethod
                .getResponseBodyAsString());
        } finally {
            getMethod.releaseConnection();
        }
    }

    /**
     * Tests that a 405 error is thrown when other http methods exist on a
     * resource but not the one looking for.
     * 
     * @throws Exception
     */
    public void test405WhenResourceMethodDoesNotExistButOthersDo() throws Exception {
        GetMethod getMethod = new GetMethod(getBaseURI() + "/existingresource/noexistsub");
        try {
            client.executeMethod(getMethod);
            assertEquals(404, getMethod.getStatusCode());
            ServerContainerAssertions.assertExceptionBodyFromServer(404, getMethod
                .getResponseBodyAsString());

        } finally {
            getMethod.releaseConnection();
        }

        PostMethod postMethod = new PostMethod(getBaseURI() + "/targeting/resourcewithmethod");
        try {
            client.executeMethod(postMethod);
            assertEquals(405, postMethod.getStatusCode());
            ServerContainerAssertions.assertExceptionBodyFromServer(405, postMethod
                .getResponseBodyAsString());
        } finally {
            getMethod.releaseConnection();
        }
    }

    /**
     * Tests that a 415 error is thrown when request entity data sent is not
     * acceptable by the resource.
     * 
     * @throws Exception
     */
    public void test415WhenResourceMethodDoesNotAcceptRequestEntity() throws Exception {
        PutMethod putMethod = new PutMethod(getBaseURI() + "/targeting/resourcewithmethod");
        try {
            putMethod.setRequestEntity(new StringRequestEntity("some content", "text/plain",
                                                               "UTF-8"));
            client.executeMethod(putMethod);
            assertEquals(200, putMethod.getStatusCode());
            assertEquals("some content", putMethod.getResponseBodyAsString());
        } finally {
            putMethod.releaseConnection();
        }

        putMethod = new PutMethod(getBaseURI() + "/targeting/resourcewithmethod");
        try {
            putMethod.setRequestEntity(new StringRequestEntity("some content",
                                                               "customplain/something", "UTF-8"));
            client.executeMethod(putMethod);
            assertEquals(415, putMethod.getStatusCode());
            ServerContainerAssertions.assertExceptionBodyFromServer(415, putMethod
                .getResponseBodyAsString());

        } finally {
            putMethod.releaseConnection();
        }
    }

    /**
     * Tests that a 406 error is produced if server side cannot produce any
     * acceptable content type.
     * 
     * @throws Exception
     */
    public void test406WhenResourceMethodDoesNotProduceResponseEntityType() throws Exception {
        PutMethod putMethod = new PutMethod(getBaseURI() + "/targeting/resourcewithmethod");

        try {
            putMethod.addRequestHeader("Accept", "text/plain");
            putMethod.setRequestEntity(new StringRequestEntity("some content", "text/plain",
                                                               "UTF-8"));
            client.executeMethod(putMethod);

            assertEquals(200, putMethod.getStatusCode());
            assertEquals("some content", putMethod.getResponseBodyAsString());
        } finally {
            putMethod.releaseConnection();
        }

        putMethod = new PutMethod(getBaseURI() + "/targeting/resourcewithmethod");
        try {
            putMethod.addRequestHeader("Accept", "text/customplain");
            putMethod.setRequestEntity(new StringRequestEntity("some content", "text/plain",
                                                               "UTF-8"));
            client.executeMethod(putMethod);

            assertEquals(406, putMethod.getStatusCode());
            ServerContainerAssertions.assertExceptionBodyFromServer(406, putMethod
                .getResponseBodyAsString());
        } finally {
            putMethod.releaseConnection();
        }
    }
}
