package pl.bmaraszek.concurrency.thread;

import java.util.Random;

public class ThreadGroupExample {
    public static void main(String[] args) {
        int numberOfThreads = 2 * Runtime.getRuntime().availableProcessors();
        MyThreadGroup threadGroup = new MyThreadGroup("My Thread Group");
        MyTask task = new MyTask();

        for(int i = 0; i < numberOfThreads; i++) {
            Thread t = new Thread(threadGroup, task);
            t.start();
        }

        System.out.printf("Number of threads: %d\n", threadGroup.activeCount());
        System.out.println("Information about the ThreadGroup:");
        threadGroup.list();

        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);
        for(int i = 0; i < threadGroup.activeCount(); i++) {
            System.out.printf("Thread %s: %s\n", threads[i].getName(), threads[i].getState());
        }
    }
}

class MyThreadGroup extends ThreadGroup {
    public MyThreadGroup(String name) {
        super(name);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.printf("The thread %s has thrown an exception\n", t.getId());
        e.printStackTrace();
        System.out.printf("Terminating the rest of the threads\n");
        interrupt(); // interrupt all threads in this group
    }
}

class MyTask implements Runnable {
    @Override
    public void run() {
        int result;
        Random random = new Random(Thread.currentThread().getId());
        while(true) {
            result = 1000/(int)(random.nextDouble()*1_000_000_000);
            if(Thread.currentThread().isInterrupted()) {
                System.out.printf("%d: Interrupted\n", Thread.currentThread().getId());
                return;
            }
        }
    }
}