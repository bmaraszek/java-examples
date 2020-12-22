package fragment.submissions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class FragmentTest {

    @Test
    public void testCalculateOverlapZeroOverlap() {
        Assertions.assertEquals(0, BartMaraszek.getLongestOverlapLengthBetween("abcd", "efgh"));
    }

    @Test
    public void testCalculateOverlapFrontOverlap() {
        Assertions.assertEquals(2, BartMaraszek.getLongestOverlapLengthBetween("abcd", "yzab"));
    }

    @Test
    public void testCalculateOverlapBackOverlap() {
        Assertions.assertEquals(3, BartMaraszek.getLongestOverlapLengthBetween("abcd", "bcdef"));
    }

    @Test
    public void testCalculateOverlapContainsOverlap() {
        Assertions.assertEquals(2, BartMaraszek.getLongestOverlapLengthBetween("abcd", "bc"));
    }

    @Test
    public void testCalculateOverlapIsContainedOverlap() {
        Assertions.assertEquals(4, BartMaraszek.getLongestOverlapLengthBetween("abcd", "yzabcdef"));
    }

    @Test
    public void testMergeNoOverlap() {
        Assertions.assertEquals("abcdefgh", BartMaraszek.merge("abcd", "efgh", 0));
    }

    @Test
    public void mergeFrontOverlap() {
        Assertions.assertEquals("yzabcd", BartMaraszek.merge("abcd", "yzab", 2));
    }

    @Test
    public void mergeBackOverlap() {
        Assertions.assertEquals("abcdef", BartMaraszek.merge("abcd", "bcdef", 3));
    }

    @Test
    public void mergeContainsOverlap() {
        Assertions.assertEquals("abcd", BartMaraszek.merge("abcd", "bc", 2));
    }

    @Test
    public void mergeIsContainedOverlap() {
        Assertions.assertEquals("yzabcdef", BartMaraszek.merge("abcd", "yzabcdef", 4));
    }

    @Test
    public void testShortString() throws Exception{
        List<String> result;
        try (BufferedReader in = new BufferedReader(new FileReader("/home/bartek/dev/demo/src/main/resources/fragment01"))) {
            result = in.lines()
                    .map(BartMaraszek::reassemble)
                    .collect(Collectors.toList());
        }
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("O draconian devil! Oh lame saint!", result.get(0));
    }

    @Test
    public void testLongString() throws Exception{
        List<String> result;
        try (BufferedReader in = new BufferedReader(new FileReader("/home/bartek/dev/demo/src/main/resources/fragment02"))) {
            result = in.lines()
                    .map(BartMaraszek::reassemble)
                    .collect(Collectors.toList());
        }
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed " +
                "quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat " +
                "voluptatem.", result.get(0));
    }

    @Test
    public void testMultipleStrings() throws Exception{
        BartMaraszek.main(new String[] {"/home/bartek/dev/demo/src/main/resources/fragment04"});
    }

}
