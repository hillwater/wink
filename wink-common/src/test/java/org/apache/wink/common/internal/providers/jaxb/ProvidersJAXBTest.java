/*******************************************************************************
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
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *  
 *******************************************************************************/
package org.apache.wink.common.internal.providers.jaxb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.wink.common.internal.CaseInsensitiveMultivaluedMap;
import org.apache.wink.common.internal.providers.entity.xml.JAXBXmlProvider;
import org.apache.wink.common.internal.providers.jaxb.jaxb1.AddNumbers;
import org.apache.wink.common.model.JAXBUnmarshalOptions;
import org.apache.wink.common.model.XmlFormattingOptions;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.junit.Before;
import org.junit.Test;

public class ProvidersJAXBTest extends MockObjectTestCase {
    
    public class MyJAXBXmlProvider extends JAXBXmlProvider {

        MyJAXBXmlProvider() {
            super();
            providers = ProvidersJAXBTest.this.providers;
        }
        
        /* 
         * simulate what would happen if application had supplied a JAXBContext provider
         */
        @Override
        protected JAXBContext getContext(Class<?> type, MediaType mediaType)
                throws JAXBException {
            // use JAXBContext.newInstance(String).  The default in AbstractJAXBProvider is JAXBContext.newInstance(Class)
            return JAXBContext.newInstance(type.getPackage().getName());
        }
        
    }
    
    static final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
        "<ns2:addNumbers xmlns:ns2=\"http://org/apache/wink/common/internal/providers/jaxb/jaxb1\">" +
        "<arg0>1</arg0>" +
        "<arg1>2</arg1>" +
        "</ns2:addNumbers>";
    
    static final String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + 
        "<addNumbers xmlns=\"http://org/apache/wink/common/internal/providers/jaxb/jaxb1\">" +
        "<arg0>0</arg0>" +
        "<arg1>0</arg1>" +
        "</addNumbers>";

    private MessageBodyReader jaxbProviderReader = null;
    private MessageBodyWriter jaxbProviderWriter = null;
    private Providers providers;
    
    @Before
    public void setUp() {
        providers = mock(Providers.class);
        checking(new Expectations() {{
            allowing(providers).getContextResolver(JAXBContext.class, MediaType.TEXT_XML_TYPE); will(returnValue(new MyJAXBContextResolver()));
            allowing(providers).getContextResolver(XmlFormattingOptions.class, MediaType.TEXT_XML_TYPE); will(returnValue(null));
            allowing(providers).getContextResolver(JAXBUnmarshalOptions.class, MediaType.TEXT_XML_TYPE); will(returnValue(null));
        }});
        jaxbProviderReader = new MyJAXBXmlProvider();
        jaxbProviderWriter = new MyJAXBXmlProvider();
    }
    
    @Test
    public void testJAXBUnmarshallingWithAlternateContext1() throws Exception {
        assertTrue(jaxbProviderReader.isReadable(AddNumbers.class, null, null, MediaType.TEXT_XML_TYPE));
        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
        Object obj = jaxbProviderReader.readFrom(AddNumbers.class, null, null, MediaType.TEXT_XML_TYPE, null, bais);
        assertTrue(obj instanceof AddNumbers);
    }
    
    public class MyJAXBContextResolver implements ContextResolver<JAXBContext> {

        public JAXBContext getContext(Class<?> arg0) {
            try {
                return JAXBContext.newInstance(arg0);
            } catch (JAXBException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testJAXBMarshalling() throws WebApplicationException, IOException {
        assertTrue(jaxbProviderWriter.isWriteable(AddNumbers.class, null, null, MediaType.TEXT_XML_TYPE));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        jaxbProviderWriter.writeTo(new AddNumbers(), AddNumbers.class, null, null, MediaType.TEXT_XML_TYPE, null, baos);
        assertEquals(expectedXml, baos.toString());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testJAXBMarshallingWithMap() throws WebApplicationException, IOException {
        assertTrue(jaxbProviderWriter.isWriteable(AddNumbers.class, null, null, MediaType.TEXT_XML_TYPE));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MultivaluedMap map = new CaseInsensitiveMultivaluedMap<Object>();
        jaxbProviderWriter.writeTo(new AddNumbers(), AddNumbers.class, null, null, MediaType.TEXT_XML_TYPE, map, baos);
        assertEquals(expectedXml, baos.toString());
    }
    
    
}
