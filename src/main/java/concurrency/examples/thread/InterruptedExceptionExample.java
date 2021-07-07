package concurrency.examples.thread;

import lombok.AllArgsConstructor;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class InterruptedExceptionExample {
    public static void main(String[] args) {
        FileSearch fileSearch = new FileSearch("/home/bartek", "README.md");
        Thread thread = new Thread(fileSearch);
        thread.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }
}

@AllArgsConstructor
class FileSearch implements Runnable {
    private String initPath;
    private String fileName;

    /**
     * In this example, the Thread.interrupted() check is implemented in each method that can be called from run()
     * Handling the exception is done in the run() method
     */

    @Override
    public void run() {
        File file = new File(initPath);
        if(file.isDirectory()) {
            try {
                directoryProcess(file);
            } catch(InterruptedException e) {
                System.out.printf("%s: The search has been interrupted", Thread.currentThread().getName());
            }
        }
    }

    private void directoryProcess(File file) throws InterruptedException {
        File[] list = file.listFiles();
        if(list != null) {
            for(int i = 0; i <list.length; i++) {
                if(list[i].isDirectory()) {
                    directoryProcess(list[i]);
                } else {
                    fileProcess(list[i]);
                }
            }
            if(Thread.interrupted()) {
                throw new InterruptedException();
            }
        }
    }

    private void fileProcess(File file) throws InterruptedException {
        if(file.getName().equals(fileName)) {
            System.out.printf("%s: %s\n", Thread.currentThread().getName(), file.getAbsolutePath());
        }
        if(Thread.interrupted()) {
            throw new InterruptedException();
        }
    }
}
