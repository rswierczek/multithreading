package learning.rasw.multithreading.countdownlatch.example5;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class BrokenWorkerTest {

    public static final int THREADS_COUNT = 5;

    @Test
    public void whenFailingToParallelProcess_thenMainThreadShouldGetNotGetStuck()
            throws InterruptedException {

        List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch countDownLatch = new CountDownLatch(THREADS_COUNT);
        List<Thread> workers = Stream
                .generate(() -> new Thread(new BrokenWorker(outputScraper, countDownLatch)))
                .limit(THREADS_COUNT)
                .collect(toList());

        workers.forEach(Thread::start);


        boolean completed = countDownLatch.await(3L, TimeUnit.SECONDS);
        assertThat(completed).isFalse();
    }
}
