package pl.bmaraszek.concurrency.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ThreadFactoryExample {
    public static void main(String[] args) {
        MyThreadFactory threadFactory = new MyThreadFactory("My thread factory");
        SimpleTask task = new SimpleTask();

        Thread thread;
        System.out.println("Starting the threads");

        for(int i = 0; i < 10; i++) {
            thread = threadFactory.newThread(task);
            thread.start();
        }

        System.out.println("Factory stats:");
        System.out.printf("%s\n", threadFactory.getStats());
    }
}

/**
 * A ThreadFactory can limit the number of threads (pool),
 * add custom names, validation, and stats.
 * It can also return a custom implementation of a Thread class.
 */
class MyThreadFactory implements ThreadFactory {

    private int counter;
    private String name;
    private List<String> stats;

    public MyThreadFactory(String name) {
        counter = 0;
        this.name = name;
        stats = new ArrayList<>();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, name + "Thread_" + counter);
        counter++;
        stats.add(String.format("Created thread %d with name %s on %s\n", t.getId(), t.getName(), new Date()));
        return t;
    }

    public String getStats() {
        StringBuffer buffer = new StringBuffer();
        Iterator<String> it = stats.iterator();
        while(it.hasNext()) {
            buffer.append(it.next());
            buffer.append("\n");
        }
        return buffer.toString();
    }
}

class SimpleTask implements Runnable {
    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}