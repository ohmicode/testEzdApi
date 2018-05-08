import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ApiClient {

    private static final int DEFAULT_TRIES  = 50;
    private static final int POOL_SIZE  = 10;

    private static final String SERVER_NAME  = "http://localhost:8080";
    private static final List<String> ENDPOINTS = Arrays.asList(SERVER_NAME + "/api/student_homeworks/248270950", SERVER_NAME + "/api/marks");

    private static Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
        runTests(DEFAULT_TRIES, ENDPOINTS);
    }

    private static void runTests(int size, List<String> variants) {
        CountDownLatch countDownLatch = new CountDownLatch(size);

        ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);
        IntStream.range(0, size).forEach(i -> {
            String api = pickRandom(variants);
            executor.execute(new ApiRunnable(api, countDownLatch));
            countDownLatch.countDown();
        });
        executor.shutdown();
        try {
            executor.awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            executor.shutdownNow();
        }
    }

    private static String pickRandom(List<String> variants) {
        return variants.get(random.nextInt(variants.size()));
    }
}
