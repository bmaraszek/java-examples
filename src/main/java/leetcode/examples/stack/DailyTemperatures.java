package leetcode.examples.stack;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Stack;
import org.junit.jupiter.api.Test;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  TODO: Review this                                                                                                 //
//  Given an array of integers 'temperatures' representing daily temperatures, return an array 'answer' such that     //
//  answer[i] is the number of days you have to wait after the i-th day to get a warmer temperature.                  //
//  If there is no future day for which this is possile, keep answer[i] = 0 instead.                                  //
//                                                                                                                    //
//  Input: [73, 74, 75, 71, 69, 72, 76, 73] --> Output: [1, 1, 4, 2, 1, 1, 0, 0]                                      //
//  Input: [30, 40, 50, 60] --> Output: [1, 1, 1, 0]                                                                  //
//                                                                                                                    //
//  Time complexity is O(n) - we do a single pass through the array                                                   //
//  Space complexity is O(n) - we allocate the answer array of length equal to the length of the input                //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DailyTemperatures {

  public static int[] calculate(int[] temperatures) {
    int[] result = new int[temperatures.length];
    Stack<Integer> stack = new Stack<>();

    for(int currDay = 0; currDay < temperatures.length; currDay++) {
      while(!stack.isEmpty() && temperatures[currDay] > temperatures[stack.peek()]) {
        int prevDay = stack.pop();
        result[prevDay] = currDay - prevDay;
      }
      stack.add(currDay);
    }
    return result;
  }

  @Test
  public void test() {
    assertThat(calculate(new int[]{73, 74, 75, 71, 69, 72, 76, 73})).containsExactly(1, 1, 4, 2, 1, 1, 0, 0);
    assertThat(calculate(new int[]{30, 40, 50, 60})).containsExactly(1, 1, 1, 0);
  }
}
