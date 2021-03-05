package pl.bmaraszek.concurrency.executor;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * The result of a callable can be checked using isDone() method.
 * You get a value out of a Future by calling get() - this is a blocking operation.
 */
public class ExecutorReturnValuesExample {
    @SneakyThrows
    public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        List<Future<Integer>> resultList = new ArrayList<>();
        Random random = new Random();

        for(int i = 0; i < 10; i++) {
            Integer number = random.nextInt(10);
            FactorialCalculator calculator = new FactorialCalculator(number);
            Future<Integer> result = executor.submit(calculator);
            resultList.add(result);
        }

        do {
            System.out.printf("Main: Number of Completed Tasks: %d\n", executor.getCompletedTaskCount());
            for(int i = 0; i < resultList.size(); i++) {
                Future<Integer> result = resultList.get(i);
                System.out.printf("Main: Task %d: %s\n", i, result.isDone());
                TimeUnit.MILLISECONDS.sleep(50);
            }
        } while(executor.getCompletedTaskCount() < resultList.size());

        System.out.printf("Main: Results:\n");
        for(int i = 0; i < resultList.size(); i++) {
            Future<Integer> result = resultList.get(i);
            Integer number = result.get();
            System.out.printf("Main: Task %d: %d\n", i, number);
        }

        executor.shutdown();
    }
}

@AllArgsConstructor
class FactorialCalculator implements Callable<Integer> {
    private final Integer number;

    @Override
    public Integer call() throws Exception {
        int result = 1;
        if(number == 0 || number == 1) {
            result = 1;
        } else {
            for(int i = 2; i <= number; i++) {
                result *= i;
                TimeUnit.MILLISECONDS.sleep(20);
            }
        }
        System.out.printf("%s: %d! => %d\n", Thread.currentThread().getName(), number, result);
        return result;
    }
}
