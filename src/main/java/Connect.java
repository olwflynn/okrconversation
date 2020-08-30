import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.Index;
import org.elasticsearch.action.search.*;
import org.elasticsearch.index.mapper.ObjectMapper;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Connect {

    private static final String HOST = "127.0.0.1";
    private static final int PORT_ONE = 9200;
    private static final int PORT_TWO = 9201;
    private static final String SCHEME = "http";

    private static RestHighLevelClient restHighLevelClient;

    private static final String INDEX = "customer";

    public Connect() {
        RestHighLevelClient restHighLevelClient = makeConnection();
    }

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

        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

        try {

            Class<?> classObject = data.getClass();
            Field field = classObject.getField("uuid"); //Note, this can throw an exception if the field doesn't exist.
            Object uuid = field.get(data);

            String dataString = mapper.writeValueAsString(data);
            System.out.println(dataString);
            IndexRequest indexRequest = new IndexRequest(indexName)
                    .id(uuid.toString()).source(dataString, XContentType.JSON);
            System.out.println(indexRequest);
            return indexRequest;

        } catch (JsonProcessingException exJson) {
            System.out.println("Couldn't handle Json conversion " + exJson);

        } catch (NoSuchFieldException exField) {
            System.out.println("No uuid field in class: " + exField);

        } catch (IllegalAccessException exAccess) {
            System.out.println("Not allowed to access uuid field: "+ exAccess);
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
