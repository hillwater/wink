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

package org.apache.wink.itest.standard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path(value = "/dstest")
public class DSResource {

    @POST
    public Response post(DataSource dataSource) {
        Response resp = null;
        try {
            InputStream inputStream = dataSource.getInputStream();
            byte[] inputBytes = new byte[inputStream.available()];
            int next = inputStream.read();
            int i = 0;
            while (next != -1) {
                if (i == inputBytes.length) {
                    inputBytes = ArrayUtils.copyOf(inputBytes, 2 * i);
                }
                inputBytes[i] = (byte)next;
                next = inputStream.read();
                i++;
            }
            TestDataSource entity =
                new TestDataSource(inputBytes, dataSource.getName(), dataSource.getContentType());
            ResponseBuilder rb = Response.ok();
            rb.entity(entity);
            resp = rb.build();
        } catch (Exception e) {
            ResponseBuilder rb = Response.serverError();
            resp = rb.build();
        }
        return resp;
    }

    public class TestDataSource implements DataSource {

        private byte[] inputBytes;

        private String name;

        private String contentType;

        public TestDataSource(byte[] inputBytes, String name, String contentType) {
            this.inputBytes = inputBytes;
            this.name = name;
            this.contentType = contentType;
        }

        public String getContentType() {
            return contentType;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(inputBytes);
        }

        public String getName() {
            return name;
        }

        public OutputStream getOutputStream() throws IOException {
            return null;
        }

    }

    @POST
    @Path("/empty")
    public Response postEmpty(DataSource dataSource) {
        InputStream inputStream;
        try {
            inputStream = dataSource.getInputStream();
            if (inputStream.read() == -1) {
                return Response.ok("expected").build();
            }
        } catch (IOException e) {
            throw new WebApplicationException(e);
        }
        return Response.serverError().build();
    }

}
