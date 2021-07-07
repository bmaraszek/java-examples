package concurrency.examples.exchange.data;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * The call to exchanger.exchange(buffer) swaps the producer and consumer buffers.
 * Producer ends up with an empty buffer, and consumer ends up with a buffer with 10 elements.
 * The thread that calls exchange() first is put to sleep until the other thread arrives.
 *
 * Exchange also has a version with timeout: exchange(V data, long time, TimeUnit unit)
 */
public class ExchangerExample {
    public static void main(String[] args) {
        List<String> buffer1 = new ArrayList<>();
        List<String> buffer2 = new ArrayList<>();
        Exchanger<List<String>> exchanger = new Exchanger<>();

        Producer producer = new Producer(buffer1, exchanger);
        Consumer consumer = new Consumer(buffer2, exchanger);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();
    }
}

@AllArgsConstructor
class Producer implements Runnable {
    private List<String> buffer;
    private final Exchanger<List<String>> exchanger;

    @Override
    public void run() {
        for(int cycle = 1; cycle <= 10; cycle++) {
            System.out.printf("Producer: Cycle %d\n", cycle);
            for(int j = 0; j < 10; j++) {
                String message = "Event " + (((cycle-1)*10)+j);
                System.out.printf("Producer: %s\n", message);
                buffer.add(message);
            }
            try {
                buffer = exchanger.exchange(buffer);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Producer buffer size: %d\n", buffer.size());
        }
    }
}

@AllArgsConstructor
class Consumer implements Runnable {
    private List<String> buffer;
    private final Exchanger<List<String>> exchanger;

    @Override
    public void run() {
        for(int cycle = 1; cycle <= 10; cycle++) {
            System.out.printf("Consumer: Cycle %d\n", cycle);
            try {
                buffer = exchanger.exchange(buffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Consumer buffer size: %d\n", buffer.size());
            for(int j = 0; j < 10; j++) {
                String message = buffer.get(0);
                System.out.printf("Consumer: %s\n", message);
                buffer.remove(0);
            }
        }
    }
}