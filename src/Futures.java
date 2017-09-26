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
        int numThreads = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Future<Integer>> futureList = new ArrayList<>();
        // Start thread for the first half of the numbers
        FutureTask<Integer> futureTask_1 = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() {
                return Futures.amountOfDivisibleBy(first, last, divisor);
            }
        });
        futureList.add(futureTask_1);
        executorService.execute(futureTask_1);
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
