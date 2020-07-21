
import org.junit.*;

public class LeaderTest {

    @Test
    public void addContributorWithNameAddsToContributorsListWithName() {
        String expectedName = "Test name";
        Leader leader = new Leader("Oli");
        leader.addContributor(expectedName);
        Assert.assertNotNull("Test contributor is not present in Contributors List", leader.getContributor(expectedName));
    }
}
