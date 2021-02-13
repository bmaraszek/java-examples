package pl.bmaraszek.concurrency.locks;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Obtaining the Write lock blocks other threads from getting Read Lock
 */
public class ReadWriteLockExample {
    public static void main(String[] args) {
        PricesInfo pricesInfo = new PricesInfo();
        Thread readerThreads[] = new Thread[5];

        for(int i = 0; i < 5; i++) {
            readerThreads[i] = new Thread(new Reader(pricesInfo));
        }

        Thread writerThread = new Thread(new Writer(pricesInfo));

        Arrays.stream(readerThreads).forEach(t -> t.start());
        writerThread.start();
    }
}

class PricesInfo {
    private ReadWriteLock lock;
    private double price1;
    private double price2;

    public PricesInfo() {
        price1 = 1.0;
        price2 = 2.0;
        lock = new ReentrantReadWriteLock();
    }

    public double getPrice1() {
        lock.readLock().lock();
        double value = price1;
        lock.readLock().unlock();
        return value;
    }

    public double getPrice2() {
        lock.readLock().lock();
        double value = price2;
        lock.readLock().unlock();
        return value;
    }

    public void setPrices(double price1, double price2) {
        lock.writeLock().lock();
        System.out.printf("%s: PricesInfo: Write Lock Acquired\n", new Date());
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        this.price1 = price1;
        this.price2 = price2;
        System.out.printf("%s: PricesInfo: Write Lock Released\n", new Date());
        lock.writeLock().unlock();
    }
}

@AllArgsConstructor
class Reader implements Runnable {
    private PricesInfo pricesInfo;

    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            System.out.printf("%s: %s: Price 1: %f, Price 2: %f\n", new Date(), Thread.currentThread().getName(), pricesInfo.getPrice1(), pricesInfo.getPrice2());
        }
    }
}

@AllArgsConstructor
class Writer implements Runnable {
    private PricesInfo pricesInfo;

    @Override
    public void run() {
        for(int i = 0; i < 3; i++) {
            System.out.printf("%s: Writer: Attempt to modify prices\n", new Date());
            pricesInfo.setPrices(Math.random()*10, Math.random()*8);
            System.out.printf("%s: Writer: Prices have been modified\n", new Date());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
