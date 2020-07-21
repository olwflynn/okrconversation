
import java.util.UUID;

public class Conversation {

    public String conversationText;
    public UUID uuid;
    public Contributor contributor;

    Conversation(String conversationText, Contributor contributor) {
        this.conversationText = conversationText;
        this.contributor = contributor;
        this.uuid = UUID.randomUUID();
    }
}
