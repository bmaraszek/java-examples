package concurrency.examples.synchronization;

import lombok.AllArgsConstructor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Using a BlockingQueue removes the need to use the wait/notify pattern
 */
public class ProducerConsumerWithBlockingQueueExample {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(5);
        Thread t0 = new Thread(new SimpleProducer(queue));
        Thread t1 = new Thread(new SimpleProducer(queue));
        Thread t2 = new Thread(new SimpleConsumer(queue));
        t0.start();
        t1.start();
        t2.start();

        t0.join();
        t1.join();
        t2.join();

        // Now the same with a thread pool, so we control the number of threads better
        System.out.println("### NOW WITH EXECUTOR");

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.execute(new SimpleProducer(queue));
        executor.execute(new SimpleProducer(queue));
        executor.execute(new SimpleConsumer(queue));

        executor.shutdown();
    }
}

@AllArgsConstructor
class SimpleProducer implements Runnable {
    private BlockingQueue<Integer> queue;

    @Override
    public void run() {
        int value = 0;
        for(int i = 0; i < 50; i++) {
            try {
                queue.put(value);
                System.out.printf("Produced: %d; queue size: %d\n", value, queue.size());
                value++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

@AllArgsConstructor
class SimpleConsumer implements Runnable {
    private BlockingQueue<Integer> queue;

    @Override
    public void run() {
        for(int i = 0; i < 100; i++) {
            try {
                Integer value = queue.take();
                System.out.printf("Consumed: %d; queue size: %d\n", value, queue.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}