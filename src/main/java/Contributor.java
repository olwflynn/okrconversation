import java.util.UUID;

public class Contributor {

    public String name;
    public UUID uuid;

    Contributor(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID();
    }
}
