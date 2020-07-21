
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.engine.Engine;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

import java.io.IOException;

public class ConnectTest {

    private static final String HOST = "localhost";
    private static final int PORT_ONE = 9200;
    private static final int PORT_TWO = 9201;
    private static final String SCHEME = "http";

    private static RestHighLevelClient restHighLevelClient;

    private static final String INDEX = "customer";

    public static synchronized RestHighLevelClient makeConnection() {

        if(restHighLevelClient == null) {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(HOST, PORT_ONE, SCHEME),
                            new HttpHost(HOST, PORT_TWO, SCHEME)));
        }

        return restHighLevelClient;
    }


    private static synchronized void closeConnection() throws IOException {
        restHighLevelClient.close();
        restHighLevelClient = null;
    }

    @Test
    public void testConnectionAndWriteThenRead() {
        RestHighLevelClient connection = makeConnection();

        Map<String, Object> jsonMap = new HashMap();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest = new IndexRequest("posts")
                .id("1").source(jsonMap);

        try {
            IndexResponse indexResponse = connection.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println("Saved data to ES..." + indexResponse);
        } catch (IOException ex) {
            System.out.println("Exception putting the data in: " + ex);
        }


        GetRequest getRequest = new GetRequest("posts",  "1");

        try {
            GetResponse getResponse = connection.get(getRequest, RequestOptions.DEFAULT);
            System.out.println(getResponse);
        } catch(IOException ex) {
            System.out.println("Exception getting the data: " + ex);
        }

        try {
            closeConnection();
        } catch (IOException ex) {
            System.out.println("Exception closing the connection: " + ex);
        }
    }


//    public static String getPersonById(String id){
//        GetRequest getPersonRequest = new GetRequest(INDEX, id);
//        GetResponse getResponse = null;
//        try {
//            getResponse = restHighLevelClient.get(getPersonRequest, RequestOptions.DEFAULT);
//        } catch (java.io.IOException e){
//            e.getLocalizedMessage();
//        }
//        return getResponse != null ?
//                getResponse.getSourceAsString() : null;
//    }

}
