package learning.rasw.multithreading.deadlocks;

import learning.rasw.multithreading.deadlocks.Logging;
import learning.rasw.multithreading.deadlocks.Names;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;


public class ForestTest {

    @Test
    public void noDeadlockWhenOnlyCareful() throws InterruptedException, TimeoutException {
        ExecutorService pool = Executors.newFixedThreadPool(10);

        Logging logging = new Logging(new Names());

        try (Forest forest = new Forest(pool, logging)) {
            forest.cutTrees(10_000, 10, 0);
        }
    }

    @Test(expected = TimeoutException.class)
    public void deadlockWhenCarefulAndYolo() throws InterruptedException, TimeoutException {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        Logging logging = new Logging(new Names());

        try (Forest forest = new Forest(pool, logging)) {

            forest.cutTrees(10_000, 10, 2);

        }
    }

}
