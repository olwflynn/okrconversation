
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.UUID;

public class Conversation {

    public String conversationText;
    public UUID uuid;
    public UUID contributorId;
    public ConversationType conversationType;

    @JsonCreator
    Conversation(String conversationText, Contributor contributor, UUID uuid, ConversationType conversationType) {
        this.conversationText = conversationText;
        this.contributorId = contributor.uuid;
        this.uuid = uuid;
        this.conversationType = conversationType;
    }
}
