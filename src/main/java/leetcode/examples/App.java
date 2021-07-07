package leetcode.examples;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Hello world!");
        App app = new App();
        app.readFile("resources.csv");

        LinkedList<String> list = new LinkedList<>();
        list.addFirst("a");
        list.addFirst("b");
        list.addLast("y");
        list.addLast("z");

        System.out.println(list.getFirst());
        System.out.println(list.getLast());
    }

    public void readFile(String fileName){
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(fileName).getFile());
        try(Scanner s = new Scanner(file)) {
            while(s.hasNextLine()) {
                System.out.println(s.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}