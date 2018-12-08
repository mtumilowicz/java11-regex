# java11-regex

_Reference_: https://docs.oracle.com/javase/10/docs/api/java/util/regex/Pattern.html  
_Reference_: https://stackoverflow.com/questions/5319840/greedy-vs-reluctant-vs-possessive-quantifiers  
_Reference_: https://javascript.info/regexp-groups#example  
_Reference_: https://stackoverflow.com/questions/6664151/difference-between-b-and-b-in-regex  
_Reference_: https://stackoverflow.com/questions/4250062/what-is-the-difference-between-and-a-and-z-in-regex

# preface
A regular expression is a way to describe a pattern in a sequence 
of characters.

## characters
|Construct   |Matches   |
|---|---|
|`x`    |The character x   |
|`\\`   |The backslash character   |
|`\t`   |The tab character (`'\u0009'`)   |
|`\n`   |The newline (line feed) character (`'\u000A'`)   |

## character classes
|Construct   |Matches   |
|---|---|
|`[abc]`   |a, b, or c (simple class)   |
|`[^abc]`   |Any character except a, b, or c (negation)   |
|`[a-zA-Z]`   |a through z or A through Z, inclusive (range)   |
|`[a-d[m-p]]`   |a through d, or m through p: `[a-dm-p]` (union)   |
|`[a-z&&[def]]`   |d, e, or f (intersection)   |
|`[a-z&&[^bc]]`   |a through z, except for b and c: `[ad-z]` (subtraction)   |
|`[a-z&&[^m-p]]`   |a through z, and not m through p: `[a-lq-z]`(subtraction)   |

## predefined character classes
|Construct   |Matches   |
|---|---|
|`.`  |   Any character (may or may not match line terminators)|
|`\d` |   A digit: `[0-9]`|
|`\D` |   A non-digit: `[^0-9]`|
|`\s` |   A whitespace character: `[ \t\n\x0B\f\r]`|
|`\S` |   A non-whitespace character: `[^\s]`|
|`\w` |   A word character: `[a-zA-Z_0-9]`|
|`\W` |   A non-word character: `[^\w]`|

## boundary matchers
|Construct   |Matches   |
|---|---|
|`^`    |The beginning of a line|
|`$`    |The end of a line|
|`\b`   |A word boundary|
|`\B`   |A non-word boundary|
|`\A`   |The beginning of the input|
|`\z`   |The end of the input|

## linebreak matcher
`\R` - Any Unicode linebreak sequence

## greedy, reluctant, possessive
* A **greedy** quantifier first matches as much as possible and 
then "backtracs" one by one element towards the beginning.

* A **reluctant** or "non-greedy" quantifier first matches 
as little as possible then goes one by one element towards 
the end.

* A **possessive** quantifier is just like the greedy 
quantifier, but it doesn't backtrack.

### greedy quantifiers

|Construct   |Matches   |
|---|---|
|`X?`       |X, once or not at all|
|`X*`       |X, zero or more times|
|`X+`       |X, one or more times|
|`X{n}`     |X, exactly n times|
|`X{n,}`    |X, at least n times|
|`X{n,m}`   |X, at least n but not more than m times|

### reluctant quantifiers

|Construct   |Matches   |
|---|---|
|`X??`        |X, once or not at all|
|`X*?`        |X, zero or more times|
|`X+?`        |X, one or more times|
|`X{n}?`      |X, exactly n times|
|`X{n,}?`     |X, at least n times|
|`X{n,m}?`    |X, at least n but not more than m times|

### possessive quantifiers

|Construct   |Matches   |
|---|---|
|`X?+`        |X, once or not at all|
|`X*+`        |X, zero or more times|
|`X++`        |X, one or more times|
|`X{n}+`      |X, exactly n times|
|`X{n,}+`     |X, at least n times|
|`X{n,m}+`    |X, at least n but not more than m times|

## logical operators

|Construct   |Matches   |
|---|---|
|`XY`       |X, once or not at all|
|X&#124;Y   |Either X or Y|
|`(X)`        |X, as a capturing group|

* Capturing group example:
    ```
    assertTrue("gogogogo regex".matches("(go)+\\sregex"));
    ```
    starts with "go" once or many times then space then 
    regex
    
