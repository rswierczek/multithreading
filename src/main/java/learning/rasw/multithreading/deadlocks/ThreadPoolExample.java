package learning.rasw.multithreading.deadlocks;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * https://dzone.com/articles/thread-pool-self-induced-deadlocks
 *
 * Thread Pool Self-Induced Deadlocks
 *
 * This was an example of a deadlock, rather than a simple one. But, it turns out that a single thread pool can cause a deadlock when used incorrectly. Imagine you have an ExecutorService, just like in the previous example, that you use, as shown below:
 */
@Slf4j
public class ThreadPoolExample {
    public static void main(String[] args) {
        //threadPoolWithBlockButEnoughAvailableThreads();
        threadPoolWithoutBlockButNotEnoughAvailableThreads();

       // deadlockWhenThreadPoolWithBlockAndNotEnoughAvailableThreads();
    }


    /**
     * Notice that we block, see  get(), waiting for the inner Runnable to complete before we display "Third." It’s a trap! Waiting for the inner   task to complete means it must acquire a thread from a thread pool in order to proceed. However, we already acquired one thread, therefore, the inner will be blocked until it can get the second. Our thread pool is large enough at the moment, so it works fine.
     */
    private static void threadPoolWithBlockButEnoughAvailableThreads() {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        pool.submit(() -> {
            try {
                log.info("First");
                pool.submit(() -> log.info("Second")).get(); //get() - block thread
                log.info("Third");
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error", e);
            }
        });
    }

    private static void threadPoolWithoutBlockButNotEnoughAvailableThreads() {
        ExecutorService pool = Executors.newSingleThreadExecutor();

        pool.submit(() -> {
            log.info("First");
            pool.submit(() -> log.info("Second"));
            log.info("Third");

        });
    }
/*
     Deadlock! Step-by-step:
    Task printing "First" is submitted to an idle single-threaded pool
    This task begins execution and prints "First"
    We submit an inner task printing "Second" to a thread pool
    The inner task lands in a pending task queue. No threads are available since the only one is currently being occupied
    We block waiting for the result of the inner task. Unfortunately, while waiting for the inner task, we hold the only available thread
    get() will wait forever, unable to acquire thread
    deadlock
Does it mean having a single-thread pool is bad? Not really. The same problem could occur with a thread pool of any size. But, in that case, a deadlock may occur only under high load, which is much worse from a maintenance perspective. You could technically have an unbounded thread pool, but that’s even worse.
 */
    private static void deadlockWhenThreadPoolWithBlockAndNotEnoughAvailableThreads(){
        ExecutorService pool = Executors.newSingleThreadExecutor();

        pool.submit(() -> {
            try {
                log.info("First");
                pool.submit(() -> log.info("Second")).get(); //get() - block thread
                log.info("Third");
            } catch (InterruptedException | ExecutionException e) {

                log.error("Error", e);

            }
        });
    }



}
