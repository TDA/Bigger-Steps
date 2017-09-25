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

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // Sequential execution
        long timeStart = System.nanoTime();
        int result = Futures.amountOfDivisibleBy(0, MAX_NUMBER, 3);
        long timeEnd = System.nanoTime();
        long timeNeeded = (timeEnd - timeStart) / 1000000;
        System.out.println("Result         : " + result + " calculated in " + timeNeeded + " ms");
    }
}
