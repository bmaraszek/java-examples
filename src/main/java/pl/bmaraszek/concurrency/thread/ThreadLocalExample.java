package pl.bmaraszek.concurrency.thread;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ThreadLocalExample {
    public static void main(String[] args) {
        /**
         * This prints somthing like:
         * Starting Thread: Thread-0 : Fri Feb 12 12:48:19 GMT 2021
         * Starting Thread: Thread-1 : Fri Feb 12 12:48:21 GMT 2021 <-- when this thread started, it changed the date
         * Thread Finished: Thread-0 : Fri Feb 12 12:48:21 GMT 2021 <-- there was a change in the attribute!
         */
        System.out.println("### UNSAFE TASK ###");
        UnsafeTask unsafeTask = new UnsafeTask();
        for(int i = 0; i < 10; i++) {
            Thread thread = new Thread(unsafeTask, "Thread-" + i);
            thread.start();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * This fixes the issue by using ThreadLocal
         */
        System.out.println("### SAFE TASK ###");
        SafeTask safeTask = new SafeTask();
        for(int i = 0; i < 10; i++) {
            Thread thread = new Thread(safeTask, "Thread-" + i);
            thread.start();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class UnsafeTask implements Runnable {
    private Date startDate;

    @Override
    public void run() {
        startDate = new Date();
        System.out.printf("Starting Thread: %s : %s\n", Thread.currentThread().getName(), startDate);
        try {
            TimeUnit.SECONDS.sleep( (int)Math.rint(Math.random() * 10) );
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Thread Finished: %s : %s\n", Thread.currentThread().getName(), startDate);
    }
}

class SafeTask implements Runnable {
    /**
     * InheritableThreadLocal provides inheritance of values for threads created from a thread.
     * If thread A has a value in a thread-local variable and it spawns thread B, then B will have the same value as A.
     * You can override the childValue() method that is called to init the value for the child thread.
     * By default, it receives the value of the parent thread as a parameter.
     */
    private static ThreadLocal<Date> startDate = new ThreadLocal<Date>() {
        @Override
        protected Date initialValue() {
            return new Date();
        }
    };

    @Override
    public void run() {
        System.out.printf("Starting Thread: %s : %s\n", Thread.currentThread().getName(), startDate.get());
        try {
            TimeUnit.SECONDS.sleep( (int)Math.rint(Math.random() * 10) );
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Thread Finished: %s : %s\n", Thread.currentThread().getName(), startDate.get());
    }
}