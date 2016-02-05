package org.yetiz.serv;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by yeti on 16/2/5.
 */
public class ResourceOperationHandlerTest {

    private String key = "GOGOGO";
    private String value = "POWERRANGER";

    @Test
    public void testChannelRead() throws Exception {

        HTTPCacheServer server = new HTTPCacheServer();
        new Thread(() -> server.start(8080)).start();
        while (!server.status().equals(HTTPCacheServer.STATUS.Connected)) {
            Thread.sleep(100);
        }
        AsyncHttpClient client = new AsyncHttpClient();
        // POST
        Response response = client.preparePost("http://localhost:8080/" + key).setBody(value.getBytes())
            .execute()
            .get();
        Assert.assertEquals(200, response.getStatusCode());
        // GET
        response = client.prepareGet("http://localhost:8080/" + key)
            .execute()
            .get();
        Assert.assertArrayEquals(value.getBytes(), response.getResponseBodyAsBytes());
        client.close();
        server.stop();
    }
}