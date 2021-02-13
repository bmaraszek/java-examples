package pl.bmaraszek.concurrency.locks;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockExample {

    private final static int NUMBER_OF_THREADS = 5;

    public static void main(String[] args) {
        /**
         * This will print Thread-0, Thread-0, Thread-1, Thread-1, ...
         */
        System.out.printf("### Running example with fair mode = false\n");
        testPrintQueue(false);

        /**
         * This will print Thread-0, Thread-1, Thread-2, ..., Thread-0, Thread-1, ...
         */
        System.out.printf("### Running example with fair mode = true\n");
        testPrintQueue(true);
    }

    private static void testPrintQueue(boolean fairMode) {
        PrintQueue printQueue = new PrintQueue(fairMode);
        Thread[] threads = new Thread[NUMBER_OF_THREADS];
        for(int i = 0; i < NUMBER_OF_THREADS; i++) {
            threads[i] = new Thread(new Job(printQueue), "Thread-" + i);
        }

        Arrays.stream(threads).forEach(t -> t.start());
        Arrays.stream(threads).forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}

@AllArgsConstructor
class Job implements Runnable {
    private PrintQueue printQueue;

    @Override
    public void run() {
        System.out.printf("%s: going to pring a document\n", Thread.currentThread().getName());
        printQueue.printJob(new Object());
        System.out.printf("%s: document has been printed\n", Thread.currentThread().getName());
    }
}

class PrintQueue {
    private Lock queueLock;

    public PrintQueue(boolean fairMode) {
        queueLock = new ReentrantLock(fairMode);
    }

    public void printJob(Object document) {
        queueLock.lock(); // also tryLock(): boolean
        try {
            simulatePrint();
        } finally {
            queueLock.unlock();
        }
        // let's have two critical sections
        // this will show the difference between the fair mode and not fair mode
        queueLock.lock();
        try {
            simulatePrint();
        } finally {
            queueLock.unlock();
        }
    }

    private void simulatePrint() {
        Long duration = (long)(Math.random()*10_000);
        System.out.printf("%s printinig a document in %d seconds\n", Thread.currentThread().getName(), duration/1_000);
        try {
            TimeUnit.MILLISECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
