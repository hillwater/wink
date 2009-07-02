package org.apache.wink.server.internal.activation;

import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.wink.server.internal.servlet.MockServletInvocationTest;
import org.apache.wink.test.mock.MockRequestConstructor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class ActivationDataContentHandlerTest extends MockServletInvocationTest {

    private static final String DATA = "data";

    @Override
    protected Class<?>[] getClasses() {
        return new Class[] {TestResource.class};
    }

    @Path("/root")
    public static class TestResource {
        public String data;
        
        public TestResource(){}
        public TestResource(String data){this.data = data;}
        
        @Path("/exists")
        @GET
        @Produces("text/resource")
        public TestResource getResource() {
            return new TestResource(DATA);
        }
        
        @Path("/missing")
        @GET
        @Produces("text/resource")
        public ActivationDataContentHandlerTest getResourceNoHandler() {
            return new ActivationDataContentHandlerTest();
        }
    }
    
    public static class TestResourceContentHandler implements DataContentHandler{

            public TestResourceContentHandler()
            {
            }

            public DataFlavor[] getTransferDataFlavors()
            {
                DataFlavor adataflavor[] = new DataFlavor[2];
                try
                {
                    adataflavor[0] = new ActivationDataFlavor(Class.forName("org.apache.wink.server.internal.activation.ActivationDataContentHandlerTest$TestResource"), "text/resource", "text string");
                }
                catch(Exception exception) { }
                adataflavor[1] = new DataFlavor("text/resource", "Plain Text");
                return adataflavor;
            }

            public Object getTransferData(DataFlavor dataflavor, DataSource datasource)
            {
                if(!dataflavor.getMimeType().startsWith("text/resource"))
                    throw new RuntimeException();
                if(dataflavor.getRepresentationClass().getName().equals("java.lang.String"))
                {
                    StringBuffer stringbuffer = new StringBuffer();
                    char ac[] = new char[1024];
                    int j = 0;
                    try
                    {
                        InputStreamReader inputstreamreader = new InputStreamReader(datasource.getInputStream());
                        do
                        {
                            int i = inputstreamreader.read(ac);
                            if(i <= 0)
                                break;
                            stringbuffer.append(ac, 0, i);
                            j += i;
                        } while(true);
                    }
                    catch(Exception exception1) { }
                    return stringbuffer.toString();
                }
                if(!dataflavor.getRepresentationClass().getName().equals("java.io.InputStream"))
                    throw new RuntimeException();
                try {
                    return datasource.getInputStream();
                } catch (IOException e) {
                    return null;
                }
            }

            public Object getContent(DataSource datasource)
            {
                StringBuffer stringbuffer = new StringBuffer();
                char ac[] = new char[1024];
                int j = 0;
                try
                {
                    InputStreamReader inputstreamreader = new InputStreamReader(datasource.getInputStream());
                    do
                    {
                        int i = inputstreamreader.read(ac);
                        if(i <= 0)
                            break;
                        stringbuffer.append(ac, 0, i);
                        j += i;
                    } while(true);
                }
                catch(Exception exception) { }
                return stringbuffer.toString();
            }

            public void writeTo(Object obj, String s, OutputStream outputstream)
                throws IOException
            {
                if(!s.startsWith("text/resource"))
                {
                    throw new IOException((new StringBuilder()).append("Invalid type \"").append(s).append("\" on StringDCH").toString());
                } else
                {
                    OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
                    outputstreamwriter.write(((TestResource)obj).data);
                    outputstreamwriter.flush();
                    return;
                }
            }
     }

    public void testHandlerExists() throws Exception {
        System.out.println(TestResource.class.getName());
        MockHttpServletRequest mockRequest =
            MockRequestConstructor.constructMockRequest("GET", "/root/exists", "text/resource");

        MockHttpServletResponse response = invoke(mockRequest);
        String content = response.getContentAsString();
        assertTrue(content.equals(DATA));
   }
    
   public void testMissingHandlerExists() throws Exception {
        System.out.println(TestResource.class.getName());
        MockHttpServletRequest mockRequest =
        MockRequestConstructor.constructMockRequest("GET", "/root/missing", "text/resource");
        MockHttpServletResponse response = invoke(mockRequest);
        int family = response.getStatus() / 100;
        assertTrue(family == 5 || family == 4);
   }
    
   

}
