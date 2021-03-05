package pl.bmaraszek.concurrency.asyc;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Generate Seed -> Generate Number List -> async(step1 | step2 | step3) -> end
 * <p>
 * step1 will calculate the nearset number to 1,000 in a list of random numbers
 * step2 will calculate the biggest number in a list of random numbers
 * step3 will calculcate the average number between the largest and smallest number in a list of random numbers
 * <p>
 * We can use CompletableFuture with 2 main purposes:
 * - wait for a value or an event with complete() and get() (throws checked exceptions), or join() (doesn't throw) methods
 * - organize a set of tasks that won't start their execution until others have finished their execution
 * <p>
 * calling get() blocks the current thread until the Future completes.
 * <p>
 * CompletableFuture.supplyAsync(Supplier s) creates a Future that returns the result of s.get()
 * Each and every function in future.thenApplyAsync(T input) will receive the result of s.get() as a param
 * <p>
 * calling CompletableFuture.allOf(CompletableFuture... args) returns a Future that will be completed when all the Futures
 * passed as params are completed.
 * <p>
 * future.thenAcceptAsync(Consumer c) executes the consumer when the future is completed.
 * <p>
 * Different ways to complete a Future:
 * - cancel() - throws a CancellationException
 * - completeAsync() - completes with the result of a supplier object passed as param. The suppler is executed in a separate thread.
 * - completeExceptionally() - throws exception passed as param
 * <p>
 * Different synchronization methods:
 * - anyOf() - completes with the result of the first Future completed
 * - runAfterBothAsync() - receives a CompletionStage and a Runnable. Runs the Runnable when Future and CompletionStage finish.
 * - runAfterEither() - similar but runs a Runnable after either Future or CompletionStage finish
 * - thenRunAsync() - similar to thenAcceptAsync(0 but it receives a Runnable instead of a Consumer
 * <p>
 * Obtain a value:
 * - getNow) - returns the completion value OR the value passed as param if not completed
 */
public class CompletableFutureExample {
    public static void main(String[] args) {
        System.out.printf("Main: start\n");
        CompletableFuture<Integer> seedFuture = new CompletableFuture<>();
        Thread seedThread = new Thread(new SeedGenerator(seedFuture));
        seedThread.start();

        System.out.printf("Main: getting the seed\n");
        int seed = 0;
        try {
            seed = seedFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.printf("The seed is %d\n", seed);
        System.out.printf("Main: Launching the list of numbers generator\n");
        NumberListGenerator task = new NumberListGenerator(seed);
        CompletableFuture<List<Long>> startFuture = CompletableFuture.supplyAsync(task);

        System.out.printf("Main: launching step 1\n");
        CompletableFuture<Long> step1Future = startFuture.thenApplyAsync(list -> {
            System.out.printf("%s: Step1: Start\n", Thread.currentThread().getName());
            long selected = 0;
            long selectedDistance = Long.MAX_VALUE;
            long distance;
            for (Long number : list) {
                distance = Math.abs(number - 1_000);
                if (distance < selectedDistance) {
                    selected = number;
                    selectedDistance = distance;
                }
            }
            System.out.printf("%s: Step 1: Result - %d\n", Thread.currentThread().getName(), selected);
            return selected;
        });

        System.out.printf("Main: Launching step 2\n");
        CompletableFuture<Long> step2Future = startFuture.thenApplyAsync(list -> list.stream().max(Long::compare).get());

        CompletableFuture<Void> write2Future = step2Future.thenAccept(selected -> {
            System.out.printf("%s: Step 2: Result - %d\n", Thread.currentThread().getName(), selected);
        });

        System.out.printf("Main: Launching step 3\n");
        NumberSelector numberSelector = new NumberSelector();
        CompletableFuture<Long> step3Future = startFuture.thenApplyAsync(numberSelector);

        System.out.printf("Main: Waiting for the end of the three steps\n");
        CompletableFuture<Void> waitFuture = CompletableFuture.allOf(step1Future, step2Future, step3Future);

        CompletableFuture<Void> finalFuture = waitFuture.thenAcceptAsync(param -> {
            // param is null because CompletableFuture.allOf(Future...) returns a CompletableFuture<Void>
            System.out.printf("Main: The CompletableFuture example has been finished");
        });
        finalFuture.join();
    }
}

@AllArgsConstructor
class SeedGenerator implements Runnable {
    private CompletableFuture<Integer> resultCommunicator;

    @SneakyThrows
    @Override
    public void run() {
        System.out.printf("SeedGenerator: Generating seed...\n");
        TimeUnit.SECONDS.sleep(5);
        int seed = (int) Math.rint(Math.random() * 10);
        System.out.printf("Seed Generator: generated %d\n", seed);
        resultCommunicator.complete(seed);
    }
}

@AllArgsConstructor
class NumberListGenerator implements Supplier<List<Long>> {
    private final int size;

    @Override
    public List<Long> get() {
        List<Long> ret = new ArrayList<>();
        System.out.printf("%s: NumberListGenerator: Start\n", Thread.currentThread().getName());

        for (int i = 0; i < size * 1_000_000; i++) {
            long number = Math.round(Math.random() * Long.MAX_VALUE);
            ret.add(number);
        }
        System.out.printf("%s: NumberListGenerator: End\n", Thread.currentThread().getName());
        return ret;
    }
}

class NumberSelector implements Function<List<Long>, Long> {

    @Override
    public Long apply(List<Long> list) {
        System.out.printf("%s: Step 3: Start\n", Thread.currentThread().getName());
        long max = list.stream().max(Long::compare).get();
        long min = list.stream().min(Long::compare).get();
        long result = (max + min) / 2;
        System.out.printf("%s: Step 3: Result - %d\n", Thread.currentThread().getName(), result);
        return result;
    }
}
