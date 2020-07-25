
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;
import org.elasticsearch.client.*;

public class Main {

    // used for error handling
    static boolean hadError = false;
    private static class ContributorDoesNotExistException extends NullPointerException {}


    private static final Leader leader = new Leader("Oli");

    public Main(){

    }

    public static void main(String[] args) throws IOException {
	    runPrompt();
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (; ; ) {
            System.out.print("> ");
            run(reader.readLine());
            hadError = false;
        }
    }

    private static void run(String source) throws IOException {

        if (source.substring(0,2).equals("-n")) {
            leader.addContributor(source.substring(2).trim());

        }

        // Write notes with contributor id
        if (source.substring(0,3).equals("-g ")) {
            String inputId = source.substring(3);
            UUID uuid = UUID.fromString(inputId);
            Contributor contributor = leader.getContributorFromId(uuid);
            if (contributor != null) {
                writeNotes(contributor);
            }
        }

        // Write notes with contributor name
        if (source.substring(0,2).equals("-c")) {
            String inputName = source.substring(2);
            Contributor contributor = checkContributorExists(inputName);
            if (contributor != null) {
                writeNotes(contributor);
            }

        }

        if (source.substring(0,6).equals("-gconv")) {
            String inputStringUUID = source.substring(6).trim();
            try {
                UUID uuid = UUID.fromString(inputStringUUID);
                Conversation conversation = leader.getConversation(uuid);
                if (conversation != null) {
                    System.out.println("Here is your conversation with " + conversation.contributor.name + ": " + conversation.conversationText);
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Conversation " + inputStringUUID + " does not exist.");
            }
        }


        if (hadError) return;

    }


    // need to fix these functions as not being recognised
    private static ContributorDoesNotExistException error(String message) {
        System.out.println("Contributor: '" + message.trim() + "' does not exist.");
        return new ContributorDoesNotExistException();
    }

    private static Contributor checkContributorExists(String name) {
        String inputName = name;
        try {
            Contributor contributor = leader.getContributor(inputName.trim());
            System.out.println("Contributor: '" + contributor.name + "' exists!");
            return contributor;

        } catch (NullPointerException ex) {

            List<String> contributorIdPairsES = leader.getContributorAndIdFromES(inputName.trim());
            if (contributorIdPairsES.isEmpty()) {
                System.out.println("Contributor: '" + inputName.trim() + "' does not exist.");
                return null;
            } else {
                System.out.println("It seems you have a few contributors with the same id. Please choose which one you " +
                        "mean by their id and then use the `-g {id}` command write their notes...");
                for (String pair : contributorIdPairsES) System.out.println(pair);
                return null;
            }
        }

    }

    private static void writeNotes(Contributor contributor) {
        System.out.print("Thanks! Now write your conversation notes here: ");
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        try {
            String conversationText = reader.readLine();
            Conversation conversation = leader.addConversation(conversationText, contributor);
            System.out.println("Added your conversation '" + conversation.uuid + "' with " + contributor.name + ".");
        } catch (IOException ex) {
            System.out.println("readline IO: " + ex);
        }
    }
}
