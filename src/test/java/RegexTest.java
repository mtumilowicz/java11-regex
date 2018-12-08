import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
        var namesContainer = "Michal--|--Marcin--|--Wojtek--|--Ania";
        String[] names = namesContainer.split("--\\|--");

        assertThat(names, is(new String[]{"Michal", "Marcin", "Wojtek", "Ania"}));
    }

    @Test
    public void string_split_limit() {
        String[] names = "Michal--|--Marcin--|--Wojtek--|--Ania".split("--\\|--", 3);

        assertThat(names, is(new String[]{"Michal", "Marcin", "Wojtek--|--Ania"}));
    }

    @Test
    public void string_replaceAll() {
        String transformed = "Michal--|--Marcin--|--Wojtek--|--Ania".replaceAll("--\\|--", "|");

        assertThat(transformed, is("Michal|Marcin|Wojtek|Ania"));
    }

    @Test
    public void string_replaceFirst() {
        String transformed = "Michal--|--Marcin--|--Wojtek--|--Ania".replaceFirst("--\\|--", "|");

        assertThat(transformed, is("Michal|Marcin--|--Wojtek--|--Ania"));
    }

    @Test
    public void pattern_emails() throws IOException {
        var emailPattern = Pattern.compile("[_.\\w]+@([\\w]+\\.)+[\\w]{2,20}");

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

    @Test
    public void capturingGroup() {
        assertTrue("gogogogo regex".matches("(go)+\\sregex"));
    }

    @Test
    public void word_boundary_end() {
        var txt = "catmania thiscat thiscatmania";

        String replaced = txt.replaceAll("cat\\b", "-");

        assertThat(replaced, is("catmania this- thiscatmania"));
    }

    @Test
    public void word_boundary_beginning() {
        var txt = "catmania thiscat thiscatmania";

        String replaced = txt.replaceAll("\\bcat", "-");

        assertThat(replaced, is("-mania thiscat thiscatmania"));
    }

    @Test
    public void non_word_boundary_not_beginning() {
        var txt = "catmania thiscat thiscatmania";

        String replaced = txt.replaceAll("\\Bcat", "-");

        assertThat(replaced, is("catmania this- this-mania"));
    }

    @Test
    public void non_word_boundary_not_end() {
        var txt = "catmania thiscat thiscatmania";

        String replaced = txt.replaceAll("cat\\B", "-");

        assertThat(replaced, is("-mania thiscat this-mania"));
    }

    @Test
    public void non_word_boundary_neither_beginning_nor_end() {
        var txt = "catmania thiscat thiscatmania";

        String replaced = txt.replaceAll("\\Bcat\\B", "-");

        assertThat(replaced, is("catmania thiscat this-mania"));
    }
}
