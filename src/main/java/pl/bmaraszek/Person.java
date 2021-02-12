package pl.bmaraszek;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public final class Person {
    private String name;
    private Date dateOfBirth;
    private List<Person> children;

    public Person(String name, Date dateOfBirth, List<Person> children) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public List<Person> getChildren() {
        return new ArrayList<>(children);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && Objects.equals(dateOfBirth, person.dateOfBirth) && Objects.equals(children, person.children);
    }

    @Override
    public int hashCode() {
        return 1; //Objects.hash(name, dateOfBirth, children);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", children=" + children +
                '}';
    }
}
