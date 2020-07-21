import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.Index;
import org.elasticsearch.action.search.*;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Connect {

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

    public static synchronized void closeConnection() throws IOException {
        restHighLevelClient.close();
        restHighLevelClient = null;
    }

    public static IndexRequest buildIndexRequest(Object data, String indexName) {

        RestHighLevelClient connection = makeConnection();


        // Change the fields that you are putting in e.g. need to be putting in the object Contributor
        // Change the indexxx name
        Map<String, Object> jsonMap = new HashMap();
        if (data instanceof Conversation) {
            jsonMap.put("conversationText", ((Conversation) data).conversationText);
            jsonMap.put("contributorId", ((Conversation) data).contributor.uuid.toString());

            IndexRequest indexRequest = new IndexRequest(indexName)
                    .id(((Conversation) data).uuid.toString()).source(jsonMap);
            return indexRequest;
        }

        if (data instanceof Contributor) {
            jsonMap.put("name", ((Contributor) data).name);

            IndexRequest indexRequest = new IndexRequest(indexName)
                    .id(((Contributor) data).uuid.toString()).source(jsonMap);
            return indexRequest;
        }
        return null;
    }

    public static IndexResponse index(IndexRequest indexRequest) {

        try {
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println("Saved data to ES..." + indexResponse);
            return indexResponse;
        } catch (IOException ex) {
            System.out.println("Exception putting the data in: " + ex);
        }
        return null;
    }


    public static GetResponse get(GetRequest getRequest) {
        try {
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            System.out.println(getResponse);
            return getResponse;
        } catch(IOException ex) {
            System.out.println("Exception getting the data: " + ex);
        }
        return null;
    }


    // This is only for match all queries
    public static SearchRequest buildSearchRequest(String indexName, Integer size, String field, String queryTerm) {
            SearchRequest searchRequest = new SearchRequest(indexName);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(field, queryTerm);
            searchSourceBuilder.query(matchQueryBuilder);
//            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
//            searchSourceBuilder.size(size);
            searchRequest.source(searchSourceBuilder);
            System.out.println(searchRequest);
            return searchRequest;
    }

    public static SearchResponse search(SearchRequest searchRequest) {
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return searchResponse;
        } catch (IOException ex) {
            System.out.println("Exception getting the data: " + ex);
        }
        return null;
    }

}
