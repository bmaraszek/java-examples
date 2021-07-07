package leetcode.examples;

import java.util.Date;

public class Car {
    public String reg;
    public int registrationYear;

    public Car(String reg, int registrationYear) {
        this.reg = reg;
        this.registrationYear = registrationYear;
    }

    public int getAge() {
        int currentYear = new Date().getYear();
        return getAge(currentYear);
    }

    public int getAge(int currentYear) {
        return currentYear - registrationYear;
    }
}
