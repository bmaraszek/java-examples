package pl.bmaraszek;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Hello world!");
        App app = new App();
        app.readFile("resources.csv");
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