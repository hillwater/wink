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

package org.apache.wink.jaxrs.test.providers.exceptionmappers.nullconditions;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("guestbooknullconditions")
public class GuestbookResource {

    @GET
    @Path("emptywebappexception")
    public String exception() {
        throw new WebApplicationException();
    }

    @GET
    @Path("webappexceptionwithcause")
    public String exceptionWithCause() {
        throw new WebApplicationException(new GuestbookException(
                "Threw checked exception"));
    }

    @POST
    @Path("webappexceptionwithcauseandstatus")
    public String exceptionWithCauseAndStatus() {
        throw new WebApplicationException(new GuestbookException(
                "Threw checked exception"), 499);
    }

    @PUT
    @Path("webappexceptionwithcauseandresponse")
    public String exceptionWithCauseAndResponse() {
        Response resp = Response.status(Status.NOT_ACCEPTABLE).entity(
                "Entity inside response").build();
        throw new WebApplicationException(new GuestbookException(
                "Threw checked exception"), resp);
    }

    @DELETE
    @Path("webappexceptionwithcauseandresponsestatus")
    public String exceptionWithCauseAndResponseStatus() {
        throw new WebApplicationException(new GuestbookException(
                "Threw checked exception"), Response.Status.BAD_REQUEST);
    }

    @GET
    @Path("exceptionmappernull")
    public String exceptionMapperReturnNull() {
        throw new GuestbookNullException("Should not see me");
    }

    @POST
    @Path("exceptionmapperthrowsexception")
    public String exceptionMapperThrowsException()
            throws GuestbookThrowException {
        throw new GuestbookThrowException("Re-throw an exception");
    }

    @POST
    @Path("exceptionmapperthrowserror")
    public String exceptionMapperThrowsError() throws GuestbookThrowException {
        throw new GuestbookThrowException("Re-throw an error");
    }

    @PUT
    @Path("throwableexceptionmapper")
    public String throwableExceptionMapper() throws GuestbookThrowable {
        throw new GuestbookThrowable();
    }

    @DELETE
    @Path("throwsthrowable")
    public String throwThrowable() throws Throwable {
        throw new Throwable("Throwable was thrown") {

            private static final long serialVersionUID = 1L;

        };
    }
}
