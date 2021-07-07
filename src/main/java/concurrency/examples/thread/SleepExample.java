package concurrency.examples.thread;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SleepExample {
    public static void main(String[] args) {
        ConsoleClock consoleClock = new ConsoleClock();
        Thread thread = new Thread(consoleClock);
        thread.start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }
}

class ConsoleClock implements Runnable {
    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            System.out.printf("%s\n", new Date());
            try {
                // sleep causes the thread to leave the CPU for other tasks
                // yield() is similar. It's essentially sleep(0), which allows other threads in
                // yield's behaviour differes between Windows and Linux
                // https://stackoverflow.com/questions/6979796/what-are-the-main-uses-of-yield-and-how-does-it-differ-from-join-and-interr
                TimeUnit.SECONDS.sleep(1);
                // when a thread is sleeping and is interrupted
                // InterruptedException is thrown immediately not after sleep is finished
            } catch (InterruptedException e) {
                System.out.printf("The clock has been interrupted\n");
            }
        }
    }
}
