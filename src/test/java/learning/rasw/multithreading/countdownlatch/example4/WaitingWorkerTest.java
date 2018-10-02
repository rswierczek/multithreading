package learning.rasw.multithreading.countdownlatch.example4;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class WaitingWorkerTest {

    public static final int THREADS_COUNT = 5;
    public static final int CALLING_THREAD_COUNT = 1;

    /**
     * Now, let’s modify our test so it blocks until all the Workers have started, unblocks the Workers,
     * and then blocks until the Workers have finished:
     */
    @Test
    public void whenDoingLotsOfThreadsInParallel_thenStartThemAtTheSameTime()
            throws InterruptedException {

        List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch readyThreadCounter = new CountDownLatch(THREADS_COUNT);
        CountDownLatch callingThreadBlocker = new CountDownLatch(CALLING_THREAD_COUNT);
        CountDownLatch completedThreadCounter = new CountDownLatch(THREADS_COUNT);
        List<Thread> workers = Stream
                .generate(() -> new Thread(new WaitingWorker(
                        outputScraper, readyThreadCounter, callingThreadBlocker, completedThreadCounter)))
                .limit(THREADS_COUNT)
                .collect(toList());

        workers.forEach(Thread::start);
        readyThreadCounter.await();
        outputScraper.add("Workers ready");
        callingThreadBlocker.countDown();
        completedThreadCounter.await();
        outputScraper.add("Workers complete");

        assertThat(outputScraper)
                .containsExactly(
                        "Workers ready",
                        "Counted down",
                        "Counted down",
                        "Counted down",
                        "Counted down",
                        "Counted down",
                        "Workers complete"
                );
    }

}
