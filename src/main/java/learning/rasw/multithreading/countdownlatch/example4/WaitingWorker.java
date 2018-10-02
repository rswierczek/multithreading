package learning.rasw.multithreading.countdownlatch.example4;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * https://www.baeldung.com/java-countdown-latch
 * 4. A Pool of Threads Waiting to Begin
 *
 * If we took the previous example, but this time started thousands of threads instead of five, it’s likely that many of the earlier ones will have finished processing before we have even called start() on the later ones. This could make it difficult to try and reproduce a concurrency problem, as we wouldn’t be able to get all our threads to run in parallel.
 *
 * To get around this, let’s get the CountdownLatch to work differently than in the previous example. Instead of blocking a parent thread until some * child threads have finished, we can block each child thread until all the others have started.
 *
 *
 * This pattern is really useful for trying to reproduce concurrency bugs, as can be used to force thousands of threads to try and perform
 * some logic in parallel.
 */
public class WaitingWorker implements Runnable {

    private List<String> outputScraper;
    private CountDownLatch readyThreadCounter;
    private CountDownLatch callingThreadBlocker;
    private CountDownLatch completedThreadCounter;

    public WaitingWorker(
            List<String> outputScraper,
            CountDownLatch readyThreadCounter,
            CountDownLatch callingThreadBlocker,
            CountDownLatch completedThreadCounter) {

        this.outputScraper = outputScraper;
        this.readyThreadCounter = readyThreadCounter;
        this.callingThreadBlocker = callingThreadBlocker;
        this.completedThreadCounter = completedThreadCounter;
    }

    @Override
    public void run() {
        readyThreadCounter.countDown();
        try {
            callingThreadBlocker.await();
            doSomeWork();
            outputScraper.add("Counted down");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            completedThreadCounter.countDown();
        }
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
