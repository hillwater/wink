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

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("providers/standard/bytesarray")
public class BytesArrayResource {

    private byte[] barr = null;

    @GET
    public Response getByteArray() {
        return Response.ok(barr).build();
    }

    @POST
    public byte[] postByteArray(byte[] bytearray) {
        return bytearray;
    }

    @PUT
    public void putByteArray(byte[] bytearray) {
        barr = bytearray;
    }

    @POST
    @Path("/empty")
    @Produces("text/plain")
    public Response postEmptyByteArray(byte[] bytearray) {
        if (bytearray != null && bytearray.length == 0) {
            return Response.ok("expected").build();
        }
        return Response.serverError().build();
    }

}
