package neetcode.examples.arraysAndHashing;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  Given a list of Strings input, encode it into a single string.                                                    //
//  Implement a decode method to decode the string back into a list of strings.                                       //
//                                                                                                                    //
//  We cannot simpy use an arbitrary separator like '#' because the encoded strings can contain the separator symbol. //
//  Instead, we'll put the character number followed by a single separator symbol.                                    //
//                                                                                                                    //
//  ["s1", "s2"] -> "2#s12#s2"                                                                                        //
//                                                                                                                    //
//  Time complexity is O(N) - we loop through the input list once                                                     //
//  Space complexity is O(N) - wa allocate a single long string                                                       //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class EncodeDecodeString {

    public static String encode(List<String> input) {
        StringBuilder encodedString = new StringBuilder();
        for (String s : input) {
            encodedString
                    .append(s.length())
                    .append("#")
                    .append(s);
        }
        return encodedString.toString();
    }

    public static List<String> decode(String input) {
        List<String> result = new ArrayList<>();
        int i = 0;
        while(i < input.length()) {
            int j = i;
            while(input.charAt(j) != '#') j++;

            int length = Integer.valueOf(input.substring(i, j));
            i = j + 1 + length;
            result.add(input.substring(j + 1, i));
        }
        return result;
    }

    @Test
    public void test() {
        List<String> input1 = List.of("Luke", "Leia", "Han", "Chewbacca");
        List<String> input2 = List.of("#R2D2", "#C3PO", "#BB8");

        assertThat(decode(encode(input1))).containsExactly("Luke", "Leia", "Han", "Chewbacca");
        assertThat(decode(encode(input2))).containsExactly("#R2D2", "#C3PO", "#BB8");
    }
}
