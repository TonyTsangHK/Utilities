package testng;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.file.FileUtil;
import utils.file.path.PathPatternMatcherGroup;
import utils.file.path.PathPatternMatcherGroupParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-01-05
 * Time: 15:12
 */
public class TestPathMatcherGroup {
    private List<String> ignoredEntries, acceptedEntries;
    private PathPatternMatcherGroup pathPatternMatcherGroup;

    @BeforeMethod
    public void setUp() throws Exception {
        PathPatternMatcherGroupParser parser = PathPatternMatcherGroupParser.getInstance();

        pathPatternMatcherGroup =
            parser.parse(
                FileUtil.getFileContent(TestPathMatcherGroup.class.getResourceAsStream("/testng/test.ignore"))
            );

        ignoredEntries = new ArrayList<>();
        acceptedEntries = new ArrayList<>();

        ignoredEntries.add("abc.txt");
        ignoredEntries.add("a/abc.txt");
        ignoredEntries.add("a/b/c/abc.txt");
        ignoredEntries.add("a/A.java");
        ignoredEntries.add("a/b/c/A.java");
        ignoredEntries.add("a.ignore");
        ignoredEntries.add("a/a.ignore");
        ignoredEntries.add("b/b/a.ignore");
        ignoredEntries.add("c/b/a.ignore");
        ignoredEntries.add("a.ignoremore");
        ignoredEntries.add("a/a.ignoremore");
        ignoredEntries.add("b/b/a.ignoremore");
        ignoredEntries.add("c/b/a.ignoremore");
        ignoredEntries.add("b/d/this_is_ignored");
        ignoredEntries.add("c/d/this_is_also_ignored");
        ignoredEntries.add("attt.cpp");
        ignoredEntries.add("bttt.cpp");
        ignoredEntries.add("cttt.cpp");
        ignoredEntries.add("a/attt.cpp");
        ignoredEntries.add("a/bttt.cpp");
        ignoredEntries.add("a/cttt.cpp");
        ignoredEntries.add("abc.txt/abc.txt");
        ignoredEntries.add("abc.txt/abcd.txt");
        // This will be ignored by rule *.txt then negated by rule unignore.txt and then ignored again by rule b/
        ignoredEntries.add("b/unignore.txt");
        // Same as b/unignore.txt, the final matching rule will be c/**
        ignoredEntries.add("c/unignore.txt");

        acceptedEntries.add("a/unignore.txt");
        acceptedEntries.add("d/A.java");
        acceptedEntries.add("a/BA.java");
        acceptedEntries.add("unignore.txt");
        acceptedEntries.add("a/cttt.cppt");
        acceptedEntries.add("a/aaattt.cpp");
    }

    @Test
    public void testIgnoreMatcher() {
        // IgnoreParser should return ignore matcher
        assertNotNull(pathPatternMatcherGroup, "pathPatternMatcherGroup is null, parser failure!");

        for (String acceptedEntry : acceptedEntries) {
            assertFalse(pathPatternMatcherGroup.match(acceptedEntry), acceptedEntry + " should be accepted but not!");
        }

        for (String ignoredEntry : ignoredEntries) {
            assertTrue(pathPatternMatcherGroup.match(ignoredEntry), ignoredEntry + " should be ignored but not!");
        }
    }
}
