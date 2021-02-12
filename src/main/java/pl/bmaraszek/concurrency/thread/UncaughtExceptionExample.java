package pl.bmaraszek.concurrency.thread;

public class UncaughtExceptionExample {
    public static void main(String[] args) {
        Task task = new Task();
        Thread thread = new Thread(task);
        /**
         * You can set the exception handler for:
         * - A particular thread
         * - A thread group (for all the threads in teh group at once)
         * - Default for all threads in via a static method on Thread class
         */
        thread.setUncaughtExceptionHandler(new ExceptionHander());
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHander());
        thread.start();
    }
}

class ExceptionHander implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.printf("An exception has been captured\n");
        System.out.printf("Thread: %s\n", t.getId());
        System.out.printf("Exception: %s: %s\n", e.getClass().getName(), e.getMessage());
        System.out.printf("Stack Trace: \n");
        e.printStackTrace(System.out);
        System.out.printf("Thread status: %s\n", t.getState());
    }
}

class Task implements Runnable {
    @Override
    public void run() {
        int number = Integer.parseInt("Invalid number");
        System.out.printf("### After throwing the exception\n");
    }
}
