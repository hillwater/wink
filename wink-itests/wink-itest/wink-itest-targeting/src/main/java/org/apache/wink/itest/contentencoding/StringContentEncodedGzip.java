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

package org.apache.wink.itest.contentencoding;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

@Provider
public class StringContentEncodedGzip implements MessageBodyReader<String> {

    @Context
    private HttpHeaders headers;

    public boolean isReadable(Class<?> type,
                              Type genericType,
                              Annotation[] annotations,
                              MediaType mediaType) {
        if (type != String.class || !MediaType.TEXT_PLAIN_TYPE.isCompatible(mediaType)) {
            return false;
        }
        List<String> contentEncodingValues = headers.getRequestHeader("Content-Encoding");
        String contentEncoding = null;
        if (contentEncodingValues.size() == 1) {
            contentEncoding = contentEncodingValues.get(0);
        }
        if (contentEncoding != null && "gzip".equals(contentEncoding)) {
            return true;
        }
        return false;
    }

    @Context
    private Providers providers;

    public String readFrom(Class<String> type,
                           Type genericType,
                           Annotation[] annotations,
                           MediaType mediaType,
                           MultivaluedMap<String, String> httpHeaders,
                           InputStream entityStream) throws IOException, WebApplicationException {
        MessageBodyReader<String> strReader =
            providers.getMessageBodyReader(String.class,
                                           String.class,
                                           annotations,
                                           MediaType.APPLICATION_XML_TYPE);
        return strReader.readFrom(type,
                                  genericType,
                                  annotations,
                                  mediaType,
                                  httpHeaders,
                                  new GZIPInputStream(entityStream));
    }

}
