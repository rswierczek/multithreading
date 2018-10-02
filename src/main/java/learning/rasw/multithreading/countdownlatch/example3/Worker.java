package learning.rasw.multithreading.countdownlatch.example3;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * https://www.baeldung.com/java-countdown-latch
 * 2. Usage in Concurrent Programming
 *
 * Simply put, a CountDownLatch has a counter field, which you can decrement as we require. We can then use it to block a calling thread until it’s been counted down to zero.
 *
 * If we were doing some parallel processing, we could instantiate the CountDownLatch with the same value for the counter as a number of threads we want to work across. Then, we could just call countdown() after each thread finishes, guaranteeing that a dependent thread calling await() will block until the worker threads are finished.
 * 3. Waiting for a Pool of Threads to Complete
 *
 * Let’s try out this pattern by creating a Worker and using a CountDownLatch field to signal when it has completed:
 */
public class Worker implements Runnable {
    private List<String> outputScraper;
    private CountDownLatch countDownLatch;

    public Worker(List<String> outputScraper, CountDownLatch countDownLatch) {
        this.outputScraper = outputScraper;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        doSomeWork();
        outputScraper.add("Counted down");
        countDownLatch.countDown();
    }

    private void doSomeWork() {
        System.out.println("[START] Worker doSomeWork- " + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[END] Worker doSomeWork - " + Thread.currentThread().getName());
    }
}