## escaping
The backslash character ('\') serves to introduce 
escaped constructs, as defined in the table above, 
as well as to quote characters that otherwise would 
be interpreted as unescaped constructs.

## typical invocation
A typical invocation sequence is:
```
Pattern p = Pattern.compile("a*b");
Matcher m = p.matcher("aaaaab");
boolean b = m.matches();
```
or
```
boolean b = Pattern.matches("a*b", "aaaaab");
```
But note that this method compiles an expression and matches an 
input sequence against it in a single invocation, so it is
equivalent to the three statements above, though for 
repeated matches it is less efficient since it does not 
allow the compiled pattern to be reused.

# project description
## string regex methods
* `public boolean matches(String regex)`
    ```
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
    ```
    * **remark**: `matches` is equivalent to the three 
    statements below, though for repeated matches it is 
    less efficient since it does not allow the compiled 
    pattern to be reused.
        ```
        Pattern p = Pattern.compile("[0-2]?\\d:[0-5]\\d");
        Matcher m = p.matcher("11:11");
        ```
* `public String[] split(String regex)`
    ```
    var namesContainer = "Michal--|--Marcin--|--Wojtek--|--Ania";
    String[] names = namesContainer.split("--\\|--");
   
    assertThat(names, is(new String[]{"Michal", "Marcin", "Wojtek", "Ania"}));
    ```
* `public String[] split(String regex, int limit)` - 
limit is size of returned array
    ```
    String[] names = "Michal--|--Marcin--|--Wojtek--|--Ania".split("--\\|--", 3);
    
    assertThat(names, is(new String[]{"Michal", "Marcin", "Wojtek--|--Ania"}));
    ```
* `public String replaceAll(String regex, String replacement)`
    ```
    String transformed = "Michal--|--Marcin--|--Wojtek--|--Ania".replaceAll("--\\|--", "|");
    
    assertThat(transformed, is("Michal|Marcin|Wojtek|Ania"));
    ```
* `public String replaceFirst(String regex, String replacement)`
    ```
    String transformed = "Michal--|--Marcin--|--Wojtek--|--Ania".replaceFirst("--\\|--", "|");
    
    assertThat(transformed, is("Michal|Marcin--|--Wojtek--|--Ania"));
    ```

## pattern methods
* `public static Pattern compile(String regex)`
    * very handy is method `public Predicate<String> asMatchPredicate()`
    (since java11) which creates a predicate that 
    tests if this pattern matches a given input string
    ```
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
    ```
    * where `[_.\\w]+@([\\w]+\\.)+[\\w]{2,20}` is:
        * `[_.\\w]+` - either (`_`, `.`, letter/digit) once or more times
        * then `@`
        * `([\\w]+\\.)+` - (letter/digit once or more times with single dot) once or many times
        * `[\\w]{2,20}` - letter/digits twice to twenty times
* `public static Pattern compile(String regex, int flags)`
    * useful flag: `Pattern.CASE_INSENSITIVE`
* `public static boolean matches(String regex, CharSequence input)`
    ```
    public static boolean matches(String regex, CharSequence input) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        return m.matches();
    }
    ```

## boundary matchers
* `\b` - A word boundary
    * end
        ```
        var txt = "catmania thiscat thiscatmania";
        
        String replaced = txt.replaceAll("cat\\b", "-");
        
        assertThat(replaced, is("catmania this- thiscatmania"));
        ```
    * beginning
        ```
        var txt = "catmania thiscat thiscatmania";
        
        String replaced = txt.replaceAll("\\bcat", "-");
        
        assertThat(replaced, is("-mania thiscat thiscatmania"));
        ```
* `\B` - A non-word boundary
    * not end
        ```
        var txt = "catmania thiscat thiscatmania";

        String replaced = txt.replaceAll("cat\\B", "-");

        assertThat(replaced, is("-mania thiscat this-mania"));
        ```
    * not beginning
        ```        
        var txt = "catmania thiscat thiscatmania";
        
        String replaced = txt.replaceAll("\\Bcat", "-");
        
        assertThat(replaced, is("catmania this- this-mania"));
        ```
    * neither beginning nor end
        ```
        var txt = "catmania thiscat thiscatmania";
        
        String replaced = txt.replaceAll("\\Bcat\\B", "-");
        
        assertThat(replaced, is("catmania thiscat this-mania"));
        ```
* `\A` - The beginning of the input and `\z` - The end of the input vs
`^` and `$`
