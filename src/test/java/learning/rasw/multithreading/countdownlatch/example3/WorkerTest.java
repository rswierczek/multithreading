package learning.rasw.multithreading.countdownlatch.example3;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class WorkerTest {

    public static final int THREADS_COUNT = 5;

    @Test
    public void whenParallelProcessing_thenMainThreadWillBlockUntilCompletion()
            throws InterruptedException {

        List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch countDownLatch = new CountDownLatch(THREADS_COUNT);
        List<Thread> workers = Stream
                .generate(() -> new Thread(new Worker(outputScraper, countDownLatch)))
                .limit(THREADS_COUNT)
                .collect(toList());

        workers.forEach(Thread::start);
        /*
        Naturally “Latch released” will always be the last output – as it’s dependant on the CountDownLatch releasing.
        Note that if we didn’t call await(), we wouldn’t be able to guarantee the ordering of the execution of the threads, so the test would randomly fail.
         */
        countDownLatch.await();
        outputScraper.add("Latch released");

        assertThat(outputScraper)
                .containsExactly(
                        "Counted down",
                        "Counted down",
                        "Counted down",
                        "Counted down",
                        "Counted down",
                        "Latch released"
                );
    }

}
