package concurrency.examples.synch.utils;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * In this example we extend Phaser to implement custom onAdvance() method.
 * This method is called by phaser *before* making a phase change and waking up all the threads sleeping with arriveAndAwaitAdvance()
 * The method is invoked by the last thread that finishes a phase as a part of the code of the arriveAndAwaitAdvance()
 * The method receives the number of the actual phase as param (0-based) and the number of participants.
 * onAdvance() returns a boolean indicating if the Phaser has terminated or not.
 */
public class PhaserOnAdvanceExample {
    public static void main(String[] args) {
        Phaser phaser = new MyPhaser();
        Student[] students = new Student[5];
        for(int i = 0; i < students.length; i++) {
            students[i] = new Student(phaser);
            phaser.register();
        }

        Thread[] threads = new Thread[students.length];
        for(int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(students[i], "Student-" + i);
            threads[i].start();
        }

        Arrays.stream(threads).forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.printf("Main: The phaser has finished: %s\n", phaser.isTerminated());
    }
}

class MyPhaser extends Phaser {
    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        switch(phase) {
            case 0:
                return studentsArrived();
            case 1:
                return finishFirstExercise();
            case 2:
                return finishSecondExercise();
            case 3:
                return finishExam();
            default:
                return true;
        }
    }

    private boolean finishExam() {
        System.out.printf("Phaser: All students have finished the exam\n");
        System.out.printf("    Phaser: Thank you for your time\n");
        return true;
    }

    private boolean finishSecondExercise() {
        System.out.printf("Phaser: All students have finished the 2nd exercise\n");
        System.out.printf("    Phaser: It's time for the 3rd exercise\n");
        return false;
    }

    private boolean finishFirstExercise() {
        System.out.printf("Phaser: All students have finished the 1st exercise\n");
        System.out.printf("    Phaser: It's time for the 2nd exercise\n");
        return false;
    }

    private boolean studentsArrived() {
        System.out.printf("Phaser: The exam is going to start. The students are ready\n");
        System.out.printf("    Phaser: We have %d students\n", getRegisteredParties());
        return false;
    }
}

@AllArgsConstructor
class Student implements Runnable {
    private Phaser phaser;

    @Override
    public void run() {
        System.out.printf("%s: has arrived to do the exam. %s\n", Thread.currentThread().getName(), new Date());
        phaser.arriveAndAwaitAdvance();

        System.out.printf("%s: is going to do the 1st exercise. %s\n", Thread.currentThread().getName(), new Date());
        doExercise1();
        System.out.printf("%s:has finished the 1st exercise. %s\n", Thread.currentThread().getName(), new Date());
        phaser.arriveAndAwaitAdvance();

        System.out.printf("%s: is going to do the 2nd exercise. %s\n", Thread.currentThread().getName(), new Date());
        doExercise2();
        System.out.printf("%s:has finished the 2nd exercise. %s\n", Thread.currentThread().getName(), new Date());
        phaser.arriveAndAwaitAdvance();

        System.out.printf("%s: is going to do the 3rd exercise. %s\n", Thread.currentThread().getName(), new Date());
        doExercise3();
        System.out.printf("%s:has finished the 3rd exercise. %s\n", Thread.currentThread().getName(), new Date());
        phaser.arriveAndAwaitAdvance();
    }

    @SneakyThrows
    private void doExercise1() {
        TimeUnit.SECONDS.sleep((long)Math.random()*10);
    }

    @SneakyThrows
    private void doExercise2() {
        TimeUnit.SECONDS.sleep((long)Math.random()*10);
    }

    @SneakyThrows
    private void doExercise3() {
        TimeUnit.SECONDS.sleep((long)Math.random()*10);
    }
}
