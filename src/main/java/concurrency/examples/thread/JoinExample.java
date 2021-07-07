package concurrency.examples.thread;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class JoinExample {
    public static void main(String[] args) {
        DataSourcesLoader dataSourcesLoader = new DataSourcesLoader();
        NetworkConnectionsLoader networkConnectionsLoader = new NetworkConnectionsLoader();

        Thread t1 = new Thread(dataSourcesLoader, "Data Sources Loader");
        Thread t2 = new Thread(networkConnectionsLoader, "Network Connection Loader");

        t1.start();
        t2.start();

        try {
            t1.join(); // main should wait for t1 to finish
            t2.join(); // main shoud wait for t2 to finish. Comment out to see the difference

            // also: t2.join(100); - wait for t2 to finish or 100ms whichever is first
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Main: Configuration has been loaded: %s\n", new Date());
    }
}

class DataSourcesLoader implements Runnable {
    @Override
    public void run() {
        System.out.printf("Beginning data sources loading: %s\n", new Date());
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Data soures loading has finished %s\n", new Date());
    }
}

class NetworkConnectionsLoader implements Runnable {
    @Override
    public void run() {
        // same as DataSourcesLoader but sleeps for 6 seconds instead of 4
        System.out.printf("Beginning data sources loading: %s\n", new Date());
        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Data soures loading has finished %s\n", new Date());
    }
}