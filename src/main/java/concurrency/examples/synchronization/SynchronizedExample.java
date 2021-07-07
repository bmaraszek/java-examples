package concurrency.examples.synchronization;

import lombok.AllArgsConstructor;

import java.util.concurrent.TimeUnit;

public class SynchronizedExample {
    public static void main(String[] args) {
        ParkingCash cash = new ParkingCash();
        ParkingStats stats = new ParkingStats(cash);
        System.out.println("Parking simulation");

        int numberOfSensors = 8;
        Thread[] threads = new Thread[numberOfSensors];
        for(int i = 0; i < numberOfSensors; i++) {
            Sensor sensor = new Sensor(stats);
            Thread thread = new Thread(sensor);
            thread.start();
            threads[i] = thread;
        }

        for(int i = 0; i < numberOfSensors; i++) {
            try {
                threads[i].join();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("Number of cars: %d\n", stats.getNumberCars());
        System.out.printf("Number of motorcycles: %d\n", stats.getNumberMotorcycles());
        cash.close();
    }
}

class ParkingCash {
    private static final int cost = 2;
    private long cash;

    public ParkingCash() {
        cash = 0;
    }

    public synchronized void vehiclePay() {
        cash += cost;
    }

    public void close() {
        System.out.printf("Closing accounting:\n");
        long totalAmount;
        synchronized (this) {
            totalAmount = cash;
            cash = 0;
        }
        System.out.printf("The total amount is: %d", totalAmount);
    }
}

class ParkingStats {
    private long numberCars;
    private long numberMotorcycles;
    private ParkingCash cash;
    private Object carLock = new Object();
    private Object motorcycleLock = new Object();

    public ParkingStats(ParkingCash cash) {
        numberCars = 0;
        numberMotorcycles = 0;
        this.cash = cash;
    }

    public void carComeIn() {
        synchronized (carLock) {
            numberCars++;
        }
    }

    public void carGoOut() {
        synchronized (carLock) {
            numberCars--;
        }
        cash.vehiclePay();
    }

    public void motorcycleComeIn() {
        synchronized (motorcycleLock) {
            numberMotorcycles++;
        }
    }

    public void motorcycleGoOut() {
        synchronized (motorcycleLock) {
            numberMotorcycles--;
        }
        cash.vehiclePay();
    }

    public synchronized long getNumberCars() {
        return numberCars;
    }

    public synchronized long getNumberMotorcycles() {
        return numberMotorcycles;
    }

    public synchronized ParkingCash getCash() {
        return cash;
    }
}

@AllArgsConstructor
class Sensor implements Runnable {
    private ParkingStats parkingStats;

    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            parkingStats.carComeIn();;
            parkingStats.carComeIn();
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            parkingStats.motorcycleComeIn();
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            parkingStats.motorcycleGoOut();
            parkingStats.carGoOut();
            parkingStats.carGoOut();
        }
    }
}
