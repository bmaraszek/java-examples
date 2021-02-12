package pl.bmaraszek.concurrency.synchronization;

import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerExample {
    public static void main(String[] args) {
        EventStorage storage = new EventStorage();
        Producer p = new Producer(storage);
        Consumer c = new Consumer(storage);
        Thread t1 = new Thread(p);
        Thread t2 = new Thread(c);
        t1.start();
        t2.start();
    }
}

class EventStorage {
    private int maxSize;
    private Queue<Date> storage;

    EventStorage() {
        maxSize = 10;
        storage = new LinkedList<>();
    }

    public synchronized void set() {
        while(storage.size() == maxSize) {
            try {
                wait(); // sleep and release lock.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        storage.offer(new Date());
        System.out.printf("Set: %d\n", storage.size());
        notify(); // wake up other threads
    }

    public synchronized void get() {
        while(storage.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String element = storage.poll().toString();
        System.out.printf("Get: %d: %s\n", storage.size(), element);
        notify();
    }
}

@AllArgsConstructor
class Producer implements Runnable {
    private EventStorage storage;

    @Override
    public void run() {
        for(int i = 0; i < 100; i++) {
            storage.set();
        }
    }
}

@AllArgsConstructor
class Consumer implements Runnable {
    private EventStorage storage;

    @Override
    public void run() {
        for(int i = 0; i < 100; i++) {
            storage.get();
        }
    }
}