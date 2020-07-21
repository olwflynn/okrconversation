
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;
import java.util.*;

public class Leader {

    private final String name;
    private Map<String, Contributor> contributors = new HashMap();
    private Map<UUID, Conversation> conversations = new HashMap();
    private Connect connection = new Connect();


    public Leader(String name) {
        this.name = name;
    }

    public void addContributor(String contributorName) {
        Contributor contributor = new Contributor(contributorName);
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

    public String getContributorFromES(String contributorName) {
        SearchRequest searchRequest = connection.buildSearchRequest("contributors",2,"name", contributorName);
        try {
            SearchResponse searchResponse = connection.search(searchRequest);
            return searchResponse.toString();
        } catch (NullPointerException ex) {
            System.out.println("Null search: "+ ex);
        }
        return null;
    }

    public Conversation addConversation(String conversationText, Contributor contributor) {
        Conversation newConversation = new Conversation(conversationText, contributor);
        conversations.put(newConversation.uuid, newConversation);
        IndexRequest indexRequest = connection.buildIndexRequest(newConversation, "conversations");
        IndexResponse indexResponse = connection.index(indexRequest);
        return newConversation;
    }

    public Conversation getConversation(UUID uuid) {
        if (conversations.containsKey(uuid)){
            Conversation conversation = conversations.get(uuid);
            GetRequest getRequest = new GetRequest("conversations",  uuid.toString());
            GetResponse getResponse = connection.get(getRequest);
            return conversation;
        }
        return null;

    }
}
