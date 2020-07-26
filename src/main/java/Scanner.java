import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private final String source;
    private static final Map<String, InputCommand> keywords;
    private Integer start = 0;
    public Integer current = 0;

    static {
        keywords = new HashMap<String, InputCommand>();
        keywords.put("-n", InputCommand.ADD_CONTRIBUTOR);
        keywords.put("-cid", InputCommand.ADD_NOTES_BY_ID);
        keywords.put("-c", InputCommand.ADD_NOTES_BY_NAME);
        keywords.put("-gconv", InputCommand.GET_NOTES_BY_ID);
    }

    Scanner(String source) {
        this.source = source;
    }

    public InputCommand scanCommand() {

        if (source.charAt(0) != '-') {
            return null;
        }
        start = 0;
        while (!isAtEndCommand()) {
            String inputString = source.substring(0, current);
            if (source.charAt(current) == ' ') {
                InputCommand inputCommand = keywords.get(inputString);
                return inputCommand;
            }
            current+=1;
        }
        System.out.println("Invalid command. All commands must start with '-'.");
        return null;
    }


    private boolean isAtEndCommand() {
        return current >= source.length();
    }
}
