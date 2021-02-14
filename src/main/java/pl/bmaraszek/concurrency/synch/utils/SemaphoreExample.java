package pl.bmaraszek.concurrency.synch.utils;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Also:
 * - acquireUniterruptibly()
 * - tryAcquire(): boolean
 * - tryAcquire(long timout, TimeUnit unit): boolean
 * All this methods also have a version with and int param specifying the number of permits to acquire.
 * Semaphores can also use the fair mode - select the longest waiting thread to acquire the permit.
 */
public class SemaphoreExample {
    public static void main(String[] args) {
        PrintQueue printQueue = new PrintQueue();
        Thread[] threads = new Thread[12];
        for(int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Job(printQueue), "Thread-" + i);
        }
        Arrays.stream(threads).forEach(t -> t.start());
    }
}

class PrintQueue {
    private final Semaphore semaphore;
    private final boolean freePrinters[];
    private final Lock lockPrinters;

    public PrintQueue() {
        semaphore = new Semaphore(3);
        freePrinters = new boolean[] {true, true, true};
        lockPrinters = new ReentrantLock();
    }

    public void printJob(Object document) {
        try {
            semaphore.acquire();
            int assignedPriter = getPrinter();
            long duration = (long)(Math.random()*10);
            System.out.printf("%s - %s: PrintQueue: Printing a job in printer %d in %d seconds\n",
                    new Date(), Thread.currentThread().getName(), assignedPriter, duration);
            TimeUnit.SECONDS.sleep(duration);
            freePrinters[assignedPriter] = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    private int getPrinter() {
        int ret = -1;
        try {
            lockPrinters.lock();
            for(int i = 0; i < freePrinters.length; i++) {
                if(freePrinters[i]) {
                    ret = i;
                    freePrinters[i] = false;
                    break;
                }
            }
        } finally {
            lockPrinters.unlock();
        }
        return ret;
    }
}

@AllArgsConstructor
class Job implements Runnable {
    private PrintQueue printQueue;

    @Override
    public void run() {
        System.out.printf("%s: Going to print a job\n", Thread.currentThread().getName());
        printQueue.printJob(new Object());
        System.out.printf("%s: The document has ben printed\n", Thread.currentThread().getName());
    }
}