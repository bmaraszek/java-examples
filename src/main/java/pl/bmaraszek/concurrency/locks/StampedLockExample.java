package pl.bmaraszek.concurrency.locks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock is *not* a reentrant lock.
 * It does not have a notion of ownership. It can be acquired by Thread A and released by Thread B.
 * - write() - no other lock can have control of the lock
 * - read() - non-exclusive access to the lock
 * - optimisticRead() - needs to be followed by a validate()
 */
public class StampedLockExample {
    public static void main(String[] args) {
        Position position = new Position(0 ,0);
        StampedLock lock = new StampedLock();

        Thread writerThread = new Thread(new WriterStamped(position, lock));
        Thread readerThread = new Thread(new ReaderStamped(position, lock));
        Thread optReaderThread = new Thread(new OptimisticReader(position, lock));

        writerThread.start();
        readerThread.start();
        optReaderThread.start();
    }
}

@Data
@AllArgsConstructor
class Position {
    private int x;
    private int y;
}

@AllArgsConstructor
class WriterStamped implements Runnable {
    private final Position position;
    private final StampedLock lock;

    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            long stamp = lock.writeLock();
            try {
                System.out.printf("Writer: Lock acquired %d\n", stamp);
                position.setX(position.getX() + 1);
                position.setY(position.getY() + 1);
                TimeUnit.SECONDS.sleep(1);
            } catch(InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlockWrite(stamp);
                System.out.printf("Writer: Lock released %d\n", stamp);
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

@AllArgsConstructor
class ReaderStamped implements Runnable {
    private final Position position;
    private final StampedLock lock;

    @SneakyThrows
    @Override
    public void run() {
        for(int i = 0; i < 50; i++) {
            long stamp = lock.readLock();
            try {
                System.out.printf("Reader: %d - position(%d, %d)\n", stamp, position.getX(), position.getY());
                TimeUnit.MILLISECONDS.sleep(200);
            } finally {
                lock.unlockRead(stamp);
                System.out.printf("Reader: %d - Lock released\n", stamp);
            }
        }
    }
}

@AllArgsConstructor
class OptimisticReader implements Runnable {
    private final Position position;
    private final StampedLock lock;

    @SneakyThrows
    @Override
    public void run() {
        long stamp;
        for(int i = 0; i < 100; i++) {
            stamp = lock.tryOptimisticRead();
            int x = position.getX();
            int y = position.getY();
            if(lock.validate(stamp)) {
                System.out.printf("OptimisticReader: %d - Position(%d, %d)\n", stamp, x, y);
            } else {
                System.out.printf("OptimisticReader: %d - Not Free\n", stamp);
            }
            TimeUnit.MILLISECONDS.sleep(200);
        }
    }
}