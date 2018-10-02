package learning.rasw.multithreading.countdownlatch.example5;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 5. Terminating a CountdownLatch Early
 *
 * Sometimes, we may run into a situation where the Workers terminate in error before counting down the CountDownLatch. This could result in it never reaching zero and await() never terminating:
 */
public class BrokenWorker implements Runnable {
    private List<String> outputScraper;
    private CountDownLatch countDownLatch;

    public BrokenWorker(List<String> outputScraper, CountDownLatch countDownLatch) {
        this.outputScraper = outputScraper;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        if (true) {
            throw new RuntimeException("Oh dear, I'm a BrokenWorker");
        }
        countDownLatch.countDown();
        outputScraper.add("Counted down");
    }
}
