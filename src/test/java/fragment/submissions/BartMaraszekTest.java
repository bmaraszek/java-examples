package fragment.submissions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BartMaraszekTest {

    @Test
    @DisplayName("Should calculate overlap length when either string is empty")
    public void shouldCalculateOverlapEmptyString() {
        assertEquals(0, BartMaraszek.getLongestOverlapLengthBetween("", ""));
        assertEquals(0, BartMaraszek.getLongestOverlapLengthBetween("abcd", ""));
        assertEquals(0, BartMaraszek.getLongestOverlapLengthBetween("", "abcd"));
    }

    @Test
    @DisplayName("Should calculate overlap length when strings do not overlap")
    public void shouldCalculateOverlapZeroOverlap() {
        assertEquals(0, BartMaraszek.getLongestOverlapLengthBetween("abcd", "efgh"));
    }

    @Test
    @DisplayName("Should calculate overlap length when strings overlap at the start")
    public void shouldCalculateOverlapFrontOverlap() {
        assertEquals(2, BartMaraszek.getLongestOverlapLengthBetween("abcd", "yzab"));
    }

    @Test
    @DisplayName("Should calculate overlap length when strings overlap at the end")
    public void shouldCalculateOverlapBackOverlap() {
        assertEquals(3, BartMaraszek.getLongestOverlapLengthBetween("abcd", "bcdef"));
    }

    @Test
    @DisplayName("Should calculate overlap length when string 1 contains string 2")
    public void shouldCalculateOverlapContainsOverlap() {
        assertEquals(2, BartMaraszek.getLongestOverlapLengthBetween("abcd", "bc"));
    }

    @Test
    @DisplayName("Should calculate overlap length when string 2 contains string 1")
    public void shouldCalculateOverlapIsContainedOverlap() {
        assertEquals(4, BartMaraszek.getLongestOverlapLengthBetween("abcd", "yzabcdef"));
    }

    @Test
    @DisplayName("Should merge strings when either string is empty")
    public void shouldMergeEmptyStrings() {
        assertEquals("", BartMaraszek.merge("", "", 0));
        assertEquals("abcd", BartMaraszek.merge("abcd", "", 0));
        assertEquals("abcd", BartMaraszek.merge("", "abcd", 0));
    }

    @Test
    @DisplayName("Should merge strings that do not overlap")
    public void shouldMergeNoOverlap() {
        assertEquals("abcdefgh", BartMaraszek.merge("abcd", "efgh", 0));
    }

    @Test
    @DisplayName("Should merge strings that overlap at the start")
    public void shouldMergeFrontOverlap() {
        assertEquals("yzabcd", BartMaraszek.merge("abcd", "yzab", 2));
    }

    @Test
    @DisplayName("Should merge strings that overlap at the end")
    public void shouldMergeBackOverlap() {
        assertEquals("abcdef", BartMaraszek.merge("abcd", "bcdef", 3));
    }

    @Test
    @DisplayName("Should merge strings when string 1 contains string 2")
    public void shouldMergeContainsOverlap() {
        assertEquals("abcd", BartMaraszek.merge("abcd", "bc", 2));
    }

    @Test
    @DisplayName("Should merge strings when string 2 contains string 1")
    public void shouldMergeIsContainedOverlap() {
        assertEquals("yzabcdef", BartMaraszek.merge("abcd", "yzabcdef", 4));
    }

    @Test
    @DisplayName("Should reassemble text fragments")
    public void shouldReassembleTextFragments() {
        Map<String, String> assemblyMap = new HashMap<>();
        assemblyMap.put("", "");
        assemblyMap.put("aaaa", "aaaa");
        assemblyMap.put("O draconia;conian devil! Oh la;h lame sa;saint!", "O draconian devil! Oh lame saint!");
        assemblyMap.put(
                "m quaerat voluptatem.;pora incidunt ut labore et d;, consectetur, adipisci velit;olore magnam aliqua;idunt ut labore et dolore magn;uptatem.;i dolorem ipsum qu;iquam quaerat vol;psum quia dolor sit amet, consectetur, a;ia dolor sit amet, conse;squam est, qui do;Neque porro quisquam est, qu;aerat voluptatem.;m eius modi tem;Neque porro qui;, sed quia non numquam ei;lorem ipsum quia dolor sit amet;ctetur, adipisci velit, sed quia non numq;unt ut labore et dolore magnam aliquam qu;dipisci velit, sed quia non numqua;us modi tempora incid;Neque porro quisquam est, qui dolorem i;uam eius modi tem;pora inc;am al",
                "Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem."
        );
        assemblyMap.put("abcdef;abcdef;fghab;habk", "abcdefghabk");
        assemblyMap.put("repeat, now;now let's repeat; repeat now!", "repeat, now let's repeat now!");
        assemblyMap.put("jkabcdefh;xxefhi;efhijk", "xxefhijkabcdefh");

        for(String input : assemblyMap.keySet()) {
            assertEquals(assemblyMap.get(input), BartMaraszek.reassemble(input));
        }
    }

}
