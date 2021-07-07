package concurrency.examples.synch.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * The CyclicBarrier can execute a new thread with a Runnable passed as a param in its constructor.
 * In this example, it's the grouper object that sums up partial results.
 *
 * - reset() sets the count of a cyclic barrier to the initial value and all waiting threads (after calling await())
 * receive a BrokenBarrierException.
 *
 * A CyclicBarrier can be in a special state denoted by the broken state. When a thread waiting in the await() method
 * is interrupted, it receives an InterruptedException and other threads receive a BrokenBarrierException.
 * The CyclicBarrier is then placed in a broken state that can be checked using the isBroken() method.
 *
 * Because of the waiting mechanism and executing a callback, this is useful for writing tests for concurrent code.
 */
public class CyclicBareerExample {
    public static void main(String[] args) {
        final int ROWS = 10_000;
        final int COLUMNS = 1_000;
        final int SEARCH = 5;
        final int PARTICIPANTS = 5;
        final int LINES_PER_PARTICIPANT = ROWS / PARTICIPANTS;

        MatrixMock matrix = new MatrixMock(ROWS, COLUMNS, SEARCH);
        Results results = new Results(ROWS);
        Grouper grouper = new Grouper(results);

        CyclicBarrier barrier = new CyclicBarrier(PARTICIPANTS, grouper);

        Searcher[] searchers = new Searcher[PARTICIPANTS];
        for(int i = 0; i < PARTICIPANTS; i++) {
            searchers[i] = new Searcher(
                    i*LINES_PER_PARTICIPANT,
                    (i*LINES_PER_PARTICIPANT) + LINES_PER_PARTICIPANT,
                    matrix,
                    results,
                    5,
                    barrier);
            Thread thread = new Thread(searchers[i]);
            thread.start();
        }

        System.out.println("THE MAIN THREAD HAS FINISHED");
    }
}

class MatrixMock {
    private final int data[][];

    public MatrixMock(int rows, int columns, int number) {
        int counter = 0;
        data = new int[rows][columns];
        Random random = new Random(new Date().getTime());
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < columns; col++) {
                data[row][col] = random.nextInt(10);
                if(data[row][col] == number) {
                    counter++;
                }
            }
        }
        System.out.printf("Mock: There are %d occurrences of number %d in the generated data\n", counter, number);
    }

    public int[] getRow(int row) {
        if(row >= 0 && row < data.length) {
            return data[row];
        }
        return null;
    }
}

@Getter
class Results {
    private final int data[];

    public Results(int size) {
        data = new int[size];
    }

    public void setData(int position, int value) {
        data[position] = value;
    }
}

@AllArgsConstructor
class Searcher implements Runnable {
    private final int firstRow;
    private final int lastRow;
    private final MatrixMock mock;
    private final Results results;
    private final int number;
    private final CyclicBarrier barrier;

    @Override
    public void run() {
        int counter;
        System.out.printf("%s: Processing lines from %d to %d\n", Thread.currentThread().getName(), firstRow, lastRow);
        for(int row = firstRow; row < lastRow; row++) {
            int[] currentRow = mock.getRow(row);
            counter = 0;
            for(int col = 0; col < currentRow.length; col++) {
                if(currentRow[col] == number) {
                    counter++;
                }
            }
            results.setData(row, counter);
        }
        System.out.printf("%s: Lines processed\n", Thread.currentThread().getName());
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

@AllArgsConstructor
class Grouper implements Runnable {
    private final Results results;
    @Override
    public void run() {
        int finalResult = 0;
        System.out.println("Grouper: Processing results...");
        int data[] = results.getData();
        for(int number : data) {
            finalResult += number;
        }
        System.out.printf("Grouper: Total result: %d\n", finalResult);
    }
}