
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.yetiz.serv.HTTPCacheServer;

import java.io.IOException;

/**
 * Created by Keith on 2/5/16.
 */
public class MainTest {
    private HTTPCacheServer server;

    private static final int PORT = 8080;
    private static final String HOST = "http://localhost:" + PORT + "/";

    @Before
    public void startUpServer() {
        server = new HTTPCacheServer();
        server.start(PORT);
    }

    @After
    public void stopServer() {
        server.stop();
    }

    @Test
    public void testInsertion() {
        String cacheKey = "KeyFor1234";
        String myData = "One2Three4";

        try {
            HttpPost post = new HttpPost(HOST + cacheKey);
            post.setEntity(new StringEntity(myData, Charsets.UTF_8));
            HttpResponse addResult = execute(new HttpPost(HOST + cacheKey));
            Assert.assertTrue(addResult.getStatusLine().getStatusCode() == HttpStatus.SC_OK);
            Assert.assertTrue(addResult.getEntity().getContentLength() == myData.length());


            HttpResponse queryResult = execute(new HttpGet(HOST + cacheKey));
            Assert.assertTrue(queryResult.getStatusLine().getStatusCode() == HttpStatus.SC_OK);
            Assert.assertEquals(myData, EntityUtils.toString(queryResult.getEntity(), Charsets.UTF_8));


            HttpResponse deleteResult = execute(new HttpDelete(HOST + cacheKey));
            Assert.assertTrue(deleteResult.getStatusLine().getStatusCode() == HttpStatus.SC_OK);

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testDeletion() {
        String cacheKey = "KeyForABCD";
        String myData = "Absolutely Brief Class Diagram";

        try {
            HttpPost post = new HttpPost(HOST + cacheKey);
            post.setEntity(new StringEntity(myData, Charsets.UTF_8));
            HttpResponse addResult = execute(new HttpPost(HOST + cacheKey));
            Assert.assertTrue(addResult.getStatusLine().getStatusCode() == HttpStatus.SC_OK);
            Assert.assertTrue(addResult.getEntity().getContentLength() == myData.length());

            HttpResponse deleteResult = execute(new HttpDelete(HOST + cacheKey));
            Assert.assertTrue(deleteResult.getStatusLine().getStatusCode() == HttpStatus.SC_OK);

            HttpResponse queryResult = execute(new HttpGet(HOST + cacheKey));
            Assert.assertTrue(queryResult.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND);

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    private HttpResponse execute(HttpUriRequest request) throws IOException {
        return HttpClients.createDefault().execute(request);
    }
}
