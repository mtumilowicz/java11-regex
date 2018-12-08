import org.junit.Test;

/**
 * Created by mtumilowicz on 2018-12-08.
 */
public class RegexTest {
    
    @Test
    public void string_regex_methods() {
        "a".matches("a");
        "a".split("a");
        "a".split("a", 1);
        "a".replaceAll("a", "a");
        "a".replaceFirst("a", "a");
    }
}
