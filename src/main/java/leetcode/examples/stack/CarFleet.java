package leetcode.examples.stack;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  There are n cars going to the same destination along a one-lane road. The destination is target miles away.       //
//                                                                                                                    //
//  You are given two integer array position and speed, both of length n, where position[i] is the position           //
//  of the ith car and speed[i] is the speed of the ith car (in miles per hour).                                      //
//                                                                                                                    //
//  A car can never pass another car ahead of it, but it can catch up to it and drive together r at the same speed.   //
//  The faster car will slow down to match the slower car's speed. The distance between these two cars is ignored     //
//  (i.e., they are assumed to have the same position).                                                               //
//                                                                                                                    //
//  A car fleet is some non-empty set of cars driving at the same position and same speed.                            //
//  Note that a single car is also a car fleet. If a car catches up to a car fleet right at the destination point,    //
//  it will still be considered as one car fleet.                                                                     //
//                                                                                                                    //
//  Return the number of car fleets that will arrive at the destination.                                              //
//                                                                                                                    //
//  Time complexity is O(n) because we go through the input once                                                      //
//  Space complexity is O(n) because we put values on the stack                                                       //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CarFleet {

    public static int carFleet(int target, List<Integer> positions, List<Integer> speed) {
        return 0;
    }

    @Test
    public void test() {
        assertThat(carFleet(12, List.of(10, 8, 0, 5, 3), List.of(2, 4, 1, 1, 3))).isEqualTo(3);
        assertThat(carFleet(30, List.of(3), List.of(3))).isEqualTo(1);
        assertThat(carFleet(100, List.of(0, 2, 4), List.of(4, 2, 1))).isEqualTo(1);
    }
}
