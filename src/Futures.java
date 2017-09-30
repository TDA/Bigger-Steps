import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.*;

public class Futures {
    public static final int MAX_NUMBER = 2000000000;

    // Some long running task
    public static int amountOfDivisibleBy(int first, int last, int divisor) {
        int amount = 0;
        for (int i = first; i <= last; i++) {
            if (i % divisor == 0) {
                amount++;
            }
        }
        return amount;
    }

    private static int amountOfDivisibleByFuture(int first, int last, int divisor) throws InterruptedException, ExecutionException {
        int amount = 0;

        // Futures ftw
        int numThreads = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Future<Integer>> futureList = new ArrayList<>();

        // Start thread for the first fourth of the numbers
        FutureTask<Integer> futureTask_1 = new FutureTask<>(() -> {
            System.out.println(String.format("From %s to %s", first, last / 4));
            return Futures.amountOfDivisibleBy(first, last / 4, divisor);
        });
        futureList.add(futureTask_1);
        executorService.execute(futureTask_1);

        // Start thread for the second fourth of the numbers
        FutureTask<Integer> futureTask_2 = new FutureTask<>(() -> {
            System.out.println(String.format("From %s to %s", last / 4 + 1, last / 4 * 2));
            return Futures.amountOfDivisibleBy(last / 4 + 1, last / 4 * 2, divisor);
        });
        futureList.add(futureTask_2);
        executorService.execute(futureTask_2);

        // Start thread for the last fourth of the numbers
        FutureTask<Integer> futureTask_3 = new FutureTask<>(() -> {
            System.out.println(String.format("From %s to %s", last / 4 * 2, last));
            return Futures.amountOfDivisibleBy(last / 4 * 2 + 1, last / 4 * 3, divisor);
        });
        futureList.add(futureTask_3);
        executorService.execute(futureTask_3);

        // Start thread for the last fourth of the numbers
        FutureTask<Integer> futureTask_4 = new FutureTask<>(() -> {

            System.out.println(String.format("From %s to %s", last / 4 * 3, last));
            return Futures.amountOfDivisibleBy(last / 4 * 3 + 1, last, divisor);
        });
        futureList.add(futureTask_4);
        executorService.execute(futureTask_4);


        for (int j = 0; j < numThreads; j++) {
            Future<Integer> futureTask = futureList.get(j);
            amount += futureTask.get();
        }
        executorService.shutdown();

        return amount;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // Sequential execution
        long timeStart = System.nanoTime();
        int result = Futures.amountOfDivisibleBy(0, MAX_NUMBER, 3);
        long timeEnd = System.nanoTime();
        long timeNeeded = (timeEnd - timeStart) / 1000000;
        System.out.println("Result         : " + result + " calculated in " + timeNeeded + " ms");


        // Parallel execution
        long timeStartFuture = System.nanoTime();
        int resultFuture = Futures.amountOfDivisibleByFuture(0, MAX_NUMBER, 3);
        long timeEndFuture = System.nanoTime();
        long timeNeededFuture = (timeEndFuture - timeStartFuture) / 1000000;
        System.out.println("Result (Future): " + resultFuture + " calculated in " + timeNeededFuture + " ms");
    }
}
