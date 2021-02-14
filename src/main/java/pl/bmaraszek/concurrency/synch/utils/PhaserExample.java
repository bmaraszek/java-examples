package pl.bmaraszek.concurrency.synch.utils;

import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * Phaser is a bit like a CyclicBarrier, where you can deregister the participants after a thread should end.
 * Threads that are sleeping in Phaser, do not respond to interruptions and do not throw InterruptedException.
 * There is an awaitAdvanceInterruptibly(int phaser) - that allows interruptions.
 * - arriveAndDeregister() to remove thread from Phaser
 * - register() or bulkRegister(int Parties) to add threads to Phaser
 *
 * If Phaser has 0 participants, it is in a terminated state.
 * forceTermination() allows to terminate at any time. Used when one of the workers has an error where the best thing
 * is to stop computation alltogether.
 * If you know that Phaser could force terminate, you should be checking if arraiveAndAwaitAdvance() returns a negative
 * number (normally it returns a positive number).
 */
public class PhaserExample {
    @SneakyThrows
    public static void main(String[] args) {
        Phaser phaser = new Phaser(3); // pass the number of worker threads in the constructor
        FileSearch desktopSearch = new FileSearch("/home/bartek/Desktop", "xml", phaser);
        FileSearch devSearch = new FileSearch("/home/bartek/dev", "xml", phaser);
        FileSearch binSearch = new FileSearch("/home/bartek/bin", "xml", phaser);

        Thread t1 = new Thread(desktopSearch, "desktop");
        Thread t2 = new Thread(devSearch, "dev");
        Thread t3 = new Thread(binSearch, "bin");

        t1.start();
        t2.start();
        t3.start();

        System.out.printf("Terminated: %s\n", phaser.isTerminated());

        t1.join();
        t2.join();
        t3.join();

        System.out.printf("Terminated: %s\n", phaser.isTerminated());
    }
}

class FileSearch implements Runnable {
    private final String initPath;
    private final String fileExtension;
    private List<String> results;
    private Phaser phaser;

    public FileSearch(String initPath, String fileExtension, Phaser phaser) {
        this.initPath = initPath;
        this.fileExtension = fileExtension;
        this.phaser = phaser;
        results = new ArrayList<>();
    }

    private void directoryProcess(File file) {
        File[] list = file.listFiles();
        if(list != null) {
            for(int i = 0; i < list.length; i++) {
                if(list[i].isDirectory()) {
                    directoryProcess(list[i]);
                } else {
                    fileProcess(list[i]);
                }
            }
        }
    }

    private void fileProcess(File file) {
        if(file.getName().endsWith(fileExtension)) {
            results.add(file.getAbsolutePath());
        }
    }

    private void filerResults() {
        List<String> newResults = new ArrayList<>();
        long actualDate = new Date().getTime();
        for(int i = 0; i < results.size(); i++) {
            File file = new File(results.get(i));
            long fileDate = file.lastModified();
            if(actualDate-fileDate < TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)) {
                newResults.add(results.get(i));
            }
        }
        results = newResults;
    }

    private boolean checkResults() {
        if(results.isEmpty()) {
            System.out.printf("%s: Phave %d: 0 results\n", Thread.currentThread().getName(), phaser.getPhase());
            System.out.printf("%s: Phase %d: End\n", Thread.currentThread().getName(), phaser.getPhase());
            phaser.arriveAndDeregister();
            return false;
        } else {
            System.out.printf("%s: Phase %d: %d results\n", Thread.currentThread().getName(), phaser.getPhase(), results.size());
            phaser.arriveAndAwaitAdvance();
            return true;
        }
    }

    private void showInfo() {
        for(int i = 0; i < results.size(); i++) {
            File file = new File(results.get(i));
            System.out.printf("### ShowInfo: %s: %s\n", Thread.currentThread().getName(), file.getAbsolutePath());
        }
        phaser.arriveAndAwaitAdvance();
    }

    @Override
    public void run() {
        phaser.arriveAndAwaitAdvance();
        System.out.printf("%s: Starting\n", Thread.currentThread().getName());
        File file = new File(initPath);
        if(file.isDirectory()) {
            directoryProcess(file);
        }
        if(!checkResults()) {
            return;
        }
        filerResults();
        if(!checkResults()) {
            return;
        }
        showInfo();
        phaser.arriveAndDeregister();
        System.out.printf("%s: Work completed\n", Thread.currentThread().getName());
    }

}