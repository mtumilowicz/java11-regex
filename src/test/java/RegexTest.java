import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by mtumilowicz on 2018-12-08.
 */
public class RegexTest {
    
    @Test
    public void string_matches() {
        // hour
        assertTrue("1:11".matches("[0-2]?\\d:[0-5]\\d"));
        assertTrue("9:11".matches("[0-2]?\\d:[0-5]\\d"));
        assertTrue("0:11".matches("[0-2]?\\d:[0-5]\\d"));
        
        // minute
        assertTrue("1:00".matches("[0-2]?\\d:[0-5]\\d"));
        assertTrue("9:01".matches("[0-2]?\\d:[0-5]\\d"));
        assertTrue("0:59".matches("[0-2]?\\d:[0-5]\\d"));
        assertFalse("0:60".matches("[0-2]?\\d:[0-5]\\d"));

        // hh
        assertTrue("00:00".matches("[0-2]?\\d:[0-5]\\d"));
        assertTrue("01:00".matches("[0-2]?\\d:[0-5]\\d"));
        assertTrue("21:00".matches("[0-2]?\\d:[0-5]\\d"));
        assertFalse("30:00".matches("[0-2]?\\d:[0-5]\\d"));
    }
    
    @Test
    public void string_split() {
        "a".split("a");
    }
    
    @Test
    public void string_split_limit() {
        "a".split("a", 1);
    }
    
    @Test
    public void string_replaceAll() {
        "a".replaceAll("a", "a");
    }
    
    @Test
    public void string_replaceFirst() {
        "a".replaceFirst("a", "a");
    }

    @Test
    public void pattern_emails() throws IOException {
        Pattern emailPattern = Pattern.compile("[_.\\w]+@([\\w]+\\.)+[\\w]{2,20}", Pattern.CASE_INSENSITIVE);
        
        List<String> emails;
        
        try (var reader = Files.newBufferedReader(Path.of("emails.txt"))) {
            emails = reader.lines()
                    .filter(emailPattern.asMatchPredicate())
                    .collect(Collectors.toList());
        }

        assertThat(emails, hasSize(5));
        assertThat(emails, contains(
                "michaltumilowicz@tlen.pl",
                "michal_tumilowicz@tlen.pl",
                "MichalTumilowicz@gmail.com",
                "a.b_cD@a.b.c.d.pl",
                "m12@wp.com.pl"));
    }
}
