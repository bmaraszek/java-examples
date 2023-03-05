package neetcode.examples.stack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.Comparator.comparingDouble;
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
//  Solution: First, we're going to merge the two lists into a list of pairs (position, speed) and sort it            //
//  by position. Then, we're going to go from right to left (highest to lowest position and for each car, calculate   //
//  the time it arrives at the destination. If a car arrives at the destination faster than the car before it,        //
//  it means that the cars must collide. We can remove the car that has collided from consideration.                  //
//                                                                                                                    //
//  Time complexity is O(n log n) because we are going to sort the cars by position                                   //
//  Space complexity is O(n) because we'll create a separate List and a Stack                                         //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CarFleet {

    @Getter
    @AllArgsConstructor
    public static class Car {
        float position;
        float speed;
    }

    public static int carFleet(int target, List<Integer> positions, List<Integer> speed) {
        if(positions.size() == 1) return 1;

        // combine two lists into one list of cars
        List<Car> cars = new ArrayList<>(positions.size());
        for(int i = 0; i < positions.size(); i ++) {
            cars.add(new Car(positions.get(i), speed.get(i)));
        }
        Collections.sort(cars, comparingDouble(Car::getPosition));

        // calculate the arrival times
        Stack<Float> stack = new Stack<>();
        for(int i = cars.size() - 1; i >= 0; i--) {
            float currentTime = (target - cars.get(i).position) / cars.get(i).speed;
            if(!stack.isEmpty() && currentTime <= stack.peek()) {
                // NOP
            } else {
                stack.push(currentTime);
            }
        }
        return stack.size();
    }

    @Test
    public void test() {
        assertThat(carFleet(12, List.of(10, 8, 0, 5, 3), List.of(2, 4, 1, 1, 3))).isEqualTo(3);
        assertThat(carFleet(30, List.of(3), List.of(3))).isEqualTo(1);
        assertThat(carFleet(100, List.of(0, 2, 4), List.of(4, 2, 1))).isEqualTo(1);
    }
}
