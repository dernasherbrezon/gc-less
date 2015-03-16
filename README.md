There are several alternatives exist
 * jdk
 * google guava
 * commons-lang

However all of them have drawbacks. gc-less utilities aimed to take the best ideas:
 * custom functions instead of generic pattern-based jdk utilities
 * simple and non-functional implementation instead of verbose guava
 * advanced algorithms instead of brute style of commons-lang.

Currently:
 * Iterable<?> instead of Collection. Return algorithm rather than data.
 * Trim & handle unicode whitespaces

Maven
-----

```
<dependency>
        <groupId>com.google.code</groupId>
        <artifactId>gcless</artifactId>
        <version>7.0</version>
</dependency>

<repositories>
        <repository>
                <id>gcless-repo</id>
                <url>http://gc-less.googlecode.com/svn/trunk/maven/</url>
        </repository>
</repositories>
```
