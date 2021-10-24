import org.junit.jupiter.api.Test;

public class CanonicalPathTest {

    @Test
    public void getCanonicalPathTest() {
        String test1 = CanonicalPath.getCanonicalPath("/../");
        assert (test1.equals("/"));
    }

}
