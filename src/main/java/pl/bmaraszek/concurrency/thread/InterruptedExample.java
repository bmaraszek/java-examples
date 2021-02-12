package pl.bmaraszek.concurrency.thread;

public class InterruptedExample {
    public static void main(String[] args) {
        Thread task = new PrimeGenerator();
        task.start();

        try {
            Thread.sleep(500);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        task.interrupt(); // sets the interrupted flag to true
        System.out.printf("Main: Status of the Thread: %s\n", task.getState());
        System.out.printf("Main: isInterrupted: %s\n", task.isInterrupted());
        System.out.printf("Main: isAlive: %s\n", task.isAlive());
    }
}

class PrimeGenerator extends Thread {
    @Override
    public void run() {
        long number = 1L;
        while(true) {
            if(isPrime(number)) {
                System.out.printf("Number %d is Prime\n", number);
            }
            if(isInterrupted()) {
                // also static boolean interupted() method => check and set flag to false
                // check the interrupted flag
                // a thread can ignore interruption but it's not the expected behaviour
                System.out.println("Prime generator has been interrupted");
                return; // end thread
            }
            number++;
        }
    }

    public boolean isPrime(long number) {
        for(int i = 2; i < number; i++) {
            if(number % i == 0) return false;
        }
        return true;
    }
}
