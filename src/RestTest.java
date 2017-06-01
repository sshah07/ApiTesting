import static org.junit.Assert.*;

import org.apache.http.impl.bootstrap.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockingDetails;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.*;
import javax.naming.spi.DirStateFactory.Result;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import java.awt.PageAttributes.MediaType;
import java.io.*;
import com.jayway.restassured.internal.http.Status;
import com.jayway.restassured.response.Response;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.ClientFilter;

import junit.framework.Assert;


@RunWith( MockitoJUnitRunner.class )
public class RestTest 
{
	Response response;
	public static final String BASE_URI = "http://localhost:8080/ApiTesting/api/";
    private HttpServer server;

	@Mock
	private App testObj;
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	
	@Before
	public void setUp() throws Exception {
		testObj = new App();
		MockitoAnnotations.initMocks(this);
		System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}
	@Test
	  public void testAssertBadRequest() {
	    //when( response.getStatusCode() ).thenReturn( Status.BAD_REQUEST.getStatusCode() );
	    
	    assertBadRequest( response );
	  }
	
	/* to check correct response is stored in text file or not */
	public void testRunFailureResultCanBeSerialised() throws Exception {
        JUnitCore runner = new JUnitCore();
        //Result result = runner.run(AnnotationTest.FailureTest.class);
        //assertResultSerializable(result);
    }

    public void testRunSuccessResultCanBeSerialised() throws Exception {
        JUnitCore runner = new JUnitCore();
        //org.junit.runner.Result result = runner.run(Success.class);
        //assertResultSerializable(result);
    }

    /* to check whether variety of request projects are stored or not*/
    private static final String URL = "http://localhost";
    
    @Test
    public void testCreateResponse() {
      Map<String, List<String>> headers = new HashMap<String, List<String>>();
      
      //Response response = CallbackResource.createResponse( URL, Status.OK, MediaType.WILDCARD, "body", headers );
      
      //assertEquals( Status.OK.getStatusCode(), response.getStatus() );
      //assertEquals( MediaType.WILDCARD, response.getType() );
      assertEquals( "body", response.getBody() );
      assertEquals( headers, response.getHeaders() );
    }
    
    @Test
    public void testCreateResponseCreatesHeaderSaveCopy() {
      Map<String, List<String>> headers = new HashMap<String, List<String>>();
      
      //Response response = CallbackResource.createResponse( URL, Status.OK, MediaType.WILDCARD, "body", headers );
      
      assertNotSame( headers, response.getHeaders() );
    }
    
    @Test
    public void testCreateResponseHasBody() {
      Map<String, List<String>> headers = new HashMap<String, List<String>>();
      
      //Response response = testObj.createResponse( URL, Status.OK, MediaType.WILDCARD, "body", headers );
      
      //assertTrue( response.hasBody() );
    }
    
    @Test
    public void testCreateResponseHasNoBody() {
      Response response 
      = testObj.createResponse( URL, Status.OK, MediaType.WILDCARD, null, null );
      
      assertFalse( response.hasBody() );
    }
    
    @Test
    public void testCreateResponseHasNoHeaders() {
      Response response 
      = testObj.createResponse( URL, Status.OK, MediaType.WILDCARD, null, null );
      
      assertNull( response.getHeaders() );
    }
    
    
    private void assertResultSerializable(org.junit.runner.Result result) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(result);
        objectOutputStream.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Result fromStream = (Result) objectInputStream.readObject();
        assertSerializedCorrectly(result, fromStream);

        InputStream resource = getClass().getResourceAsStream(getName());
        assertNotNull("Could not read resource " + getName(), resource);
        objectInputStream = new ObjectInputStream(resource);
        fromStream = (Result) objectInputStream.readObject();
        
        assertSerializedCorrectly(new ResultWithFixedRunTime(result), fromStream);
    }

	
	  
	  @Test( expected = AssertionError.class )
	  public void testAssertBadRequestFails() {
	    when( response.getStatusCode() ).thenReturn( Status.ACCEPTED.getStatusCode() );
	    
	    assertBadRequest( response );
	  }
	 private void assertBadRequest(Response response2) {
		// TODO Auto-generated method stub
		
	}
	@Test
	    public void testMockedGreetingService() {
	        Client client = ClientFilter.newClient();
	        Response response = ((Object) client).target("http://localhost:8080/ApiTesting/api/createproject")
	                .queryParam("name", "peeskillet")
	                .request(MediaType.TEXT_PLAIN).get();
	        Assert.assertEquals(200, response.getStatusCode());

	        String msg = response.readEntity(String.class);
	        Assert.assertEquals("Hello peeskillet", msg);
	        System.out.println("Message: " + msg);

	        response.close();
	        client.close();

	    }
	@Test
	  public void testAssertAccpeted() {
	    javax.ws.rs.core.Response response;
		when( response.getStatus() ).thenReturn( Status.ACCEPTED.getStatusCode() );
	    
	    assertAccepted( response );
	  }
	  
	  private Object when(int status) {
		// TODO Auto-generated method stub
		return null;
	}

	private void assertAccepted(javax.ws.rs.core.Response response) {
		// TODO Auto-generated method stub
		
	}

	@Test( expected = AssertionError.class )
	  public void testAssertAccpetedFails() {
	    javax.ws.rs.core.Response response;
		when( response.getStatus() ).thenReturn( Status.NOT_FOUND.getStatusCode() );
	    
	    assertAccepted( response );
	  }
	  
	  @Test
	  public void testAssertNotFound() {
	    when( response.getStatusCode() ).thenReturn( Status.NOT_FOUND.getStatusCode() );
	    
	    assertNotFound( response );
	  }
	  
	  @Test( expected = AssertionError.class )
	  public void testAssertNotFoundFails() {
	    when( response.getStatusCode() ).thenReturn( Status.ACCEPTED.getStatusCode() );
	    
	    assertNotFound( response );
	  }
	  

	@After
	public void tearDown() throws Exception 
	{
		
	}
	@Test
	public void out() {
	    System.out.print("hello");
	    assertEquals("hello shachi", outContent.toString());
	}

	@Test
	public void err() {
	    System.err.print("hello again");
	    assertEquals("hello shachi again", errContent.toString());
	}
	@Test
	public void varyUrlParamtest()
	{
		
		assertEquals(testObj.createproject(null), "project1");
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
