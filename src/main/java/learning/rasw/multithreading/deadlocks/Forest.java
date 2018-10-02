package learning.rasw.multithreading.deadlocks;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import java.util.List;

import java.util.concurrent.CountDownLatch;

import java.util.concurrent.ExecutorService;

import java.util.concurrent.TimeUnit;

import java.util.concurrent.TimeoutException;

import java.util.function.Supplier;

import java.util.stream.Collectors;

import java.util.stream.IntStream;


/**
 * Now, what would happen if all the Lumberjacks were yolo, i.e., they all tried to pick the chainsaw first?
 * It turns out that the easiest way to avoid deadlocks is to obtain and release locks always in the same order.
 * For example, you can sort your resources based on some arbitrary criteria.
 * If one thread obtains lock A followed by B, whereas the second thread obtains B first, it’s a recipe for a deadlock.
 */
@RequiredArgsConstructor
public class Forest implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(Forest.class);

    private final ExecutorService pool;

    private final Logging logging;

    void cutTrees(int howManyTrees, int carefulLumberjacks, int yoloLumberjacks) throws InterruptedException, TimeoutException {
        CountDownLatch latch = new CountDownLatch(howManyTrees);
        List<Lumberjack> lumberjacks = new ArrayList<>();
        lumberjacks.addAll(generate(carefulLumberjacks, logging::careful));
        lumberjacks.addAll(generate(yoloLumberjacks, logging::yolo));

        IntStream
                .range(0, howManyTrees)
                .forEach(x -> {
                    Lumberjack roundRobinJack = lumberjacks.get(x % lumberjacks.size());
                    pool.submit(() -> {
                        log.debug("{} cuts down tree, {} left", roundRobinJack, latch.getCount());
                        roundRobinJack.cut(latch::countDown);
                    });
                });

        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new TimeoutException("Cutting forest for too long");
        }
        log.debug("Cut all trees");
    }

    private List<Lumberjack> generate(int count, Supplier<Lumberjack> factory) {

        return IntStream
                .range(0, count)
                .mapToObj(x -> factory.get())
                .collect(Collectors.toList());
    }

    @Override

    public void close() {
        pool.shutdownNow();
    }

}
