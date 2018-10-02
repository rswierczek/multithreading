package learning.rasw.multithreading.countdownlatch.example2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Driver2 {
    private static final int N = 10;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch doneSignal = new CountDownLatch(N);
        Executor executor = Executors.newFixedThreadPool(N);

        for (int i = 0; i < N; ++i) {// create and start threads
            executor.execute(new WorkerRunnable(doneSignal, i));
        }

        doneSignal.await();           // wait for all to finish
    }
}
