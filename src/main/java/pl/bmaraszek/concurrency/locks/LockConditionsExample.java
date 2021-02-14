package pl.bmaraszek.concurrency.locks;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition objects are created using lock.newCondition()
 * Before performing any operation on a condition, you need to have the associated lock
 * When a thread calls aCondition.await() it frees the lock
 * When a thread calls aCondition.signal() or aCondition.signalAll() it wakes up thread(s) waiting for that condition
 * This does not guarante that the condition is noew true, so you must await() inside a loop
 * You cannot leave this loop until the condition is true. When the condition is false, you must await() again
 *
 * The await() method is overloaded:
 * - await(long time, TimeUnit unit)
 * - awaitUninterruptibly() - awaits until singalled, cannot be interrupted
 * - awaitUntil(Date date)
 */
public class LockConditionsExample {
    public static void main(String[] args) {
        FileMock mock = new FileMock(100, 10);
        Buffer buffer = new Buffer(20);
        Thread producerThread = new Thread(new Producer(mock, buffer), "Prod-1");
        Thread[] consumers = new Thread[3];
        for(int i = 0; i < 3; i++) {
            consumers[i] = new Thread(new Consumer(buffer), "Cons-" + i);
        }

        producerThread.start();
        Arrays.stream(consumers).forEach(t -> t.start());
    }
}

@Getter
class FileMock {
    private String[] content;
    private int index;

    public FileMock(int size, int length) {
        content = new String[size];
        for(int i = 0; i < size; i++) {
            Random random = new Random(new Date().getTime());
            String randomString = random.ints(97, 122 + 1)
                    .limit(length)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            content[i] = randomString;
        }
        index = 0;
    }

    public boolean hasMoreLines() {
        return index < content.length;
    }

    public String getLine() {
        if(hasMoreLines()) {
            System.out.printf("Mock: %d\n", content.length-index);
            return content[index++];
        }
        return null;
    }
}

class Buffer {
    private final LinkedList<String> buffer;
    private final int maxSize;
    private final ReentrantLock lock;
    private final Condition lines;
    private final Condition space;
    private boolean pendingLines;

    public Buffer(int maxSize) {
        this.maxSize = maxSize;
        buffer = new LinkedList<>();
        lock = new ReentrantLock();
        lines = lock.newCondition();
        space = lock.newCondition();
        pendingLines = true;
    }

    public void insert(String line) {
        lock.lock();
        try {
            while(buffer.size() == maxSize) {
                space.await();
            }
            buffer.offer(line);
            System.out.printf("%s: Inserted Line. Current buffer size: %d\n", Thread.currentThread().getName(), buffer.size());
            lines.signalAll();
        } catch(InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public String get() {
        String line = null;
        lock.lock();
        try {
            while((buffer.size() == 0) && (hasPendingLines())) {
                lines.await();
            }
            if(hasPendingLines()) {
              line = buffer.poll();
              System.out.printf("%s: Lines Read. Current buffer size: %d\n", Thread.currentThread().getName(), buffer.size());
              space.signalAll();
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return line;
    }

    public synchronized void setPendingLines(boolean pendingLines) {
        this.pendingLines = pendingLines;
    }

    public synchronized boolean hasPendingLines() {
        return pendingLines || buffer.size() > 0;
    }
}

@AllArgsConstructor
class Producer implements Runnable {
    private FileMock mock;
    private Buffer buffer;
    @Override
    public void run() {
        buffer.setPendingLines(true);
        while(mock.hasMoreLines()) {
            String line = mock.getLine();
            buffer.insert(line);
        }
        buffer.setPendingLines(false);
    }
}

@AllArgsConstructor
class Consumer implements Runnable {
    private Buffer buffer;
    @Override
    public void run() {
        while(buffer.hasPendingLines()) {
            String line = buffer.get();
            processLine(line);
        }
    }

    private void processLine(String line) {
        try {
            System.out.printf("--- processing line: %s\n", line);
            TimeUnit.SECONDS.sleep(1);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
