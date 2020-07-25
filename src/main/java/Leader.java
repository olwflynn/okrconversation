
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;

public class Leader {

    private final String name;
    private Map<String, Contributor> contributors = new HashMap();
    private Map<UUID, Conversation> conversations = new HashMap();
    private Connect connection = new Connect();

    // In order to initialise the contributors at startup we need to figure out the bug from ES of having to write first
    //    private SearchHit[] contributorHits;


    public Leader(String name) {
        this.name = name;
//        this.contributorHits = getAllContributors();
//        System.out.println(this.contributorHits);
    }

    public void addContributor(String contributorName) {
        Contributor contributor = new Contributor(contributorName, UUID.randomUUID());
        contributors.put(contributor.name, contributor);
        IndexRequest indexRequest = connection.buildIndexRequest(contributor, "contributors");
        IndexResponse indexResponse = connection.index(indexRequest);
    }

    public Contributor getContributor(String contributorName) {
        if (contributors.containsKey(contributorName)){
            Contributor contributor = contributors.get(contributorName);
            return contributor;
        }
        return null;
    }

    public Contributor getContributorFromId(UUID uuid) {

        GetRequest getRequest = new GetRequest("contributors", uuid.toString());
        GetResponse getResponse = connection.get(getRequest);
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        String contributorName = (String) sourceAsMap.get("name");
        Contributor contributor = new Contributor(contributorName, uuid);
        return contributor;
    }

    public List<String> getContributorAndIdFromES(String contributorName) {
        SearchRequest searchRequest = connection.buildSearchRequest("contributors",3,"name", contributorName);
        try {
            SearchResponse searchResponse = connection.search(searchRequest);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            List<String> contributorAndIdArray = new ArrayList<String>();
            for (SearchHit hit : searchHits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                String contributor = (String) sourceAsMap.get("name");
                String uuidString = hit.getId();
                String contributorAndId = contributor + " has id: " + uuidString;
                contributorAndIdArray.add(contributorAndId);
            }
            return contributorAndIdArray;


        } catch (NullPointerException ex) {
            System.out.println("Null search: "+ ex);
        }
        return null;
    }

    public Conversation addConversation(String conversationText, Contributor contributor, String conversationTypeString) {
        Conversation newConversation = new Conversation(conversationText, contributor, UUID.randomUUID(),
                ConversationType.valueOf(conversationTypeString));
//        conversations.put(newConversation.uuid, newConversation);
        System.out.println(newConversation);
        IndexRequest indexRequest = connection.buildIndexRequest(newConversation, "conversations");
        IndexResponse indexResponse = connection.index(indexRequest);
        return newConversation;
    }

    public Conversation getConversation(UUID uuid) {
        if (conversations.containsKey(uuid)){
            Conversation conversation = conversations.get(uuid);
            GetRequest getRequest = new GetRequest("conversations", uuid.toString(),"test");
            GetResponse getResponse = connection.get(getRequest);
            return conversation;
        }
        return null;

    }

    private SearchHit[] getAllContributors() {
        SearchRequest searchRequest = new SearchRequest("contributors");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = connection.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        return searchHits;
    }
}

