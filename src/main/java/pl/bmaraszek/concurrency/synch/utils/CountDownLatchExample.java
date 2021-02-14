package pl.bmaraszek.concurrency.synch.utils;

import lombok.AllArgsConstructor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A CountDownLatch is similar to join() method but has some benefits:
 * - Can be used with an Executor
 * - Can decrease the count based on condition different than thread completion
 * - You can easily wait for N out of K threads to complete e.g. minimum 2 players should join a game to proceed
 *
 * Important methods:
 * - await() blocks until the count is 0. Also await(long time, TimeUnit unit)
 * - countDown() is called by the threads when they finish their execution
 */
public class CountDownLatchExample {
    public static void main(String[] args) {
        VideoConferece conferece = new VideoConferece(10);
        Thread conferenceThread = new Thread(conferece);
        conferenceThread.start();

        for(int i = 0; i < 10; i++) {
            Participant p = new Participant(conferece, "Participant-" + i);
            Thread t = new Thread(p);
            t.start();
        }
    }
}

class VideoConferece implements Runnable {
    private final CountDownLatch controller;

    public VideoConferece(int number) {
        controller = new CountDownLatch(number);
    }

    public void arrive(String name) {
        System.out.printf("%s has arrived\n", name);
        controller.countDown();
        System.out.printf("VideoConference: Waiting for %d participants\n", controller.getCount());
    }

    @Override
    public void run() {
        System.out.printf("VideoConference: Initialization: %d participants\n", controller.getCount());
        try {
            controller.await();
            System.out.println("VideoConference: All the participants have come");
            System.out.println("VideoConference: Let's start...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

@AllArgsConstructor
class Participant implements Runnable {
    private VideoConferece videoConferece;
    private String name;

    @Override
    public void run() {
        long duration = (long)(Math.random()*10);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        videoConferece.arrive(name);
    }
}
