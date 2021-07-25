package fragment.submissions;

import leetcode.examples.Car;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CarTest {

    @Test
    public void shouldCalcualteAge() {
        Car car = new Car("ab12", 2018);
        int result = car.getAge(2021);
        Assertions.assertEquals(3, result);
    }
}
