package concurrency.examples.thread;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class DaemonThreadExample {
    public static void main(String[] args) {
        /**
         * A deamon thread (e.g. the garbage collector) will not stop the JVM from terminating
         * Usually a deamon thread will loop while(true) and perform some maintenence tasks
         * Here, a cleaner thread removes events older than 10[s] = 10_000[ms]
         */
        Deque<Event> deque = new ConcurrentLinkedDeque<>();
        WriterTask writer = new WriterTask(deque);
        for(int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            Thread thread = new Thread(writer);
            thread.start();
        }
        CleanerTask cleaner = new CleanerTask(deque);
        cleaner.start();
    }
}

@Data
@AllArgsConstructor
class Event {
    private Date date;
    private String event;
}

@AllArgsConstructor
class WriterTask implements Runnable {
    private Deque<Event> deque;

    @Override
    public void run() {
        for(int i = 0; i < 100; i++){
            Event event = new Event(new Date(), String.format("Thread %s generated an event", Thread.currentThread().getId()));
            deque.addFirst(event);
            try{
                TimeUnit.SECONDS.sleep(1);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class CleanerTask extends Thread {
    private Deque<Event> deque;

    public CleanerTask(Deque<Event> deque) {
        this.deque = deque;
        setDaemon(true); // XXX: HERE
    }

    @Override
    public void run() {
        while(true) {
            Date date = new Date();
            clean(date);
        }
    }

    private void clean(Date date) {
        long difference;
        boolean delete;
        if(deque.size() == 0) {
            return;
        }
        delete = false;
        do {
            Event e = deque.getLast();
            difference = date.getTime() - e.getDate().getTime();
            if(difference > 10_000) {
                System.out.printf("Cleaner: %s\n", e.getEvent());
                deque.removeLast();
                delete = true;
            }
        } while(difference > 10_000);
        if(delete) {
            System.out.printf("Cleaner: Size of the queue: %d\n", deque.size());
        }
    }
}