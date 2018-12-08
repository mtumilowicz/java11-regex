# java11-regex

_Reference_: https://docs.oracle.com/javase/10/docs/api/java/util/regex/Pattern.html  
_Reference_: https://stackoverflow.com/questions/5319840/greedy-vs-reluctant-vs-possessive-quantifiers
_Reference_: https://javascript.info/regexp-groups#example

# preface
A regular expression is a way to describe a pattern in a sequence 
of characters.

## characters
|Construct   |Matches   |
|---|---|
|x   |The character x   |
|`\\`   |The backslash character   |
|\t   |The tab character ('\u0009')   |
|\n   |The newline (line feed) character ('\u000A')   |

## character classes
|Construct   |Matches   |
|---|---|
|[abc]   |a, b, or c (simple class)   |
|[^abc]   |Any character except a, b, or c (negation)   |
|[a-zA-Z]   |a through z or A through Z, inclusive (range)   |
|[a-d[m-p]]   |a through d, or m through p: [a-dm-p] (union)   |
|[a-z&&[def]]   |d, e, or f (intersection)   |
|[a-z&&[^bc]]   |a through z, except for b and c: [ad-z] (subtraction)   |
|[a-z&&[^m-p]]   |a through z, and not m through p: [a-lq-z](subtraction)   |

## predefined character classes
|Construct   |Matches   |
|---|---|
|.  |   Any character (may or may not match line terminators)|
|\d |   A digit: [0-9]|
|\D |   A non-digit: [^0-9]|
|\s |   A whitespace character: [ \t\n\x0B\f\r]|
|\S |   A non-whitespace character: [^\s]|
|\w |   A word character: [a-zA-Z_0-9]|
|\W |   A non-word character: [^\w]|

## boundary matchers
|Construct   |Matches   |
|---|---|
|^|The beginning of a line|
|$|The end of a line|
|\b|A word boundary|
|\B|A non-word boundary|
|\A|The beginning of the input|
|\z|The end of the input|

## linebreak matcher
\R	Any Unicode linebreak sequence

## greedy, reluctant, possessive
A greedy quantifier first matches as much as possible. So the .* matches the entire string. Then the matcher tries to match the f following, but there are no characters left. So it "backtracks", making the greedy quantifier match one less thing (leaving the "o" at the end of the string unmatched). That still doesn't match the f in the regex, so it "backtracks" one more step, making the greedy quantifier match one less thing again (leaving the "oo" at the end of the string unmatched). That still doesn't match the f in the regex, so it backtracks one more step (leaving the "foo" at the end of the string unmatched). Now, the matcher finally matches the f in the regex, and the o and the next o are matched too. Success!

A reluctant or "non-greedy" quantifier first matches as little as possible. So the .* matches nothing at first, leaving the entire string unmatched. Then the matcher tries to match the f following, but the unmatched portion of the string starts with "x" so that doesn't work. So the matcher backtracks, making the non-greedy quantifier match one more thing (now it matches the "x", leaving "fooxxxxxxfoo" unmatched). Then it tries to match the f, which succeeds, and the o and the next o in the regex match too. Success!

In your example, it then starts the process over with the remaining unmatched portion of the string, following the same process.

A possessive quantifier is just like the greedy quantifier, but it doesn't backtrack. So it starts out with .* matching the entire string, leaving nothing unmatched. Then there is nothing left for it to match with the f in the regex. Since the possessive quantifier doesn't backtrack, the match fails there.

### greedy quantifiers

|Construct   |Matches   |
|---|---|
|X?|X, once or not at all|
|X*|X, zero or more times|
|X+|X, one or more times|
|X{n}|X, exactly n times|
|X{n,}|X, at least n times|
|X{n,m}|X, at least n but not more than m times|

### reluctant quantifiers

|Construct   |Matches   |
|---|---|
|X??|X, once or not at all|
|X*?|X, zero or more times|
|X+?|X, one or more times|
|X{n}?|X, exactly n times|
|X{n,}?|X, at least n times|
|X{n,m}?|X, at least n but not more than m times|

### possessive quantifiers

|Construct   |Matches   |
|---|---|
|X?+|X, once or not at all|
|X*+|X, zero or more times|
|X++|X, one or more times|
|X{n}+|X, exactly n times|
|X{n,}+|X, at least n times|
|X{n,m}+|X, at least n but not more than m times|

## logical operators

|Construct   |Matches   |
|---|---|
|XY|X, once or not at all|
|X&#124;Y|Either X or Y|
|(X)|X, as a capturing group|

Capturing group example:
alert( 'Gogogo now!'.match(/(go)+/i) ); // "Gogogo"
Without parentheses, the pattern /go+/ means g, followed by o repeated one or more times. For instance, goooo or gooooooooo.

Parentheses group the word (go) together.

The backslash character ('\') serves to introduce escaped constructs, as defined in the table above, as well as to quote characters that otherwise would be interpreted as unescaped constructs. Thus the expression \\ matches a single backslash and \{ matches a left brace.

A typical invocation sequence is thus

 Pattern p = Pattern.compile("a*b");
 Matcher m = p.matcher("aaaaab");
 boolean b = m.matches();
A matches method is defined by this class as a convenience for when a regular expression is used just once. This method compiles an expression and matches an input sequence against it in a single invocation. The statement

 boolean b = Pattern.matches("a*b", "aaaaab");
is equivalent to the three statements above, though for repeated matches it is less efficient since it does not allow the compiled pattern to be reused.