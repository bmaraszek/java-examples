package pl.bmaraszek.concurrency.executor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 *
 */
public class ExecutorProcessAllResultsExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<SimpleTask> taskList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            SimpleTask task = new SimpleTask("Task-" + i);
            taskList.add(task);
        }

        List<Future<Result>> resultList = null;

        try {
            resultList = executor.invokeAll(taskList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        System.out.printf("Main: Printing the results\n");

        for(int i = 0; i < resultList.size(); i++) {
            Future<Result> future = resultList.get(i);
            Result result = null;
            try {
                result = future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            System.out.printf("name: %s value: %s\n", result.getName(), result.getValue());
        }
    }
}

@AllArgsConstructor
@Getter
@Setter
class Result {
    private String name;
    private int value;
}

@AllArgsConstructor
class SimpleTask implements Callable<Result> {
    private final String name;

    @Override
    public Result call() throws Exception {
        System.out.printf("%s: Starting\n", this.name);
        long duration = (long) (Math.random()*10);
        System.out.printf("%s: Waiting %d seconds for results\n", this.name, duration);
        TimeUnit.SECONDS.sleep(duration);

        // to generate an int value to be returned, calculate the sum of five random numbers
        int value = 0;
        for(int i = 0; i < 5; i++) {
            value += Math.random() * 100;
        }

        System.out.printf("    %s: ends\n", this.name);
        return new Result(name, value);
    }
}